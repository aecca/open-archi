package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two Gantt activities.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "GanttRelationship")
public class Relationship extends com.araguacaima.open_archi.persistence.diagrams.core.Relationship {
    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    public Relationship() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public void override(Relationship source) {
        super.override(source);
        this.type = source.getType();
    }

    public void copyNonEmpty(Relationship source) {
        super.copyNonEmpty(source);
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }
}