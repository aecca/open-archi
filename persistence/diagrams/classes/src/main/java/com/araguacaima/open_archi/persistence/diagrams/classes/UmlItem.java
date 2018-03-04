package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * This is the superclass for all model elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class UmlItem extends Item {

    public UmlItem() {
        setKind(ElementKind.UML_CLASS);
    }

    public void override(UmlItem source) {
        super.override(source);
    }

    public void copyNonEmpty(UmlItem source) {
        super.copyNonEmpty(source);
    }
}