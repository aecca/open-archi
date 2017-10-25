package com.araguacaima.gsa.model.diagrams.bpm;

import java.util.Collection;

public class Lane {

    private Pool pool;
    private Collection<Activity> activities;

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Collection<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Collection<Activity> activities) {
        this.activities = activities;
    }
}
