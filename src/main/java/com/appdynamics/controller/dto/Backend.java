package com.appdynamics.controller.dto;

import java.util.Arrays;

/**
 * Created by abey.tom on 5/9/16.
 */
public class Backend extends AnEntity {
    private long applicationComponentNodeId;
    private String exitPointType;
    private BackendProperty[] properties;

    public long getApplicationComponentNodeId() {
        return applicationComponentNodeId;
    }

    public void setApplicationComponentNodeId(long applicationComponentNodeId) {
        this.applicationComponentNodeId = applicationComponentNodeId;
    }

    public String getExitPointType() {
        return exitPointType;
    }

    public void setExitPointType(String exitPointType) {
        this.exitPointType = exitPointType;
    }

    public BackendProperty[] getProperties() {
        return properties;
    }

    public void setProperties(BackendProperty[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Backend{" +
                "applicationComponentNodeId=" + applicationComponentNodeId +
                ", exitPointType='" + exitPointType + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
