package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Activity extends Item {


    public void override(Activity source) {
        super.override(source);
    }

    public void copyNonEmpty(Activity source) {
        super.copyNonEmpty(source);
    }

}
