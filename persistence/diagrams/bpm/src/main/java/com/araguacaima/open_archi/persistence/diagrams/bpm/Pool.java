package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Pool extends Item {

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Pool_Lanes",
            joinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Lane_Id",
                    referencedColumnName = "Id")})
    private Collection<Lane> lanes;

    public Pool() {
        setKind(ElementKind.BPM);
    }

    public Collection<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(Collection<Lane> lanes) {
        this.lanes = lanes;
    }

    public Collection<BaseEntity> override(Pool source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (Lane consumer : source.getLanes()) {
            Lane newLane = new Lane();
            overriden.addAll(newLane.override(consumer, keepMeta, suffix, clonedFrom));
            this.lanes.add(newLane);
            overriden.add(newLane);
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Pool source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getLanes() != null && !source.getLanes().isEmpty()) {
            for (Lane consumer : source.getLanes()) {
                Lane newLane = new Lane();
                overriden.addAll(newLane.copyNonEmpty(consumer, keepMeta));
                this.lanes.add(newLane);
                overriden.add(newLane);
            }
        }
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return true;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}
