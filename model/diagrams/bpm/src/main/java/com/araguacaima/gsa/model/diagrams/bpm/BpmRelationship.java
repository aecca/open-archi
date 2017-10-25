package com.araguacaima.gsa.model.diagrams.bpm;

import com.araguacaima.gsa.model.diagrams.core.Constants;
import com.araguacaima.gsa.model.diagrams.core.Relationship;
import com.araguacaima.gsa.model.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two classes.
 */
public class BpmRelationship extends Relationship {

    private RelationshipType type;

    public BpmRelationship() {
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