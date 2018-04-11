package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipKind;
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
    @Enumerated(EnumType.STRING)
    private InteractionStyle interactionStyle = InteractionStyle.SYNCHRONOUS;

    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type = RelationshipType.BIDIRECTIONAL;

    @Column
    @Enumerated(EnumType.STRING)
    protected RelationshipKind kind = RelationshipKind.ARCHITECTURE_RELATIONSHIP;

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

    public RelationshipKind getKind() {
        return kind;
    }

    public void setKind(RelationshipKind kind) {
        this.kind = kind;
    }

    public void override(Relationship source, boolean keepMeta) {
        super.override(source, keepMeta, suffix);
        this.technology = source.getTechnology();
        this.interactionStyle = source.getInteractionStyle();
        this.type = source.getType();
    }

    public void copyNonEmpty(Relationship source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
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