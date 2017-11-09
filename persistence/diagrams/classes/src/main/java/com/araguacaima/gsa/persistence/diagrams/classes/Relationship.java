package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two classes.
 */
@Entity
@PersistenceUnit(unitName = "gsa" )
@DiscriminatorValue("ClassesRelationship")
@Table(name = "Classes_Relationship", schema = "DIAGRAMS")
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