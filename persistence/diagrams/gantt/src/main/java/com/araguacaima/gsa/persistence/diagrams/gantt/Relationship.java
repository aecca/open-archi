package com.araguacaima.gsa.persistence.diagrams.gantt;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;
import java.util.Set;

/**
 * A relationship between two Gantt activities.
 */

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Gantt_Relationship", schema = "DIAGRAMS")
@DiscriminatorValue(value = "GanttRelationship")
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship {
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