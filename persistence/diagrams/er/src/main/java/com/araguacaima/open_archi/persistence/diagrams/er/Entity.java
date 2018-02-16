package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@javax.persistence.Entity
@PersistenceUnit(unitName = "open-archi")
public class Entity extends Element {
    @Column
    private ElementKind kind = ElementKind.ENTITY_RELATIONSHIP_MODEL;
    @OneToMany
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
}
