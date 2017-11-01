package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "msa")
@Table(schema = "MSA",
        name = "Measurable")
public class Measurable extends BaseEntity {

    @OneToOne
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
}
