package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PersistenceContext;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceContext(unitName = "gsa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Taggable extends BaseEntity {

    private Set<String> tags = new LinkedHashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
