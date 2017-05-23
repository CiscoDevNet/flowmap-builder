package com.appdynamics.tools.flowmap.dto;

import com.appdynamics.controller.dto.Controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by abey.tom on 2/1/17.
 */
public class ControllerFlowMap {
    private Controller controller;
    private List<ApplicationFlowMap> appFlowMaps = new CopyOnWriteArrayList<>();

    public ControllerFlowMap() {
    }

    public ControllerFlowMap(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public List<ApplicationFlowMap> getAppFlowMaps() {
        return appFlowMaps;
    }

    public void setAppFlowMaps(List<ApplicationFlowMap> appFlowMaps) {
        this.appFlowMaps = appFlowMaps;
    }

    public void add(ApplicationFlowMap applicationFlowMap) {
        if (appFlowMaps == null) {
            appFlowMaps = new CopyOnWriteArrayList<>();
        }
        appFlowMaps.add(applicationFlowMap);

    }
}
