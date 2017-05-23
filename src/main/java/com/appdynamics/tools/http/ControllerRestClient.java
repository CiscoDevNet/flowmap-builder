package com.appdynamics.tools.http;


import com.appdynamics.controller.dto.Controller;
import com.appdynamics.tools.util.HttpResponseFuture;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

/**
 * Created by abey.tom on 2/2/15.
 */
public class ControllerRestClient implements AutoCloseable {
    public static final Logger logger = LoggerFactory.getLogger(ControllerRestClient.class);
    protected URLCodec codec = new URLCodec("UTF-8");
    protected final CloseableHttpAsyncClient httpClient;

    protected PasswordDecrypter decrypter;

    public ControllerRestClient(CloseableHttpAsyncClient httpClient, PasswordDecrypter decrypter) {
        this.httpClient = httpClient;
        this.decrypter = decrypter;
        httpClient.start();
    }

    public <T> HttpResponseFuture<T> get(String url, Class<T> clazz, Controller controller) {
        logger.debug("Invoking GET on the URL {}", url);
        HttpGet get = new HttpGet(url);
        setAuthentication(get, controller);
        Future<HttpResponse> future = httpClient.execute(get, null);
        return new HttpResponseFuture<T>(url, future, clazz);
    }

    private String generateAuthValue(Controller controller) {
        StringBuilder sb = new StringBuilder();
        sb.append(controller.getUserName()).append("@")
                .append(controller.getAccountName()).append(":");
        if (!StringUtils.isEmpty(controller.getPassword())) {
            sb.append(encode(controller.getPassword()));
        } else if (!StringUtils.isEmpty(controller.getPasswordEncrypted())) {
            if (decrypter != null) {
                sb.append(encode(decrypter.decrypt(controller.getPasswordEncrypted())));
            } else {
                throw new RuntimeException("The decrypter shpuld be set in ControllerRestClient when passwordEncrypted is used");
            }
        } else {
            throw new RuntimeException("The password is not set for the controller " + controller.getUrl());
        }
        return "Basic " + new String(Base64.encodeBase64(sb.toString().getBytes(Charset.forName("UTF-8"))));
    }

    protected void setAuthentication(HttpRequestBase get, Controller controller) {
        String authHeaderValue = generateAuthValue(controller);
        get.addHeader(HttpHeaders.AUTHORIZATION, authHeaderValue);
    }

    private String encode(String val) {
        try {
            return codec.encode(val);
        } catch (EncoderException e) {
            logger.error("Error while encoding the value [cant log might be a password]", e);
        }
        return val;
    }

    public void close() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("Error while closing the http client", e);
            }
        }
    }

    public interface PasswordDecrypter {
        String decrypt(String encrypted);
    }
}
