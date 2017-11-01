package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "Lane", schema = "DIAGRAMS")
public class Lane extends BaseEntity{

    @OneToOne
    private Pool pool;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Lane_Activities",
            joinColumns = {@JoinColumn(name = "Activity_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Activity_Id",
                    referencedColumnName = "Id")})
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
