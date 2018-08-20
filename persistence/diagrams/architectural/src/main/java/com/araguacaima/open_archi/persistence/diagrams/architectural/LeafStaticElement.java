package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * This is the superclass for model elements that describe the static structure
 * of a system, namely Person, System, Container and Component.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class LeafStaticElement extends StaticElement {

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }

    public void override(LeafStaticElement source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
    }

    public void copyNonEmpty(LeafStaticElement source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }
}
