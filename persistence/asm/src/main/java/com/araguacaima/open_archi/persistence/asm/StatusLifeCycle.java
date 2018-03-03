package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "StatusLifeCycle")
@DynamicUpdate
public class StatusLifeCycle extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "ASM",
            name = "StatusLifeCycle_Ancestors",
            joinColumns = {@JoinColumn(name = "StatusLifeCycle_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Ancestor_Id",
                    referencedColumnName = "Id")})
    private Collection<Status> ancestors;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Status current;
    @OneToMany
    @JoinTable(schema = "ASM",
            name = "StatusLifeCycle_Descendants",
            joinColumns = {@JoinColumn(name = "StatusLifeCycle_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Descendant_Id",
                    referencedColumnName = "Id")})
    private Collection<Status> descendants;

    public Collection<Status> getAncestors() {
        return ancestors;
    }

    public void setAncestors(Collection<Status> ancestors) {
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

    public void setDescendants(Collection<Status> descendants) {
        this.descendants = descendants;
    }
}
