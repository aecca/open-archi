package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

public class Effort extends BaseEntity {

    private double costs;

    private double days;

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
