package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Pool", schema = "DIAGRAMS")
public class Pool extends Item {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Pool_Lanes",
            joinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")})
    private Collection<Lane> lanes;

    @Column
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
