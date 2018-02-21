package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Pool extends Item {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Pool_Lanes",
            joinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")})
    private Collection<Lane> lanes;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind;

    public Collection<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(Collection<Lane> lanes) {
        this.lanes = lanes;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}