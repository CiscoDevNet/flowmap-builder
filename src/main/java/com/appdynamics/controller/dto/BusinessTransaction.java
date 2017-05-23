package com.appdynamics.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessTransaction extends AnEntity {

    private String entryPointType;
    private String internalName;
    private String tierId;
    private String tierName;
    private String background;
    private long applicationId;
    private long controllerId;

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getControllerId() {
        return controllerId;
    }

    public void setControllerId(long controllerId) {
        this.controllerId = controllerId;
    }

    public String getEntryPointType() {
        return entryPointType;
    }

    public void setEntryPointType(String entryPointType) {
        this.entryPointType = entryPointType;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getTierId() {
        return tierId;
    }

    public void setTierId(String tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}