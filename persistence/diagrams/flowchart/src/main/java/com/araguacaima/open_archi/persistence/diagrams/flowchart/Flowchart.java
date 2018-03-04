package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Flowchart extends Item {
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FLOWCHART;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }


    public void override(Flowchart source) {
        super.override(source);
        this.setCategory(source.getCategory());
    }

    public void copyNonEmpty(Flowchart source) {
        super.copyNonEmpty(source);
        if (source.getCategory() != null) {
            this.setCategory(source.getCategory());
        }
    }
}
