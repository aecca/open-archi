package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

public class Measurable extends BaseEntity {

    private Range range;

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
