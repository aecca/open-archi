package com.araguacaima.gsa.model.diagrams.er;

import java.util.Collection;

public class Entity extends EntityRelationship {

    private Collection<Attribute> attributes;

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }
}
