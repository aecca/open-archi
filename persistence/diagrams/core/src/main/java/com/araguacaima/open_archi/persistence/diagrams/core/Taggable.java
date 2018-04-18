package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Models", schema = "DIAGRAMS", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "kind"}))
@DynamicUpdate
@DiscriminatorColumn(name = "modelType", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({@NamedQuery(name = Taggable.GET_ALL_MODELS,
        query = "select a from Taggable a "), @NamedQuery(name = Taggable.GET_MODELS_BY_TYPE,
        query = "select a from Taggable a where TYPE(a)=:modelType"),
        @NamedQuery(name = Taggable.GET_MODELS_BY_STATUS,
                query = "select a from Taggable a where a.status=:status")})
@Component
public class Taggable extends BaseEntity {

    public static final String GET_ALL_MODELS = "get.all.models";
    public static final String GET_MODELS_BY_TYPE = "get.models.by.type";
    public static final String GET_MODELS_BY_STATUS = "get.models.by.status";

    @ElementCollection
    @CollectionTable(name = "Tags", schema = "DIAGRAMS")
    protected Set<String> tags = new LinkedHashSet<>();


    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.INITIAL;

    @OneToOne
    private ElementRole role;

    @OneToOne
    private CompositeElement clonedFrom;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Taggable_Cloned_By_Ids",
            joinColumns = {@JoinColumn(name = "Taggable_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Cloned_Tagabble_Id",
                    referencedColumnName = "Id")})
    private Set<CompositeElement> clonedBy;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ElementRole getRole() {
        return role;
    }

    public void setRole(ElementRole role) {
        this.role = role;
    }

    public CompositeElement getClonedFrom() {
        return clonedFrom;
    }

    public void setClonedFrom(CompositeElement clonedFrom) {
        this.clonedFrom = clonedFrom;
    }

    public Set<CompositeElement> getClonedBy() {
        return clonedBy;
    }

    public void setClonedBy(Set<CompositeElement> clonedBy) {
        this.clonedBy = clonedBy;
    }

    public void override(Taggable source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.tags = source.getTags();
        this.role = source.getRole();
    }

    public void copyNonEmpty(Taggable source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getTags() != null && !source.getTags().isEmpty()) {
            this.tags = source.getTags();
        }
        if (source.getRole() != null) {
            this.role = source.getRole();
        }
    }


}
