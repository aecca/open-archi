package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class ModelElements extends Elements {

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public void setGroup(boolean container) {

    }
}