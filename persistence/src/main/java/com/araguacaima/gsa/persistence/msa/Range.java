package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "Range")
public class Range<T extends Enum> extends BaseEntity {

    @Column
    private Integer lower;
    @Column
    private T unit;
    @Column
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
