package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * This is the superclass for model elements that describe the static structure
 * of a software system, namely Person, SoftwareSystem, Container and Component.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class StaticElement extends Element {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.COMPONENT;

    public StaticElement() {
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
