package com.araguacaima.open_archi.persistence.diagrams.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * A relationship between two elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Relationship extends Taggable {

    @OneToOne(targetEntity = Taggable.class, cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JsonIgnore
    private Taggable source;
    @Column
    private String sourceId;
    @OneToOne(targetEntity = Taggable.class, cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JsonIgnore
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

    public void override(Relationship source) {
        super.override(source);
        this.source = source.getSource();
        this.sourceId = source.getSourceId();
        this.destination = source.getDestination();
        this.destinationId = source.getDestinationId();
        this.description = source.getDescription();
        this.sourcePort = source.getSourcePort();
        this.destinationPort = source.getDestinationPort();
    }

    public void copyNonEmpty(Relationship source) {
        super.copyNonEmpty(source);
        if (source.getSource() != null) {
            this.source = source.getSource();
        }
        if (source.getSourceId() != null) {
            this.sourceId = source.getSourceId();
        }
        if (source.getDestination() != null) {
            this.destination = source.getDestination();
        }
        if (source.getDestinationId() != null) {
            this.destinationId = source.getDestinationId();
        }
        if (source.getDescription() != null) {
            this.description = source.getDescription();
        }
        if (source.getSourcePort() != null) {
            this.sourcePort = source.getSourcePort();
        }
        if (source.getDestinationPort() != null) {
            this.destinationPort = source.getDestinationPort();
        }
    }

}