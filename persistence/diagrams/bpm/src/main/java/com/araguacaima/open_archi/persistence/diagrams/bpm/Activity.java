package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Activity extends Item {

    //TODO Completar
    public void override(Activity source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
    }

    public void copyNonEmpty(Activity source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }

}
