package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A container represents something that hosts code or data. A container is
 * something that needs to be running in order for the overall system
 * to work. In real terms, a container is something like a server-side web application,
 * a client-side web application, client-side desktop application, a mobile app,
 * a microservice, a database schema, a file system, etc.
 * <p>
 * A container is essentially a context or boundary inside which some code is executed
 * or some data is stored. And each container is a separately deployable thing.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Containers extends GroupStaticElements {

    @Column
    private String technology;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Container_Components",
            joinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Components> components = new LinkedHashSet<>();

    public Containers() {
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Set<Components> getComponents() {
        return components;
    }

    public void setComponents(Set<Components> components) {
        this.components = components;
    }


    public Collection<BaseEntity> override(Containers source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setTechnology(source.getTechnology());
        this.setComponents(source.getComponents());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Containers source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            this.setComponents(source.getComponents());
        }
        return overriden;
    }

}
