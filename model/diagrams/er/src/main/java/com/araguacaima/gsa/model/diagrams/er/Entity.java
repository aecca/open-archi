package com.araguacaima.gsa.model.diagrams.er;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Entity extends Element {
    public static final String CANONICAL_NAME_SEPARATOR = "|";
    private ElementKind kind = ElementKind.ENTITY_RELATIONSHIP_MODEL;

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return null;
    }

    private Collection<Attribute> attributes;

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }
}
