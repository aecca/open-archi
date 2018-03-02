package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue("Sequence")
public class Sequence extends Item {

    @Column
    private int start;
    @Column
    private int duration;

    public Sequence() {
        setKind(ElementKind.SEQUENCE);
    }

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
