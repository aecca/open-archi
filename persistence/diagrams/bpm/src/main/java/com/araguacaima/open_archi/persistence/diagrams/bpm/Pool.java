package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Pool extends Item {

    @ManyToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Pool_Lanes",
            joinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")})
    private Collection<Lane> lanes;

    public Pool() {
        setKind(ElementKind.BPM);
    }

    public Collection<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(Collection<Lane> lanes) {
        this.lanes = lanes;
    }

    public void override(Pool source) {
        super.override(source);
        this.setLanes(source.getLanes());
    }

    public void copyNonEmpty(Pool source) {
        super.copyNonEmpty(source);
        if (source.getLanes() != null && !source.getLanes().isEmpty()) {
            this.setLanes(source.getLanes());
        }
    }

}
