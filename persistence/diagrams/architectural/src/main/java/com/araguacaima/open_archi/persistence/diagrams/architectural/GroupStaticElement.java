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
public abstract class GroupStaticElement extends StaticElement {

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public void setGroup(boolean container) {

    }

    public void override(GroupStaticElement source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
    }

    public void copyNonEmpty(GroupStaticElement source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }
}
