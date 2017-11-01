package com.araguacaima.gsa.persistence.diagrams.architectural;

import com.araguacaima.gsa.persistence.diagrams.core.Item;
import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;
import com.araguacaima.gsa.persistence.diagrams.core.Taggable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * A relationship between two architectural elements.
 */
@Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "Architecture_Relationship", schema = "DIAGRAMS")
public class Relationship extends com.araguacaima.gsa.persistence.diagrams.core.Relationship<StaticElement> {

    @Column
    private String technology;
    @Column
    private InteractionStyle interactionStyle = InteractionStyle.Synchronous;
    @Column
    private RelationshipType type;

    public Relationship() {
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public InteractionStyle getInteractionStyle() {
        return interactionStyle;
    }

    public void setInteractionStyle(InteractionStyle interactionStyle) {
        this.interactionStyle = interactionStyle;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
}