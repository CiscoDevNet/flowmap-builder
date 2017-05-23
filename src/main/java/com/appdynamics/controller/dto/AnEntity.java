package com.appdynamics.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by abey.tom on 8/23/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnEntity {
    protected long id;
    protected String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnEntity)) return false;

        AnEntity anEntity = (AnEntity) o;

        return id == anEntity.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AnEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
