package com.appdynamics.tools.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by abey.tom on 5/23/17.
 */
public class HttpResponseFuture<T> {
    public static final Logger logger = LoggerFactory.getLogger(HttpResponseFuture.class);
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String url;
    private Future<HttpResponse> future;
    private Class<T> clazz;


    public HttpResponseFuture(String url, Future<HttpResponse> future, Class<T> clazz) {
        this.url = url;
        this.future = future;
        this.clazz = clazz;
    }

    public T getData() {
        try {
            HttpResponse response = future.get();
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                return getContent(entity, url, clazz);
            } else {
                printError(response, url);
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception while invoking the url" + url, e);
            return null;
        }

    }

    private <T> T getContent(HttpEntity entity, String url, Class<T> clazz) throws IOException {
        if (logger.isDebugEnabled()) {
            String json = EntityUtils.toString(entity);
            logger.debug("The response of url [{}] is {}", url, json);
            return objectMapper.readValue(json, clazz);
        } else {
            return objectMapper.readValue(entity.getContent(), clazz);
        }
    }

    private static void printError(HttpResponse response, String url) {
        if (response != null) {
            logger.error("The status line for the url [{}] is [{}] and the headers are [{}]"
                    , url, response.getStatusLine(), response.getAllHeaders());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    logger.error("The contents are {}", EntityUtils.toString(response.getEntity()));
                } catch (Exception e) {
                    logger.error("", e);
                }
            } else {
                logger.error("The response content is null");
            }
        } else {
            logger.error("The response is null for the URL {}", url);
        }
    }
}
