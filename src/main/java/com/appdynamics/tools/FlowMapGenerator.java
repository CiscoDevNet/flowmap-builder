package com.appdynamics.tools;

import com.appdynamics.controller.dto.Application;
import com.appdynamics.controller.dto.Controller;
import com.appdynamics.tools.flowmap.builder.ApplicationFlowMapBuilder;
import com.appdynamics.tools.flowmap.dto.ApplicationFlowMap;
import com.appdynamics.tools.flowmap.dto.ControllerFlowMap;
import com.appdynamics.tools.flowmap.dto.FlowMapRequest;
import com.appdynamics.tools.util.Filters;
import com.appdynamics.tools.util.Mapper;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMapGenerator {
    public static final Logger logger = LoggerFactory.getLogger(FlowMapGenerator.class);
    protected final ControllerService controllerService;
    private ExecutorService executorService;
    protected FlowMapRequest request;
    protected Controller controller;

    //This is needed while resolving the name of cross app calls.
    private final Map<String, Application> applicationMap;

    /**
     * Use this for single threaded execution using the default http client HttpClients.createDefault()
     *
     * @param request
     */
    public FlowMapGenerator(FlowMapRequest request) {
        this(request, HttpAsyncClients.createDefault());
    }

    /**
     * Use a customized Http Client if needed. This also runs single threaded
     *
     * @param request
     * @param httpClient
     */
    public FlowMapGenerator(FlowMapRequest request, CloseableHttpAsyncClient httpClient) {
        this(request, httpClient, null);
    }

    /**
     * Use this for multi threaded execution. If the ExecutorService is not null, this will be executed in the given thread pool.
     *
     * @param request
     * @param httpClient
     * @param executorService
     */
    public FlowMapGenerator(FlowMapRequest request, CloseableHttpAsyncClient httpClient, ExecutorService executorService) {
        this.request = request;
        this.controller = request.getController();
        this.controllerService = new ControllerService(httpClient);
        this.executorService = executorService;
        this.applicationMap = createApplicationMap(controllerService.getApplications(controller).getData());
    }

    /**
     * Generate flow maps for all applications in the given controller.
     *
     * @return
     */

    public ControllerFlowMap generateFlowMap() {
        return generateFlowMap(null);
    }

    /**
     * Generates the FlowMap for the given set of applications. If the application list is null,
     * it will generate the flowmap for all applications.
     *
     * @param applicationNames
     * @return
     */
    public ControllerFlowMap generateFlowMap(Set<String> applicationNames) {
        ControllerFlowMap controllerFlowMap = new ControllerFlowMap(clone(controller));
        if (applicationMap != null && !applicationMap.isEmpty()) {
            Collection<Application> filtered = filterApplications(applicationNames);
            if (filtered != null && !filtered.isEmpty()) {
                generateApplicationFlowMap(filtered, controllerFlowMap);
            } else {
                logger.error("The application filter didnt return any applications. Make sure that the controller has applications " +
                        "or the applications names in the filter are correct");
            }

        }
        return controllerFlowMap;
    }

    private void generateApplicationFlowMap(Collection<Application> applications, final ControllerFlowMap controllerFlowMap) {
        if (executorService != null) {
            List<Future<ApplicationFlowMap>> futures = new ArrayList<>();
            for (final Application app : applications) {
                final HashMap<String, Application> map = new HashMap<>(applicationMap);
                Future<ApplicationFlowMap> future = executorService.submit(new Callable<ApplicationFlowMap>() {
                    @Override
                    public ApplicationFlowMap call() throws Exception {
                        logger.info("Generating the flowmap for the controller [{}] application [{}]"
                                , controller.getUrl(), app.getName());
                        ApplicationFlowMapBuilder builder = new ApplicationFlowMapBuilder(controllerService,
                                request, map, app, executorService);
                        return builder.build();
                    }
                });
                futures.add(future);
            }
            for (Future<ApplicationFlowMap> future : futures) {
                try {
                    ApplicationFlowMap appFlowMap = future.get();
                    controllerFlowMap.add(appFlowMap);
                } catch (Exception e) {
                    logger.error("Exception while building the application flowmap", e);
                }
            }
        } else {
            for (Application app : applications) {
                final HashMap<String, Application> map = new HashMap<>(applicationMap);
                logger.info("Generating the flowmap for the controller [{}] application [{}]"
                        , controller.getUrl(), app.getName());
                ApplicationFlowMapBuilder builder = new ApplicationFlowMapBuilder(controllerService,
                        request, map, app);
                controllerFlowMap.add(builder.build());
            }
        }
    }

    protected Controller clone(Controller controller) {
        Controller clone = new Controller();
        clone.setUrl(controller.getUrl());
        clone.setAccountName(controller.getAccountName());
        clone.setUserName(controller.getUserName());
        return clone;
    }

    /**
     * Lookup the applications based on the names given in the request
     *
     * @param applicationNames
     * @return
     */
    protected Collection<Application> filterApplications(final Set<String> applicationNames) {
        if (applicationNames == null || applicationNames.isEmpty()) {
            return applicationMap.values();
        } else {
            List<Application> filter = Filters.filter(applicationMap.values(), new Filters.Filter<Application>() {
                @Override
                public boolean filter(Application item) {
                    return applicationNames.contains(item.getName());
                }
            });
            if (filter != null) {
                return filter;
            } else {
                return null;
            }
        }
    }

    private void buildNodeFlowMap(FlowMapRequest request) {

    }

    private void buildBTFlowMap(FlowMapRequest request) {

    }

    private void buildTierFlowMap(FlowMapRequest request) {

    }

    private Map<String, Application> createApplicationMap(Application[] applications) {
        return Mapper.toMap(applications, new Mapper.KeyGetter<String, Application>() {
            @Override
            public String getKey(Application value) {
                return value.getName();
            }
        });
    }

}
