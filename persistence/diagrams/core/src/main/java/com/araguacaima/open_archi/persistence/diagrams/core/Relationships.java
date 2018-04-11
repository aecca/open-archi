package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * A relationship between two elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Relationships extends Taggable {

    @Column
    private String description;
    @Column
    private String sourceId;
    @Column
    private String destinationId;
    @Column
    private String sourcePort;
    @Column
    private String destinationPort;

    public Relationships() {
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public void override(Relationships source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.sourceId = source.getSourceId();
        this.destinationId = source.getDestinationId();
        this.description = source.getDescription();
        this.sourcePort = source.getSourcePort();
        this.destinationPort = source.getDestinationPort();
    }

    public void copyNonEmpty(Relationships source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getSourceId() != null) {
            this.sourceId = source.getSourceId();
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