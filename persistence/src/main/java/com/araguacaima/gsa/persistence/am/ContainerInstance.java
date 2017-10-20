package com.araguacaima.gsa.persistence.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "ContainerInstance", schema = "AM")
public class ContainerInstance extends Element {

    @OneToOne
    private Container container;

    @Column
    private String containerId;

    @Column
    private int instanceId;

    public ContainerInstance() {
    }

    ContainerInstance(Container container, int instanceId) {
        this.container = container;
        this.instanceId = instanceId;
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Gets the ID of the container that this object represents a deployment instance of.
     *
     * @return the container ID, as a String
     */
    public String getContainerId() {
        if (container != null) {
            return container.getId();
        } else {
            return containerId;
        }
    }

    void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * Gets the instance ID of this container.
     *
     * @return the instance ID, an integer greater than zero
     */
    public int getInstanceId() {
        return instanceId;
    }

    void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return container.getParent();
    }



}