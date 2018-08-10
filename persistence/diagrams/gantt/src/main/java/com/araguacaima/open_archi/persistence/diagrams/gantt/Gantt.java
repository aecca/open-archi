package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Gantt extends Item {

    @Column
    private int diagramStart;

    @Column
    private int diagramEnd;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.GANTT;

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

    public void override(Gantt source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setDiagramStart(source.getDiagramStart());
        this.setDiagramEnd(source.getDiagramEnd());
    }

    public void copyNonEmpty(Gantt source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getDiagramStart() != 0) {
            this.setDiagramStart(source.getDiagramStart());
        }
        if (source.getDiagramEnd() != 0) {
            this.setDiagramEnd(source.getDiagramEnd());
        }
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setGroup(boolean container) {

    }
}
