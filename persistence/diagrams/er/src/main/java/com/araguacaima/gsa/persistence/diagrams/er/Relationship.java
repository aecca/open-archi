package com.araguacaima.gsa.persistence.diagrams.er;

import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * A relationship between two entities.
 */

@javax.persistence.Entity
@PersistenceUnit(unitName = "gsa")
@DiscriminatorValue("ErRelationship")
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship {
    @Column
    private RelationshipType type;
    @Column
    private String sourceText;
    @Column
    private String destinationText;

    public Relationship() {
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getDestinationText() {
        return destinationText;
    }

    public void setDestinationText(String destinationText) {
        this.destinationText = destinationText;
    }
}