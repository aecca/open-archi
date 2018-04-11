package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.ItemCategory;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Flowchart extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FLOWCHART;

    @OneToOne(targetEntity = Category.class)
    private Category category;

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(ItemCategory category) {
        this.category = (Category) category;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }


    public void override(Flowchart source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setCategory(source.getCategory());
    }

    public void copyNonEmpty(Flowchart source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getCategory() != null) {
            this.setCategory(source.getCategory());
        }
    }
}
