package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa")
@DiscriminatorValue(value = "BpmModel")
public class Model extends Element {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Pools",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")})
    private Collection<Pool> pools;

    @Column
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<Pool> getPools() {
        return pools;
    }

    public void setPools(Collection<Pool> pools) {
        this.pools = pools;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
