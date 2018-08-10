package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
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

    public void override(Entity source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setAttributes(source.getAttributes());
    }

    public void copyNonEmpty(Entity source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getAttributes() != null && !source.getAttributes().isEmpty()) {
            this.setAttributes(source.getAttributes());
        }
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public void setGroup(boolean container) {

    }
}
