package com.araguacaima.open_archi.persistence.asm;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
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
