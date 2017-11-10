package com.araguacaima.gsa.persistence.diagrams.flowchart;

import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two classes.
 */
@Entity
@PersistenceUnit(unitName = "gsa")
@DiscriminatorValue("FlowchartRelationship")
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