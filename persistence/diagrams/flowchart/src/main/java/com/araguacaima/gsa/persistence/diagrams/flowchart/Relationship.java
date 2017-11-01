package com.araguacaima.gsa.persistence.diagrams.flowchart;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.util.Set;

/**
 * A relationship between two classes.
 */
@Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "Flowchart_Relationship", schema = "DIAGRAMS")
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship<Flowchart> {
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