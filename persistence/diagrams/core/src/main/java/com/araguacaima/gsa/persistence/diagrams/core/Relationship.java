package com.araguacaima.gsa.persistence.diagrams.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * A relationship between two elements.
 */
public abstract class Relationship <T extends Item> extends Taggable {

    private T source;
    private String sourceId;
    private T destination;
    private String destinationId;
    private String description;
    private String sourcePort;
    private String destinationPort;

    public Relationship() {
    }

    public Relationship(T source, T destination, String description) {
        this();

        this.source = source;
        this.destination = destination;
        this.description = description;
    }

    @JsonIgnore
    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    /**
     * Gets the ID of the source element.
     *
     * @return the ID of the source element, as a String
     */
    public String getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * Gets the ID of this relationship in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public T getDestination() {
        return destination;
    }

    public void setDestination(T destination) {
        this.destination = destination;
    }

    /**
     * Gets the ID of the destination element.
     *
     * @return the ID of the destination element, as a String
     */
    public String getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract RelationshipType getType();

    public abstract void setType(RelationshipType type);

    @Override
    protected Set<String> getRequiredTags() {
        return build(Constants.RELATIONSHIP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relationship that = (Relationship) o;

        if (!getDescription().equals(that.getDescription())) return false;
        if (!getDestination().equals(that.getDestination())) return false;
        if (!getSource().equals(that.getSource())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getSourceId().hashCode();
        result = 31 * result + getDestinationId().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return source.toString() + " ---[" + description + "]---> " + destination.toString();
    }

}