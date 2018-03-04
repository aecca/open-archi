package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two classes.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("BpmRelationship")
public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    public Relationships() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public void override(Relationships source) {
        super.override(source);
        this.type = source.getType();
    }

    public void copyNonEmpty(Relationships source) {
        super.copyNonEmpty(source);
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }
}