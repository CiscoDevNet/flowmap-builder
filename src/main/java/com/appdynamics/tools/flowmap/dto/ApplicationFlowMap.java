package com.appdynamics.tools.flowmap.dto;


import com.appdynamics.controller.dto.Application;
import com.appdynamics.controller.dto.Tier;

import java.util.Set;

/**
 * Created by abey.tom on 2/1/17.
 */
public class ApplicationFlowMap {
    private Application application;
    private Tier[] tiers;
    protected Set<FlowMapNode> flowMapNodes;
    protected Set<FlowMapEdge> flowMapEdges;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Tier[] getTiers() {
        return tiers;
    }

    public void setTiers(Tier[] tiers) {
        this.tiers = tiers;
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

    public static ApplicationFlowMap from(FlowMap flowMap) {
        ApplicationFlowMap appFlow = new ApplicationFlowMap();
        appFlow.application = flowMap.getApplication();
        appFlow.tiers = flowMap.getTiers();
        appFlow.flowMapEdges = flowMap.getFlowMapEdges();
        appFlow.flowMapNodes = flowMap.getFlowMapNodes();
        return appFlow;

    }
}
