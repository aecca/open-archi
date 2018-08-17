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

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private CompositeElement source;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private CompositeElement destination;

    @Column
    private String description;

    @Column
    private String sourcePort;

    @Column
    private String destinationPort;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Connector connector;

    public Relationship() {
    }

    public CompositeElement getSource() {
        return source;
    }

    public void setSource(CompositeElement source) {
        this.source = source;
    }

    public CompositeElement getDestination() {
        return destination;
    }

    public void setDestination(CompositeElement destination) {
        this.destination = destination;
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

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public void override(Relationship source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.source = source.getSource();
        this.destination = source.getDestination();
        this.description = source.getDescription();
        this.sourcePort = source.getSourcePort();
        this.destinationPort = source.getDestinationPort();
        this.connector = source.getConnector();
    }

    public void copyNonEmpty(Relationship source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getSource() != null) {
            this.source = source.getSource();
        }
        if (source.getDestination() != null) {
            this.destination = source.getDestination();
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
        if (source.getConnector() != null) {
            this.connector = source.getConnector();
        }
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setGroup(boolean container) {

    }
}