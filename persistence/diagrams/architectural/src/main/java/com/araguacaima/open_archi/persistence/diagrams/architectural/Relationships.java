package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;

import javax.persistence.*;

/**
 * A relationship between two architectural elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("ArchitectureRelationships")
public class Relationships extends com.araguacaima.open_archi.persistence.diagrams.core.Relationships {

    @Column
    private String technology;

    @Column
    @Enumerated(EnumType.STRING)
    private InteractionStyle interactionStyle = InteractionStyle.SYNCHRONOUS;

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

    public Relationships() {
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

    public void override(Relationship source) {
        super.override(source);
        this.technology = source.getTechnology();
        this.interactionStyle = source.getInteractionStyle();
        this.type = source.getType();
    }

    public void copyNonEmpty(Relationship source) {
        super.copyNonEmpty(source);
        if (source.getTechnology() != null) {
            this.technology = source.getTechnology();
        }
        if (source.getInteractionStyle() != null) {
            this.interactionStyle = source.getInteractionStyle();
        }
        if (source.getType() != null) {
            this.type = source.getType();
        }

    }
}