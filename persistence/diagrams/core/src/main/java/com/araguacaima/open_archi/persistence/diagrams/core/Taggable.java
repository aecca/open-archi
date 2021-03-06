package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Models", schema = "DIAGRAMS", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "kind"}))
@DynamicUpdate
@DiscriminatorColumn(name = "modelType", discriminatorType = DiscriminatorType.STRING)
@Component
public abstract class Taggable extends BaseEntity {

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Taggable_Cloned_By_Ids",
            joinColumns = {@JoinColumn(name = "Taggable_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Cloned_Tagabble_Id",
                    referencedColumnName = "Id")})
    private Set<CompositeElement> clonedBy;

    @Column
    private Integer rank = -1000;

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

    abstract public boolean isIsGroup();

    abstract public void setIsGroup(boolean container);

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Collection<BaseEntity> override(Taggable source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.tags = source.getTags();
        this.role = source.getRole();
        this.rank = source.getRank();
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Taggable source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.copyNonEmpty(source, keepMeta);
        if (source.getTags() != null && !source.getTags().isEmpty()) {
            this.tags = source.getTags();
        }
        if (source.getRole() != null) {
            this.role = source.getRole();
        }
        if (source.getRank() != null) {
            this.rank = source.getRank();
        }
        return overriden;
    }


}
