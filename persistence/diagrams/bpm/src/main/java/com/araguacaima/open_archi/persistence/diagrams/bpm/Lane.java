package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Lane", schema = "DIAGRAMS")
@DynamicUpdate
public class Lane extends BaseEntity {

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Lane_Activities",
            joinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Activity_Id",
                    referencedColumnName = "Id")})
    private Collection<Activity> activities;

    public Collection<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Collection<Activity> activities) {
        this.activities = activities;
    }

    public void override(Lane source) {
        super.override(source);
        this.activities = source.getActivities();
    }

    public void copyNonEmpty(Lane source) {
        super.copyNonEmpty(source);
        if (source.getActivities() != null && !source.getActivities().isEmpty()) {
            this.activities = source.getActivities();
        }
    }
}
