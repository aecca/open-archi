package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two Gantt activities.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "SequenceRelationships")
public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    @Column
    private int time;

    public Relationships() {
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

    public void override(Relationships source, boolean keepMeta) {
        super.override(source, keepMeta, suffix);
        this.type = source.getType();
        this.time = source.getTime();
    }

    public void copyNonEmpty(Relationships source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getType() != null) {
            this.type = source.getType();
        }
        if (source.getTime() != 0) {
            this.time = source.getTime();
        }
    }
}