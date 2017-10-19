package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceContext(unitName = "gsa")
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
