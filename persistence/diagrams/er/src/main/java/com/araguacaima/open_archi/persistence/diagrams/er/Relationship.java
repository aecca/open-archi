package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two entities.
 */

@javax.persistence.Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("ErRelationship")
public class Relationship extends com.araguacaima.open_archi.persistence.diagrams.core.Relationship {
    @Column
    @Enumerated(EnumType.STRING)
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