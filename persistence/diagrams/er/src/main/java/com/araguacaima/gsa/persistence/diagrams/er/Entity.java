package com.araguacaima.gsa.persistence.diagrams.er;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Entity extends Element {
    private ElementKind kind = ElementKind.ENTITY_RELATIONSHIP_MODEL;
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
