package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
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
