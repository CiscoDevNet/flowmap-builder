package com.appdynamics.controller.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abey.tom on 5/3/16.
 */
public class ExternalCall {
    public static final Logger logger = LoggerFactory.getLogger(ExternalCall.class);

    public static final Pattern TIER_CALL_REGEX = Pattern.compile("Call-(\\S+) to (.+)");
    public static final Pattern BACKEND_CALL_REGEX = Pattern.compile("Call-(\\S+) to Discovered backend call - (.+)");
    public static final Pattern APP_EXT_CALL_REGEX = Pattern.compile("Call-(\\S+) to External Application - (.+)");

    public enum EntityType {
        APPLICATION, TIER, BACKEND
    }

    private Long entityId;
    private String entityName;
    private EntityType entityType;
    private String exitPointType;
    private Backend backend;

    public ExternalCall() {
    }

    public ExternalCall(String entityName, String exitPointType, EntityType entityType) {
        this.entityType = entityType;
        this.exitPointType = exitPointType;
        this.entityName = entityName;
    }

    public String getExitPointType() {
        return exitPointType;
    }

    public void setExitPointType(String exitPointType) {
        this.exitPointType = exitPointType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityEntityType) {
        this.entityType = entityEntityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Backend getBackend() {
        return backend;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public static List<ExternalCall> from(MetricNode[] metricNodes) {
        List<ExternalCall> calls = new ArrayList<>();
        if (metricNodes != null) {
            for (MetricNode metricNode : metricNodes) {
                if (metricNode.getType().equals("folder")) {
                    ExternalCall externalCall = ExternalCall.from(metricNode.getName());
                    if (externalCall != null) {
                        calls.add(externalCall);
                    } else {
                        logger.error("Cannot convert the metric tree node to a external call {}", metricNode.getName());
                    }
                }
            }
        }
        return calls;
    }

    public static ExternalCall from(String str) {
        Matcher matcher = APP_EXT_CALL_REGEX.matcher(str);
        if (matcher.matches()) {
            String callType = matcher.group(1);
            String name = matcher.group(2);
            return new ExternalCall(name, callType, EntityType.APPLICATION);
        }

        matcher = BACKEND_CALL_REGEX.matcher(str);
        if (matcher.matches()) {
            String callType = matcher.group(1);
            String name = matcher.group(2);
            return new ExternalCall(name, callType, EntityType.BACKEND);
        }

        matcher = TIER_CALL_REGEX.matcher(str);
        if (matcher.matches()) {
            String callType = matcher.group(1);
            String name = matcher.group(2);
            return new ExternalCall(name, callType, EntityType.TIER);
        }
        return null;
    }

    public String getApplicationName() {
        if (entityType == EntityType.APPLICATION) {
            return entityName;
        } else {
            return null;
        }
    }

    public String getTierName() {
        if (entityType == EntityType.TIER) {
            return entityName;
        } else {
            return null;
        }
    }

    public String getBackendName() {
        if (entityType == EntityType.BACKEND) {
            return entityName;
        } else {
            return null;
        }
    }
}
