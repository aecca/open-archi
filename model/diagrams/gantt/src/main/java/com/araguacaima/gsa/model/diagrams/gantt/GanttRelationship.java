package com.araguacaima.gsa.model.diagrams.gantt;

import com.araguacaima.gsa.model.diagrams.core.Constants;
import com.araguacaima.gsa.model.diagrams.core.Relationship;
import com.araguacaima.gsa.model.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two classes.
 */
public class GanttRelationship extends Relationship {

    private RelationshipType type;

    public GanttRelationship() {
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