package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@PersistenceUnit(unitName = "gsa" )
public abstract class Taggable extends BaseEntity {

    @ElementCollection
    @CollectionTable(name = "Tag", schema = "DIAGRAMS")
    private Set<String> tags = new LinkedHashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
