package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

public class Range<T extends Enum> extends BaseEntity {

    private Integer lower;

    private T unit;

    private Integer upper;

    public Range(T unit, Integer lower, Integer upper) {
        this.unit = unit;
        this.lower = lower;
        this.upper = upper;
    }

    public Range() {
    }

    public Integer getLower() {
        return lower;
    }

    public void setLower(Integer lower) {
        this.lower = lower;
    }

    public T getUnit() {
        return unit;
    }

    public void setUnit(T unit) {
        this.unit = unit;
    }

    public Integer getUpper() {
        return upper;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }
}
