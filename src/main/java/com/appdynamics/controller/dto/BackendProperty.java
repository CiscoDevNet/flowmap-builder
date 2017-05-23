package com.appdynamics.controller.dto;

/**
 * Created by abey.tom on 5/9/16.
 */
public class BackendProperty extends AnEntity{
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BackendProperty{" +
                "value='" + value + '\'' +
                "} " + super.toString();
    }
}
