package com.araguacaima.gsa.persistence.diagrams.sequence;

import com.araguacaima.gsa.persistence.diagrams.core.Constants;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two Gantt activities.
 */
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship {

    private RelationshipType type;
    private int time;

    public Relationship() {
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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