package com.appdynamics.tools.http;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: abey.tom
 * Date: 4/4/14
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class UrlBuilder {
    public static final Logger logger = LoggerFactory.getLogger(UrlBuilder.class);
    private List<String> paths;
    private Map<String, String> taskArgs;
    private Map<String, String> queryParams;
    private boolean encodeQueryParam = true;

    public UrlBuilder() {
        taskArgs = new HashMap<String, String>();
    }

    public UrlBuilder(boolean encodeQueryParam) {
        this.encodeQueryParam = encodeQueryParam;
        taskArgs = new HashMap<String, String>();
    }

    public UrlBuilder(Map<String, String> taskArgs) {
        if (taskArgs == null) {
            throw new IllegalArgumentException("The task arguments to the URLBuilder cannot be null");
        }
        this.taskArgs = new HashMap<String, String>(taskArgs);
    }

    public static UrlBuilder builder() {
        return new UrlBuilder(true);
    }

    public static UrlBuilder builder(boolean encodeQueryParam) {
        return new UrlBuilder(encodeQueryParam);
    }

    public static UrlBuilder builder(Map<String, String> taskArgs) {
        return new UrlBuilder(taskArgs);
    }


    public UrlBuilder ssl(boolean useSSL) {
        taskArgs.put("useSSL", String.valueOf(useSSL));
        return this;
    }

    public UrlBuilder host(String host) {
        taskArgs.put("host", host);
        return this;
    }

    public UrlBuilder port(String port) {
        taskArgs.put("port", port);
        return this;
    }

    public UrlBuilder port(int port) {
        taskArgs.put("port", String.valueOf(port));
        return this;
    }

    public UrlBuilder uri(String uri) {
        taskArgs.put("uri", uri);
        return this;
    }

    public UrlBuilder encodeQueryParam(boolean encodeQueryParam) {
        this.encodeQueryParam = encodeQueryParam;
        return this;
    }

    public UrlBuilder path(String path) {
        if (!StringUtils.isEmpty(path)) {
            path = path.trim();
            path = trimLeadingSlash(path);
            path = trimTrailingSlash(path);
            if (paths == null) {
                paths = new ArrayList<String>();
            }
            paths.add(path);
        }
        return this;
    }

    private String trimLeadingSlash(String path) {
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    private String trimTrailingSlash(String path) {
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public UrlBuilder query(String name, String value) {
        if (!StringUtils.isEmpty(name)) {
            name = name.trim();
            if (!!StringUtils.isEmpty(value)) {
                value = "";
            }
            if (queryParams == null) {
                queryParams = new LinkedHashMap<String, String>();
            }
            queryParams.put(name, value);
        }
        return this;
    }

    public UrlBuilder query(String name) {
        return query(name, null);
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        String uri = taskArgs.get("uri");
        if (!StringUtils.isEmpty(uri)) {
            sb.append(trimTrailingSlash(uri.trim()));
        } else {
            String useSSL = defaultIfEmpty(taskArgs, "useSSL", "false");
            if (Boolean.valueOf(useSSL)) {
                sb.append("https://");
            } else {
                sb.append("http://");
            }
            String host = defaultIfEmpty(taskArgs, "host", "localhost");
            String port = defaultIfEmpty(taskArgs, "port", "80");
            sb.append(host).append(":").append(port);
        }

        if (paths != null && !paths.isEmpty()) {
            for (String path : paths) {
                sb.append("/").append(path);
            }
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            sb.append("?");
            Set<String> keys = queryParams.keySet();
            for (String key : keys) {
                sb.append(key);
                String value = queryParams.get(key);
                if (!value.isEmpty()) {
                    if (encodeQueryParam) {
                        sb.append("=").append(encode(value));
                    } else {
                        sb.append("=").append(value);
                    }
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("The url is initialized to {}", sb);
        }
        return sb.toString();
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Excepting while encoding the value " + value, e);
            return value;
        }
    }

    public static String defaultIfEmpty(Map<String, String> taskArgs, String key, String defaultValue) {
        if (taskArgs.containsKey(key)) {
            String value = taskArgs.get(key);
            if (!StringUtils.isEmpty(value)) {
                return value;
            } else {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
}
