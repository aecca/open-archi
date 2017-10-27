package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two classes.
 */
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship<UmlClass> {

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