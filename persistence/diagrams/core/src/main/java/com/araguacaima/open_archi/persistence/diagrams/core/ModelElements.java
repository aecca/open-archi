package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

/**
 * This is the superclass for all model elements.
 */

public abstract class ModelElements extends Elements {

    @Override
    public boolean isIsGroup() {
        return true;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}