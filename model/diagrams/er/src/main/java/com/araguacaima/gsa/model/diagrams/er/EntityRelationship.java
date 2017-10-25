package com.araguacaima.gsa.model.diagrams.er;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Set;

public class EntityRelationship extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "|";
    private ElementKind kind = ElementKind.UML_CLASS_MODEL;

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
