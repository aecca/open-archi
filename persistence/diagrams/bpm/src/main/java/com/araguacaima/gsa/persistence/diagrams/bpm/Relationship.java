package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two classes.
 */
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship<Activity> {

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