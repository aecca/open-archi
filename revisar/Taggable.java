package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceContext(unitName = "gsa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Taggable extends BaseEntity {

    @ElementCollection
    @CollectionTable(name = "Tag", schema = "AM")
    private Set<String> tags = new LinkedHashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
