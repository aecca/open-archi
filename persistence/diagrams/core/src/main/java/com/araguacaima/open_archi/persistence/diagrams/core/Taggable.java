package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Models", schema = "DIAGRAMS", uniqueConstraints= @UniqueConstraint(columnNames={"name", "kind"}))
@DiscriminatorColumn(name = "modelType", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({@NamedQuery(name = Taggable.GET_ALL_MODELS,
        query = "select a from Taggable a "), @NamedQuery(name = Taggable.GET_MODELS_BY_TYPE,
        query = "select a from Taggable a where TYPE(a)=:modelType")})
@Component
public class Taggable extends BaseEntity {

    public static final String GET_ALL_MODELS = "get.all.models";
    public static final String GET_MODELS_BY_TYPE = "get.models.by.type";
    @ElementCollection
    @CollectionTable(name = "Tags", schema = "DIAGRAMS")
    protected Set<String> tags = new LinkedHashSet<>();

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void copy(Taggable source) {
        super.copy(source);
        this.tags = source.getTags();
    }
}
