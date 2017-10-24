package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.common.Element;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

import static com.araguacaima.gsa.model.diagrams.architectural.StaticStructureElement.CANONICAL_NAME_SEPARATOR;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
public final class ContainerInstance extends Element {

    private Container container;
    private String containerId;
    private int instanceId;

    ContainerInstance() {
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
    protected Set<Tag> getRequiredTags() {
        return build(Tag.CONTAINER_INSTANCE);
    }

    @Override
    public String getTags() {
        return container.getTags() + "," + super.getTags();
    }

    @Override
    @JsonIgnore
    public String getCanonicalName() {
        return container.getCanonicalName() + "[" + instanceId + "]";
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return container.getParent();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        // no-op ... the name of a container instance is taken from the associated Container
    }

    /**
     * Adds a relationship between this container instance and another.
     *
     * @param destination the destination of the relationship (a ContainerInstance)
     * @param description a description of the relationship
     * @param technology  the technology of the relationship
     * @return a Relationship object
     */
    public Relationship uses(ContainerInstance destination, String description, String technology) {
        if (destination != null) {
            return getModel().addRelationship(this, destination, description, technology);
        } else {
            throw new IllegalArgumentException("The destination of a relationship must be specified.");
        }
    }
    @Override
    public String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }
}