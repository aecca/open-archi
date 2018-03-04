package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Gantt extends Item {
    @Column
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column
    private int diagramStart;
    @Column
    private int diagramEnd;
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.GANTT;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getDiagramStart() {
        return diagramStart;
    }

    public void setDiagramStart(int start) {
        this.diagramStart = start;
    }

    public int getDiagramEnd() {
        return diagramEnd;
    }

    public void setDiagramEnd(int end) {
        this.diagramEnd = end;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public void override(Gantt source) {
        super.override(source);
        this.setCategory(source.getCategory());
        this.setDiagramStart(source.getDiagramStart());
        this.setDiagramEnd(source.getDiagramEnd());
    }

    public void copyNonEmpty(Gantt source) {
        super.copyNonEmpty(source);
        if (source.getCategory() != null) {
            this.setCategory(source.getCategory());
        }
        if (source.getDiagramStart() != 0) {
            this.setDiagramStart(source.getDiagramStart());
        }
        if (source.getDiagramEnd() != 0) {
            this.setDiagramEnd(source.getDiagramEnd());
        }
    }
}
