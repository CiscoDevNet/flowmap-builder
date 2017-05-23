package com.appdynamics.controller.dto;

import java.util.List;

/**
 * Created by abey.tom on 8/23/15.
 */
public class Application extends AnEntity {
    private List<Tier> tiers;
    private List<Node> nodes;

    public List<Tier> getTiers() {
        return tiers;
    }

    public void setTiers(List<Tier> tiers) {
        if (tiers != null) {
            for (Tier tier : tiers) {
                tier.setApplicationId(getId());
            }
        }
        this.tiers = tiers;
    }

    public void setNodes(List<Node> nodes) {
        if (nodes != null) {
            for (Node node : nodes) {
                node.setApplicationId(getId());
            }
        }
        this.nodes = nodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
