package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

/**
 * A relationship between two entities.
 */

public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {
    private RelationshipType type;
    private String sourceText;
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

    public void override(Relationships source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.type = source.getType();
        this.sourceText = source.getSourceText();
        this.destinationText = source.getDestinationText();
    }

    public void copyNonEmpty(Relationships source, boolean keepMeta) {
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