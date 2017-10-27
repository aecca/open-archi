package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
public abstract class UmlItem extends Item {

    private ElementKind kind = ElementKind.UML_CLASS_MODEL;

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}