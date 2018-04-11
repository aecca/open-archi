package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "DIAGRAMS", name = "Measurable")
@DynamicUpdate
public class Measurable extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Range range;

    @Column
    private double value;

    public Measurable(Range range) {
        this.range = range;
    }

    public Measurable() {
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void override(Measurable source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setValue(source.getValue());
    }

    public void copyNonEmpty(Measurable source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getValue() != 0) {
            this.setValue(source.getValue());
        }
    }
}
