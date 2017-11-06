package com.araguacaima.gsa.persistence.diagrams.core;

import javax.persistence.*;

/**
 * A relationship between two elements.
 */
@Entity
@PersistenceUnit(unitName = "gsa" )
public class Relationship extends Taggable {

    @OneToOne(targetEntity=Taggable.class)
    private Taggable source;
    @Column
    private String sourceId;
    @OneToOne(targetEntity=Taggable.class)
    private Taggable destination;
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

    public Taggable getSource() {
        return source;
    }

    public void setSource(Taggable source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Taggable getDestination() {
        return destination;
    }

    public void setDestination(Taggable destination) {
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