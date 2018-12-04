package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

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

    public void override(Relationships source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.type = source.getType();
    }

    public void copyNonEmpty(Relationships source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }
}