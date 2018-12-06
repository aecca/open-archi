package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "StatusLifeCycle")
@DynamicUpdate
public class StatusLifeCycle extends BaseEntity {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "ASM",
            name = "StatusLifeCycle_Ancestors",
            joinColumns = {@JoinColumn(name = "StatusLifeCycle_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Ancestor_Id",
                    referencedColumnName = "Id")})
    private Set<Status> ancestors;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Status current;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "ASM",
            name = "StatusLifeCycle_Descendants",
            joinColumns = {@JoinColumn(name = "StatusLifeCycle_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Descendant_Id",
                    referencedColumnName = "Id")})
    private Set<Status> descendants;

    public Collection<Status> getAncestors() {
        return ancestors;
    }

    public void setAncestors(Set<Status> ancestors) {
        this.ancestors = ancestors;
    }

    public Status getCurrent() {
        return current;
    }

    public void setCurrent(Status current) {
        this.current = current;
    }

    public Collection<Status> getDescendants() {
        return descendants;
    }

    public void setDescendants(Set<Status> descendants) {
        this.descendants = descendants;
    }
}
