package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
public abstract class ModelElement extends Element {

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public void setGroup(boolean container) {

    }
}