package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Flowchart extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FLOWCHART;

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }


    public void override(Flowchart source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
    }

    public void copyNonEmpty(Flowchart source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setGroup(boolean container) {

    }
}
