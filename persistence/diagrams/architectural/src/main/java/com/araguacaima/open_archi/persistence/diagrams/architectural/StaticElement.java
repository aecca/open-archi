package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * This is the superclass for model elements that describe the static structure
 * of a software system, namely Person, SoftwareSystem, Container and Component.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class StaticElement extends Element {

    public StaticElement() {
        setKind(ElementKind.COMPONENT);
    }

    public void override(StaticElement source, boolean keepMeta) {
        super.override(source, keepMeta, suffix);
    }

    public void copyNonEmpty(StaticElement source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }
}
