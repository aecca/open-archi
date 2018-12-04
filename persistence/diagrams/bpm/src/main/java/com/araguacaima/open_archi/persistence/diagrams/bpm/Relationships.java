package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A relationship between two classes.
 */

public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {

    private RelationshipType type;

    public Relationships() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public Collection<BaseEntity> override(Relationships source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.type = source.getType();
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Relationships source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getType() != null) {
            this.type = source.getType();
        }
        return overriden;
    }
}