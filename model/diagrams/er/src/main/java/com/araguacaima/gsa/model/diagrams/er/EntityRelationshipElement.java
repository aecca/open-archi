package com.araguacaima.gsa.model.diagrams.er;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
public abstract class EntityRelationshipElement extends Element {

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
}