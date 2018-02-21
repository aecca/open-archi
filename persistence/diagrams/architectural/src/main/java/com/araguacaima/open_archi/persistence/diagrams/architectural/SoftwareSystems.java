package com.araguacaima.open_archi.persistence.diagrams.architectural;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A software system is the highest level of abstraction and describes something
 * that delivers value to its users, whether they are human or not. This includes
 * the software system you are modelling, and the other software systems upon
 * which your software system depends.
 * <p>
 * See <a href="https://structurizr.com/help/model#SoftwareSystem">Model - Software System</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class SoftwareSystems extends StaticElements {

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "SoftwareSystem_Containers",
            joinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Containers> containers = new LinkedHashSet<>();

    public SoftwareSystems() {
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<Containers> getContainers() {
        return containers;
    }

    public void setContainers(Set<Containers> containers) {
        this.containers = containers;
    }
}