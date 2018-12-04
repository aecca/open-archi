package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A relationship between two elements.
 */

public class Relationships extends Taggable {

    private String description;
    private String sourceId;
    private String destinationId;
    private String sourcePort;
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

    public Collection<BaseEntity> override(Relationships source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.sourceId = source.getSourceId();
        this.destinationId = source.getDestinationId();
        this.description = source.getDescription();
        this.sourcePort = source.getSourcePort();
        this.destinationPort = source.getDestinationPort();
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Relationships source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
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
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}