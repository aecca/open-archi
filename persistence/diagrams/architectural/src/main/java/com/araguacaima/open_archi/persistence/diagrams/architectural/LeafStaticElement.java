package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import java.util.Collection;

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

    public Collection<BaseEntity> override(LeafStaticElement source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        return super.override(source, keepMeta, suffix, clonedFrom);
    }

    public Collection<BaseEntity> copyNonEmpty(LeafStaticElement source, boolean keepMeta) {
        return super.copyNonEmpty(source, keepMeta);
    }
}
