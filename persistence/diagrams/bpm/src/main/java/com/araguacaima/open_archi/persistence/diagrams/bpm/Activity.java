package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Activity extends Item {

    public Collection<BaseEntity> override(Activity source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        return super.override(source, keepMeta, suffix, clonedFrom);
    }

    public Collection<BaseEntity> copyNonEmpty(Activity source, boolean keepMeta) {
        return super.copyNonEmpty(source, keepMeta);
    }

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}
