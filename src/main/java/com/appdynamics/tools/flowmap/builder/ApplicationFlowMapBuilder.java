package com.appdynamics.tools.flowmap.builder;

import com.appdynamics.controller.dto.*;
import com.appdynamics.tools.ControllerService;
import com.appdynamics.tools.flowmap.dto.*;
import com.appdynamics.tools.util.HttpResponseFuture;
import com.appdynamics.tools.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by abey.tom on 10/19/16.
 */
public class ApplicationFlowMapBuilder {
    public static final Logger logger = LoggerFactory.getLogger(ApplicationFlowMapBuilder.class);
    private ControllerService controllerService;
    private FlowMapRequest request;

    protected Map<String, Backend> backendMap;
    protected Map<String, Tier> tierMap;
    protected Map<String, Application> applicationMap;
    protected Application application;
    protected ExecutorService executorService;

    public ApplicationFlowMapBuilder(ControllerService controllerService, FlowMapRequest request,
                                     Map<String, Application> applicationMap, Application application) {
        this(controllerService, request, applicationMap, application, null);
    }

    public ApplicationFlowMapBuilder(ControllerService controllerService, FlowMapRequest request,
                                     Map<String, Application> applicationMap, Application application,
                                     ExecutorService executorService) {
        this.controllerService = controllerService;
        this.request = request;
        this.applicationMap = applicationMap;
        this.application = application;
        this.executorService = executorService;
    }

    public ApplicationFlowMapBuilder createTierMap(Tier[] tiers) {
        this.tierMap = Mapper.toMap(tiers, new Mapper.KeyGetter<String, Tier>() {
            @Override
            public String getKey(Tier value) {
                return value.getName();
            }
        });
        return this;
    }

    public ApplicationFlowMapBuilder createBackendMap(Backend[] backends) {
        this.backendMap = Mapper.toMap(backends, new Mapper.KeyGetter<String, Backend>() {
            @Override
            public String getKey(Backend value) {
                return value.getName();
            }
        });
        return this;
    }

    protected void addNodesToTier(Controller controller, Application application, Tier[] tiers) {
        Node[] nodes = controllerService.getApplicationNodes(controller, application).getData();
        if (nodes != null && tierMap != null) {
            for (Node node : nodes) {
                String tierId = node.getTierName();
                Tier tier = tierMap.get(tierId);
                if (tier != null) {
                    tier.addNode(node);
                }
            }
        }
    }

    public void addEdges(FlowMap flowMap, Tier tier, List<ExternalCall> externalCalls) {
        FlowMapNode srcFlowMapNode = FlowMapNode.from(tier);
        flowMap.addNode(srcFlowMapNode);
        if (externalCalls != null) {
            for (ExternalCall call : externalCalls) {
                AnEntity backend = getBackend(call);
                if (backend != null) {
                    FlowMapNode destFlowMapNode = FlowMapNode.from(backend);
                    flowMap.addNode(destFlowMapNode);
                    FlowMapEdge flowMapEdge = new FlowMapEdge(srcFlowMapNode, destFlowMapNode);
                    flowMap.addEdge(flowMapEdge);
                }
            }
        }
    }

    private AnEntity getBackend(ExternalCall call) {
        ExternalCall.EntityType entityType = call.getEntityType();
        String name = call.getEntityName();
        if (entityType == ExternalCall.EntityType.APPLICATION) {
            Application application = applicationMap.get(name);
            if (application != null) {
                return application;
            } else {
                logger.error("Cannot locate the application with name {}", name);
            }
        } else if (entityType == ExternalCall.EntityType.TIER) {
            Tier tier = tierMap.get(name);
            if (tier != null) {
                return tier;
            } else {
                logger.error("Cannot locate the tier with name {}", name);
            }
        } else if (entityType == ExternalCall.EntityType.BACKEND) {
            Backend backend = backendMap.get(name);
            if (backend != null) {
                return backend;
            } else {
                logger.error("Cannot locate the tier with name {}", name);
            }
        } else {
            logger.error("unknown Entity name={}, type={}", name, entityType);
        }
        return null;
    }

    public ApplicationFlowMap build() {
        Controller controller = request.getController();
        //Fetch the Data
        HttpResponseFuture<Tier[]> tiersFuture = controllerService.getTiers(controller, application);
        HttpResponseFuture<Backend[]> backendsFuture = controllerService.getBackends(controller, application);
        Tier[] tiers = tiersFuture.getData();
        Backend[] backends = backendsFuture.getData();
        createTierMap(tiers);
        createBackendMap(backends);

        //Create the flowmap
        FlowMap flowMap = new FlowMap();
        flowMap.setApplication(application);

        flowMap.setController(controller);
        ArrayList<Application> applications = new ArrayList<>(applicationMap.values());
        Collections.sort(applications, new Comparator<Application>() {
            @Override
            public int compare(Application o1, Application o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        flowMap.setApplications(applications.toArray(new Application[applications.size()]));
        flowMap.setTiers(tiers);
        if (tiers != null) {
            //Trigger GET the external calls for a given tier.
            Map<Tier, HttpResponseFuture<MetricNode[]>> futureMap = new LinkedHashMap<>();
            for (Tier tier : tiers) {
                HttpResponseFuture<MetricNode[]> future = controllerService.getExternalCalls(controller, application, tier);
                futureMap.put(tier, future);
            }
            // Add nodes to tier is blocking
            addNodesToTier(controller, application, tiers);
            //get the external calls data
            for (Tier tier : futureMap.keySet()) {
                List<ExternalCall> externalCalls = ExternalCall.from(futureMap.get(tier).getData());
                if (externalCalls != null) {
                    addEdges(flowMap, tier, externalCalls);
                    tier.setExtCalls(externalCalls);
                    setEntityIdToExternalCall(externalCalls);
                }
            }
        }
        return ApplicationFlowMap.from(flowMap);
    }

    private void setEntityIdToExternalCall(List<ExternalCall> externalCalls) {
        for (ExternalCall externalCall : externalCalls) {
            switch (externalCall.getEntityType()) {
                case APPLICATION:
                    String applicationName = externalCall.getApplicationName();
                    if (applicationName != null && applicationMap.get(applicationName) != null) {
                        externalCall.setEntityId(applicationMap.get(applicationName).getId());
                    }
                    break;
                case BACKEND:
                    Backend backend = backendMap.get(externalCall.getEntityName());
                    externalCall.setBackend(backend);
                    if (backend != null) {
                        externalCall.setEntityId(backend.getId());
                    }
                    break;
                case TIER:
                    String tierName = externalCall.getTierName();
                    if (tierName != null && tierMap.get(tierName) != null) {
                        externalCall.setEntityId(tierMap.get(tierName).getId());
                    }
                    break;
            }
        }
    }
}
