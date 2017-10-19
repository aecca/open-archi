package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "Msa",
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
