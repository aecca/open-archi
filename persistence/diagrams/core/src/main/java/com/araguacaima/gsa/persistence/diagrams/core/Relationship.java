package com.araguacaima.gsa.persistence.diagrams.core;

import javax.persistence.Column;

/**
 * A relationship between two elements.
 */
public abstract class Relationship<T extends Item> extends Taggable {

    private T source;
    @Column
    private String sourceId;
    private T destination;
    @Column
    private String destinationId;
    @Column
    private String description;
    @Column
    private String sourcePort;
    @Column
    private String destinationPort;

    public Relationship() {
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public T getDestination() {
        return destination;
    }

    public void setDestination(T destination) {
        this.destination = destination;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}