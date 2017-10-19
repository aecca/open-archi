package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "Msa",
        name = "Effort")
public class Effort extends BaseEntity {

    @Column
    private double costs;
    @Column
    private double days;
    @Column
    private int hours;

    public double getCosts() {
        return costs;
    }

    public void setCosts(double effortInCosts) {
        this.costs = effortInCosts;
    }

    public double getDays() {
        return days;
    }

    public void setDays(double effortInDays) {
        this.days = effortInDays;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int effortInHours) {
        this.hours = effortInHours;
    }
}
