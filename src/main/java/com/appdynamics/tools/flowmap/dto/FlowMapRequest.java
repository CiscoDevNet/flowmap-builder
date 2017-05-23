package com.appdynamics.tools.flowmap.dto;

import com.appdynamics.controller.dto.Controller;
import org.apache.http.util.Asserts;

/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMapRequest {
    private Controller controller;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void validate() {
        Asserts.check(controller != null, "Controller cannot be null");
        controller.validate();
    }
}
