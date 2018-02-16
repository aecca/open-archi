package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two architectural elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("ArchitectureRelationship")
public class Relationship extends com.araguacaima.open_archi.persistence.diagrams.core.Relationship {

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