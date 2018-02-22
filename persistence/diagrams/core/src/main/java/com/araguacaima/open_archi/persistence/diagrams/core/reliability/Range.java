package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "DIAGRAMS",
        name = "Range")
public class Range extends BaseEntity {

    @Column
    private String unit;
    @Column
    private Integer lower;
    @Column
    private Integer upper;

    public Range(String unit, Integer lower, Integer upper) {
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getUpper() {
        return upper;
    }

    public void setUpper(Integer upper) {
        this.upper = upper;
    }
}
