package com.araguacaima.gsa.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.UUID;


public class Role implements Serializable, Comparable {

    private static final long serialVersionUID = 3104672099472914869L;

    protected String id;

    private String name;

    private int priority;

    public Role() {
        this.id = UUID.randomUUID().toString();
    }

    public Role(String id, String name, int priority) {
        this();
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public Role(String name, int priority) {
        this();
        this.name = name;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int oreder) {
        this.priority = oreder;
    }

    @Override
    public int compareTo(Object o) {
        Role role = (Role) o;
        return this.getPriority() - role.getPriority();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return new EqualsBuilder()
                .append(priority, role.priority)
                .append(id, role.id)
                .append(name, role.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(priority)
                .toHashCode();
    }
}
