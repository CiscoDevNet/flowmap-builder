## Flowmap Builder
Generates the Application Flow Map in JSON format. This can be used as standalone and as a  library.  

### 1. USAGE - COMMAND LINE

```
 usage: java -jar flowmap-builder-$version.jar
  -a <arg>      [REQUIRED] AppDynamics Controller Account Name
  -apps <arg>   Applications to generate the flowmap for. Ommit for all
                applications
  -c <arg>      [REQUIRED] AppDynamics Controller URL
                http://xyz.saas.appdynamics.com
  -l            [OPTIONAL] Use Legacy Flowmap Builder. Use this option to
                generate it from older api
  -p <arg>      [REQUIRED] AppDynamics Controller Password
  -u <arg>      [REQUIRED] AppDynamics Controller User Name
``` 

##### 1.1 EXAMPLE
````
 java -jar flowmap-builder-<version>.jar \
            -c http://192.168.1.132:8090 \
            -a customer1 -u abey -p welcome \
            -apps "TestApp,TestApp2"

````
##### 1.2 OUTPUT

A file named flowmap.json will be created in the same directory as the jar file. 

##### 1.3 BUILD STANDALONE JAR

Run the following command to create a standalone jar. This will create a fat jar will all the dependencies. 

````
mvn clean install -P standalone
````

### 2. USAGE - LIBRARY
````
 Controller controller = new Controller();
 controller.setUrl();
 controller.setAccountName();
 controller.setUserName();
 controller.setPassword();

 FlowMapRequest request = new FlowMapRequest();
 request.setController(controller);
 
 // #1 Default Configuration
 FlowMapGenerator generator = new FlowMapGenerator(request);
 ControllerFlowMap controllerFlowMap = generator.generateFlowMap();
 generator.close();//shutdown the async http client
 
 //#2 can customize http client if needed
 FlowMapGenerator generator2 = new FlowMapGenerator(request,
                     HttpAsyncClients.createDefault());
 ControllerFlowMap controllerFlowMap2 = generator2.generateFlowMap();
 generator2.close();//shutdown the async http client 
  
````
 
##### 1.3 BUILD LIBRARY JAR
  
````
mvn clean install
````  