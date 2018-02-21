package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Lane", schema = "DIAGRAMS")
public class Lane extends BaseEntity {

/*    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Pool pool;*/

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Lane_Activities",
            joinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Activity_Id",
                    referencedColumnName = "Id")})
    private Collection<Activity> activities;

/*    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }*/

    public Collection<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Collection<Activity> activities) {
        this.activities = activities;
    }
}