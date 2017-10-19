package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(schema = "Msa",
        name = "StatusLifeCycle")
public class StatusLifeCycle extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "Msa",
            name = "VolumetryStatusLifeCycle_Ancestors",
            joinColumns = {@JoinColumn(name = "Ancestors_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Ancestors_Id",
                    referencedColumnName = "Id")})
    private Collection<Status> ancestors;
    @OneToOne
    private Status current;
    @OneToMany
    @JoinTable(schema = "Msa",
            name = "VolumetryStatusLifeCycle_Descendants",
            joinColumns = {@JoinColumn(name = "Status_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Status_Id",
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
