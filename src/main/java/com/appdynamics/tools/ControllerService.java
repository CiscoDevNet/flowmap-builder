package com.appdynamics.tools;

import com.appdynamics.controller.dto.*;
import com.appdynamics.tools.http.ControllerRestClient;
import com.appdynamics.tools.http.UrlBuilder;
import com.appdynamics.tools.util.HttpResponseFuture;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;

/**
 * Created by abey.tom on 10/19/16.
 */
public class ControllerService implements AutoCloseable {
    public static final Logger logger = LoggerFactory.getLogger(ControllerService.class);

    private URLCodec codec = new URLCodec("UTF-8");
    private ControllerRestClient restClient;

    public ControllerService(CloseableHttpAsyncClient httpClient) {
        this(httpClient, null);
    }

    public ControllerService(CloseableHttpAsyncClient httpClient,
                             ControllerRestClient.PasswordDecrypter decrypter) {
        this.restClient = new ControllerRestClient(httpClient, decrypter);
    }

    public HttpResponseFuture<Application[]> getApplications(Controller controller) {
        String url = new UrlBuilder().uri(controller.getUrl()).path("controller/rest/applications")
                .query("output", "JSON").build();
        return restClient.get(url, Application[].class, controller);
    }

    public HttpResponseFuture<Tier[]> getTiers(Controller controller, Application application) {
        String url = new UrlBuilder().uri(controller.getUrl())
                .path("controller/rest/applications")
                .path(String.valueOf(application.getId()))
                .path("tiers")
                .query("output", "JSON").build();
        return restClient.get(url, Tier[].class, controller);

    }

    public HttpResponseFuture<MetricNode[]> getExternalCalls(Controller controller, Application application, Tier tier) {
        String metricPath = "Overall Application Performance|${TIER}|External Calls".replace("${TIER}", tier.getName());
        return getMetricNodes(controller, application, metricPath);
    }

    private HttpResponseFuture<MetricNode[]> getMetricNodes(Controller controller, Application application, String metricPath) {
        String url = new UrlBuilder().uri(controller.getUrl())
                .path("controller/rest/applications")
                .path(String.valueOf(application.getId()))
                .path("metrics")
                .query("metric-path", metricPath)
                .query("output", "JSON").build();
        return restClient.get(url, MetricNode[].class, controller);
    }

    public HttpResponseFuture<Backend[]> getBackends(Controller controller, Application application) {
        String url = new UrlBuilder().uri(controller.getUrl())
                .path("controller/rest/applications")
                .path(String.valueOf(application.getId()))
                .path("backends")
                .query("output", "JSON").build();
        return restClient.get(url, Backend[].class, controller);
    }

    public HttpResponseFuture<Node[]> getApplicationNodes(Controller controller, Application application) {
        String url = new UrlBuilder().uri(controller.getUrl())
                .path("controller/rest/applications")
                .path(String.valueOf(application.getId()))
                .path("nodes")
                .query("output", "JSON").build();
        return restClient.get(url, Node[].class, controller);
    }

    public void close() {
        if (restClient != null) {
            restClient.close();
        }
    }
}
