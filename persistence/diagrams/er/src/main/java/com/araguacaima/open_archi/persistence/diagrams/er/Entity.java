package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@javax.persistence.Entity
@PersistenceUnit(unitName = "open-archi")
public class Entity extends Element {
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.ENTITY_RELATIONSHIP_MODEL;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Entity_Attributes",
            joinColumns = {@JoinColumn(name = "Entity_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Attribute_Id",
                    referencedColumnName = "Id")})
    private Collection<Attribute> attributes;

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Collection<BaseEntity> override(Entity source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (Attribute att : source.getAttributes()) {
            Attribute newAttribute = new Attribute();
            overriden.addAll(newAttribute.override(att, keepMeta, suffix, clonedFrom));
            if(!this.attributes.add(newAttribute)) {
                this.attributes.remove(newAttribute);
                this.attributes.add(newAttribute);
            }
            overriden.add(newAttribute);
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Entity source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getAttributes() != null && !source.getAttributes().isEmpty()) {
            for (Attribute att : source.getAttributes()) {
                Attribute newAttribute = new Attribute();
                overriden.addAll(newAttribute.copyNonEmpty(att, keepMeta));
                if(!this.attributes.add(newAttribute)) {
                    this.attributes.remove(newAttribute);
                    this.attributes.add(newAttribute);
                }
                overriden.add(newAttribute);
            }
        }
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return true;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}
