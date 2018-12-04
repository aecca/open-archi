package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Elements;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.Collection;

/**
 * This is the superclass for model elements that describe the static structure
 * of a system, namely Person, System, Container and Component.
 */
public abstract class StaticElements extends Elements {

    private ElementKind kind = ElementKind.COMPONENT;

    public StaticElements() {
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Collection<BaseEntity> override(StaticElements source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        return super.override(source, keepMeta, suffix, clonedFrom);
    }

    public Collection<BaseEntity> copyNonEmpty(StaticElements source, boolean keepMeta) {
        return super.copyNonEmpty(source, keepMeta);
    }
}
