package com.araguacaima.gsa.persistence.diagrams.architectural;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * This is the superclass for model elements that describe the static structure
 * of a software system, namely Person, SoftwareSystem, Container and Component.
 */

@MappedSuperclass
public abstract class StaticElement extends Element<StaticElement> {

    @OneToOne
    private Model model;
    @Column
    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;

    public StaticElement() {
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
