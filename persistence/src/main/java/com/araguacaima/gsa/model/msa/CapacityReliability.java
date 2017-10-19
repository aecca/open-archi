package com.araguacaima.gsa.model.msa;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "Msa",
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
