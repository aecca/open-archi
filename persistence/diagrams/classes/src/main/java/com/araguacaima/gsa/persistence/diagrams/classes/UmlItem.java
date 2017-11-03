package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import javax.persistence.*;

/**
 * This is the superclass for all model elements.
 */
@Entity
@PersistenceUnit(unitName = "gsa" )
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UmlItem extends Item {

    @Column
    private ElementKind kind = ElementKind.UML_CLASS_MODEL;

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}