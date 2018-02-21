package com.araguacaima.open_archi.persistence.diagrams.core;


import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "IncomingConstraint", schema = "DIAGRAMS")
public class IncomingConstraint extends Taggable{

    private Set<String> canBeUsedFrom;
    private int maxConcurrentUses;
    private int maxAmountOfData;

    public Set<String> getCanBeUsedFrom() {
        return canBeUsedFrom;
    }

    public void setCanBeUsedFrom(Set<String> canBeUsedFrom) {
        this.canBeUsedFrom = canBeUsedFrom;
    }

    public int getMaxConcurrentUses() {
        return maxConcurrentUses;
    }

    public void setMaxConcurrentUses(int maxConcurrentUses) {
        this.maxConcurrentUses = maxConcurrentUses;
    }
}
