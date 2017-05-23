package com.appdynamics.tools.flowmap.dto;

import com.appdynamics.controller.dto.EntityType;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;

/**
 * Created by abey.tom on 10/19/16.
 */
public class FlowMapEntity {
    private Long entityId;
    private String entityName;
    private EntityType entityType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void validate() {
        Asserts.check(entityId != null || !StringUtils.isEmpty(entityName),
                "The entityName of entity Id should be set in FlowMapEntity");
        Asserts.check(entityType != null, "The entity Type should be set");
    }

}
