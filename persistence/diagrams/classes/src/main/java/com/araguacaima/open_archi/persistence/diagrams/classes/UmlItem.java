package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

/**
 * This is the superclass for all model elements.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class UmlItem extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.UML_CLASS_MODEL;

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

}