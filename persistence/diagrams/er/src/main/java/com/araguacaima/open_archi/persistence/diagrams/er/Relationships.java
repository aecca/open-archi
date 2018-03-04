package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two entities.
 */

@javax.persistence.Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("ErRelationship")
public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {
    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;
    @Column
    private String sourceText;
    @Column
    private String destinationText;

    public Relationships() {
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

    public void override(Relationships source) {
        super.override(source);
        this.type = source.getType();
        this.sourceText = source.getSourceText();
        this.destinationText = source.getDestinationText();
    }

    public void copyNonEmpty(Relationships source) {
        super.copyNonEmpty(source);
        if (source.getType() != null) {
            this.type = source.getType();
        }
        if (source.getSourceText() != null) {
            this.sourceText = source.getSourceText();
        }
        if (source.getDestinationText() != null) {
            this.destinationText = source.getDestinationText();
        }
    }

}