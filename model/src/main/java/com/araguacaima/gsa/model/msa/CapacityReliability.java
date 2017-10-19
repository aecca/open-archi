package com.araguacaima.gsa.model.msa;

public class CapacityReliability extends AbstractReliability implements IVolumetricReliability {

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
