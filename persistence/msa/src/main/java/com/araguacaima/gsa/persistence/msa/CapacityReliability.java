package com.araguacaima.gsa.persistence.msa;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "CapacityReliability")

public class CapacityReliability extends AbstractReliability implements IVolumetricReliability {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Volumetry volumetry;

    @Override
    public Volumetry getVolumetry() {
        return volumetry;
    }

    @Override
    public void setVolumetry(Volumetry volumetry) {
        this.volumetry = volumetry;
    }
}
