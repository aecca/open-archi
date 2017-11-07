package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.commons.exceptions.EntityError;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Activity", schema = "DIAGRAMS")
public class Activity extends Item {

    @OneToOne
    private Lane lane;

    @Column
    private ElementKind kind;

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

}
