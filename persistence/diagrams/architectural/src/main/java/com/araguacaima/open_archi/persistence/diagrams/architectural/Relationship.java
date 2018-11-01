package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipKind;
import com.araguacaima.open_archi.persistence.diagrams.core.RelationshipType;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A relationship between two architectural elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("ArchitectureRelationship")
public class Relationship extends com.araguacaima.open_archi.persistence.diagrams.core.Relationship {

    @Column
    @Enumerated(EnumType.STRING)
    protected RelationshipKind kind = RelationshipKind.ARCHITECTURE_RELATIONSHIP;
    @Column
    private String technology;
    @Column
    @Enumerated(EnumType.STRING)
    private InteractionStyle interactionStyle = InteractionStyle.SYNCHRONOUS;
    @Column
    @Enumerated(EnumType.STRING)
    private RelationshipType type = RelationshipType.BIDIRECTIONAL;

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

    public Collection<BaseEntity> override(Relationship source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.technology = source.getTechnology();
        this.interactionStyle = source.getInteractionStyle();
        this.type = source.getType();
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Relationship source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getTechnology() != null) {
            this.technology = source.getTechnology();
        }
        if (source.getInteractionStyle() != null) {
            this.interactionStyle = source.getInteractionStyle();
        }
        if (source.getType() != null) {
            this.type = source.getType();
        }
        return overriden;
    }
}