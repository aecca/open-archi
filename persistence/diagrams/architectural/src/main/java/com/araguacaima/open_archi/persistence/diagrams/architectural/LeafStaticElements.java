package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.Collection;

/**
 * This is the superclass for model elements that describe the static structure
 * of a system, namely Person, System, Container and Component.
 */
public abstract class LeafStaticElements extends StaticElements {

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }

    public Collection<BaseEntity> override(LeafStaticElements source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
       return super.override(source, keepMeta, suffix, clonedFrom);
    }

    public Collection<BaseEntity> copyNonEmpty(LeafStaticElements source, boolean keepMeta) {
        return super.copyNonEmpty(source, keepMeta);
    }
}
