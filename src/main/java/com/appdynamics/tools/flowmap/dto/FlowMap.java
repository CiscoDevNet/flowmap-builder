package com.appdynamics.tools.flowmap.dto;


import com.appdynamics.controller.dto.Application;
import com.appdynamics.controller.dto.Controller;
import com.appdynamics.controller.dto.Tier;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMap {
    protected Controller controller;
    protected Application application;
    private Application[] applications;
    private Tier[] tiers;
    protected Set<FlowMapNode> flowMapNodes;
    protected Set<FlowMapEdge> flowMapEdges;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Set<FlowMapNode> getFlowMapNodes() {
        return flowMapNodes;
    }

    public void setFlowMapNodes(Set<FlowMapNode> flowMapNodes) {
        this.flowMapNodes = flowMapNodes;
    }

    public Set<FlowMapEdge> getFlowMapEdges() {
        return flowMapEdges;
    }

    public void setFlowMapEdges(Set<FlowMapEdge> flowMapEdges) {
        this.flowMapEdges = flowMapEdges;
    }

    public Tier[] getTiers() {
        return tiers;
    }

    public void setTiers(Tier[] tiers) {
        this.tiers = tiers;
    }

    public Application[] getApplications() {
        return applications;
    }

    public void setApplications(Application[] applications) {
        this.applications = applications;
    }

    public void addNode(FlowMapNode node) {
        if(flowMapNodes==null){
            flowMapNodes = new HashSet<>();
        }
        flowMapNodes.add(node);
    }

    public void addEdge(FlowMapEdge flowMapEdge){
        if(flowMapEdges ==null){
            flowMapEdges = new HashSet<>();
        }
        flowMapEdges.add(flowMapEdge);

    }
}
