package com.araguacaima.gsa.persistence.msa;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "CapacityReliability")

public class CapacityReliability extends AbstractReliability implements IVolumetricReliability {

    @OneToOne
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
