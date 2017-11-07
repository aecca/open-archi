package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.sun.tracing.dtrace.ModuleAttributes;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Tags", schema = "DIAGRAMS")
@DiscriminatorColumn(name = "diagramType")

@NamedQueries({ @NamedQuery(name = Taggable.GET_ALL_MODELS,
        query = "select a from Taggable a ")})
public class Taggable extends BaseEntity {

    public static final String GET_ALL_MODELS = "get.all.models";
    @ElementCollection
    @CollectionTable(name = "Tag", schema = "DIAGRAMS")
    protected Set<String> tags = new LinkedHashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
