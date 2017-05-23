package com.appdynamics.tools;

import com.appdynamics.controller.dto.Controller;
import org.junit.Test;

/**
 * Created by abey.tom on 5/2/17.
 */
public class FlowMapMainTest {
    @Test
    public void main() throws Exception {
        Controller[] controllers = FlowMapGeneratorTest.getControllersFromFile();
        Controller controller = controllers[0];
        FlowMapMain.main(new String[]{
                "-c", controller.getUrl(), "-a", controller.getAccountName(), "-u", controller.getUserName()
                , "-p", controller.getPassword(), "-apps", "ECommerce"
        });
    }

}