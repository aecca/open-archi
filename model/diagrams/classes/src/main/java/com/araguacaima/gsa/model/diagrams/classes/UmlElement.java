package com.araguacaima.gsa.model.diagrams.classes;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
public abstract class UmlElement extends Element {


    public static final String CANONICAL_NAME_SEPARATOR = ".";
    private ElementKind kind = ElementKind.UML_CLASS;

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