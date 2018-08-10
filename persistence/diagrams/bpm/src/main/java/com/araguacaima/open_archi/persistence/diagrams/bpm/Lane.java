package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DynamicUpdate
public class Lane extends Item {

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

    public void override(Lane source, boolean keepMeta, String suffix, Set<CompositeElement> clonedBy) {
        super.override(source, keepMeta, suffix);
        if (clonedBy != null) {
            this.setClonedBy(clonedBy);
        }
        this.activities = source.getActivities();
    }

    public void copyNonEmpty(Lane source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getActivities() != null && !source.getActivities().isEmpty()) {
            this.activities = source.getActivities();
        }
    }
}
