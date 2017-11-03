package com.araguacaima.gsa.persistence.diagrams.sequence;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Sequence", schema = "DIAGRAMS")
public class Sequence extends Item {

    @Column
    private int start;
    @Column
    private int duration;
    @Column
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
