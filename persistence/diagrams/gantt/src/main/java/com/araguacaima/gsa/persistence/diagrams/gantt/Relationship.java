package com.araguacaima.gsa.persistence.diagrams.gantt;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.util.Set;

/**
 * A relationship between two Gantt activities.
 */

@Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "Gantt_Relationship", schema = "DIAGRAMS")
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship<Gantt> {
    @Column
    private RelationshipType type;

    public Relationship() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}