package com.appdynamics.tools.flowmap.dto;

import com.appdynamics.controller.dto.*;

/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMapNode {
    private EntityType entityType;
    private long entityId;
    private String entityName;
    private String exitType;

    public FlowMapNode() {
    }

    public FlowMapNode(long entityId, String entityName, EntityType entityType) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getExitType() {
        return exitType;
    }

    public void setExitType(String exitType) {
        this.exitType = exitType;
    }

    public static FlowMapNode from(Tier tier) {
        return new FlowMapNode(tier.getId(), tier.getName(), EntityType.TIER);
    }

    public static FlowMapNode from(Application application) {
        return new FlowMapNode(application.getId(), application.getName(), EntityType.APPLICATION);
    }

    public static FlowMapNode from(Backend backend) {
        FlowMapNode flowMapNode = new FlowMapNode(backend.getId(), backend.getName(), EntityType.BACKEND);
        flowMapNode.setExitType(backend.getExitPointType());
        return flowMapNode;
    }

    public static FlowMapNode from(AnEntity anEntity) {
        if (anEntity instanceof Application) {
            Application application = (Application) anEntity;
            return from(application);
        } else if (anEntity instanceof Tier) {
            Tier tier = (Tier) anEntity;
            return from(tier);
        } else if (anEntity instanceof Backend) {
            Backend backend = (Backend) anEntity;
            return from(backend);
        }
        return null;
    }

    @Override
    public String toString() {
        if (EntityType.BACKEND == entityType) {
            return "FlowMapNode{" +
                    "entityType=" + entityType +
                    ", entityId=" + entityId +
                    ", entityName='" + entityName + '\'' +
                    ", exitType='" + exitType + '\'' +
                    '}';
        } else {
            return "FlowMapNode{" +
                    "entityType=" + entityType +
                    ", entityId=" + entityId +
                    ", entityName='" + entityName + '\'' +
                    '}';
        }
    }
}
