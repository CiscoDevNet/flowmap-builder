package com.appdynamics.tools;


import com.appdynamics.controller.dto.Controller;
import com.appdynamics.tools.flowmap.dto.ControllerFlowMap;
import com.appdynamics.tools.flowmap.dto.FlowMapRequest;
import com.appdynamics.tools.util.DefaultJarPathResolver;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by abey.tom on 10/26/16.
 */
public class FlowMapMain {
    public static Logger logger;

    public static void main(String[] args) throws ParseException, IOException {
        DefaultJarPathResolver pathResolver = new DefaultJarPathResolver();
        File dir = pathResolver.resolveDirectory(FlowMapMain.class);
        configureLogging(dir);

        logger = LoggerFactory.getLogger(FlowMapMain.class);
        Options options = new Options();
        options.addOption("c", true, "[REQUIRED] AppDynamics Controller URL http://xyz.saas.appdynamics.com");
        options.addOption("a", true, "[REQUIRED] AppDynamics Controller Account Name");
        options.addOption("u", true, "[REQUIRED] AppDynamics Controller User Name");
        options.addOption("p", true, "[REQUIRED] AppDynamics Controller Password");
        options.addOption("l", false, "[OPTIONAL] Use Legacy Flowmap Builder. Use this option to generate it from older api");
        options.addOption("apps", true, "Applications to generate the flowmap for. Ommit for all applications");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        String url = cmd.getOptionValue("c");
        String account = cmd.getOptionValue("a");
        String user = cmd.getOptionValue("u");
        String password = cmd.getOptionValue("p");
        String applications = cmd.getOptionValue("apps");
        validateNotNull(options, url, account, user, password);
        Controller controller = new Controller();
        controller.setUrl(url);
        controller.setAccountName(account);
        controller.setUserName(user);
        controller.setPassword(password);
        FlowMapRequest request = new FlowMapRequest();
        request.setController(controller);
        FlowMapGenerator generator = new FlowMapGenerator(request);
        writeFlowMap(dir, controller, generator,asSet(applications));
    }

    private static void writeFlowMap(File dir, Controller controller, FlowMapGenerator generator, Set<String> applications) throws IOException {
        ControllerFlowMap flowMap = generator.generateFlowMap(applications);
        if (flowMap != null) {
            if (flowMap.getController() != null) {
                flowMap.getController().setPassword(null);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            File resultFile = new File(dir, "flowmap.json");
            logger.info("Writing the output to the file {}", resultFile.getAbsolutePath());
            mapper.writeValue(resultFile, flowMap);
        } else {
            logger.error("Cannot generate flowmap");
        }
    }

    private static void configureLogging(File dir) throws FileNotFoundException {
        System.setProperty("WORK_DIR", dir.getAbsolutePath());
        File file = new File(dir, "log4j.xml");
        if (file.exists()) {
            PropertyConfigurator.configure(new FileInputStream(file));
        } else {
            URL url = FlowMapMain.class.getResource("/log4j.flowmap-builder.xml");
            if (url != null) {
                System.out.println("[INFO] Using the log4j file " + url);
                DOMConfigurator.configure(url);
            } else {
                System.out.println("[ERROR] Cannot find the log4j.xml");
            }
        }
    }

    private static Set<String> asSet(String applications) {
        if (!StringUtils.isEmpty(applications)) {
            String[] split = applications.split(",");
            HashSet<String> apps = new HashSet<>();
            for (String s : split) {
                String trim = s.trim();
                if (!trim.isEmpty()) {
                    apps.add(trim);
                }
            }
            return apps;
        }
        return null;
    }

    private static void validateNotNull(Options options, String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg)) {
                System.out.println("Please ENTER all REQUIRED arguments:");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar flowmap-builder-$version.jar", options);
                System.exit(1);
            }
        }
    }
}
