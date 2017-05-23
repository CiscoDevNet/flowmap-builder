package com.appdynamics.tools.flowmap.dto;
/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMapEdge {
    private FlowMapNode srcFlowMapNode;
    private FlowMapNode destFlowMapNode;

    public FlowMapEdge(FlowMapNode srcFlowMapNode, FlowMapNode destFlowMapNode) {
        this.srcFlowMapNode = srcFlowMapNode;
        this.destFlowMapNode = destFlowMapNode;
    }

    public FlowMapNode getSrcFlowMapNode() {
        return srcFlowMapNode;
    }

    public void setSrcFlowMapNode(FlowMapNode srcFlowMapNode) {
        this.srcFlowMapNode = srcFlowMapNode;
    }

    public FlowMapNode getDestFlowMapNode() {
        return destFlowMapNode;
    }

    public void setDestFlowMapNode(FlowMapNode destFlowMapNode) {
        this.destFlowMapNode = destFlowMapNode;
    }


}
