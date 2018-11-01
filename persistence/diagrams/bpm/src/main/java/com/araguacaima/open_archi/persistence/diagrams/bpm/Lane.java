package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
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

    public Collection<BaseEntity> override(Lane source, boolean keepMeta, String suffix, CompositeElement clonedBy) {
        Collection<BaseEntity> overriten = new ArrayList<>();
        overriten.addAll(super.override(source, keepMeta, suffix, clonedBy));
        this.activities = source.getActivities();
        return overriten;
    }

    public Collection<BaseEntity> copyNonEmpty(Lane source, boolean keepMeta) {
        Collection<BaseEntity> overriten = new ArrayList<>();
        overriten.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getActivities() != null && !source.getActivities().isEmpty()) {
            this.activities = source.getActivities();
        }
        return overriten;
    }

    @Override
    public boolean isIsGroup() {
        return true;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}
