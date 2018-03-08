package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two Gantt activities.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "SequenceRelationship")
public class Relationship extends com.araguacaima.open_archi.persistence.diagrams.core.Relationship {

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    @Column
    private int time;

    public Relationship() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void override(Relationship source) {
        super.override(source);
        this.type = source.getType();
        this.time = source.getTime();
    }

    public void copyNonEmpty(Relationship source) {
        super.copyNonEmpty(source);
        if (source.getType() != null) {
            this.type = source.getType();
        }
        if (source.getTime() != 0) {
            this.time = source.getTime();
        }
    }
}