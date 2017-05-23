package com.appdynamics.tools;

import com.appdynamics.controller.dto.Controller;
import com.appdynamics.tools.flowmap.dto.ControllerFlowMap;
import com.appdynamics.tools.flowmap.dto.FlowMapRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by abey.tom on 5/19/17.
 */
public class FlowMapGeneratorTest {
    /**
     * This expects a file at ${user.home}/flowmapbuiler-controllers.json
     * refer to src/test/resources/controller-template.json as a template for flowmapbuiler-controllers.json
     *
     * @throws Exception
     */
    @Test
    public void getControllerFlowMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Controller[] controllers = getControllersFromFile();
        for (Controller controller : controllers) {
            FlowMapRequest request = new FlowMapRequest();
            request.setController(controller);
            FlowMapGenerator builder = new FlowMapGenerator(request,
                    HttpAsyncClients.createDefault(), Executors.newFixedThreadPool(5));
            ControllerFlowMap controllerFlowMap = builder.generateFlowMap();
            String host = new URL(controller.getUrl()).getHost();
            File outFile = new File("target/output/" + host + ".json");
            outFile.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, controllerFlowMap);
        }
    }

    public static Controller[] getControllersFromFile() throws java.io.IOException {
        File controllersFile = new File(System.getProperty("user.home"), "flowmapbuiler-controllers.json");
        if (!controllersFile.exists()) {
            throw new RuntimeException("Create a file named [${user.home}/flowmapbuiler-controllers.json] with [ src/test/resources/controller-template.json] as template");
        }
        Controller[] controllers = new ObjectMapper().readValue(controllersFile, Controller[].class);
        if (ArrayUtils.isEmpty(controllers)) {
            throw new RuntimeException("The file [${user.home}/flowmapbuiler-controllers.json] is expected to contain atleast one controller definition");
        }
        return controllers;
    }

}