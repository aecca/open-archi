package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipKind;
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

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipKind kind = RelationshipKind.ENTITY_RELATIONSHIP_RELATIONSHIP;

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

    public RelationshipKind getKind() {
        return kind;
    }

    public void setKind(RelationshipKind kind) {
        this.kind = kind;
    }

    public void override(Relationship source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.type = source.getType();
        this.sourceText = source.getSourceText();
        this.destinationText = source.getDestinationText();
    }

    public void copyNonEmpty(Relationship source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
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