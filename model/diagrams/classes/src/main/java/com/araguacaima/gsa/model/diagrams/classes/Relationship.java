package com.araguacaima.gsa.model.diagrams.classes;

import com.araguacaima.gsa.model.diagrams.core.Constants;
import com.araguacaima.gsa.model.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two classes.
 */
public class Relationship extends com.araguacaima.gsa.model.diagrams.core.Relationship<UmlClass> {

    private RelationshipType type;

    public Relationship() {
    }


    @Override
    public RelationshipType getType() {
        return type;
    }

    @Override
    public void setType(RelationshipType type) {
        this.type = type;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return build(Constants.RELATIONSHIP);
    }

}