package com.araguacaima.gsa.persistence.diagrams.architectural;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A container represents something that hosts code or data. A container is
 * something that needs to be running in order for the overall software system
 * to work. In real terms, a container is something like a server-side web application,
 * a client-side web application, client-side desktop application, a mobile app,
 * a microservice, a database schema, a file system, etc.
 * <p>
 * A container is essentially a context or boundary inside which some code is executed
 * or some data is stored. And each container is a separately deployable thing.
 */
@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Container", schema = "DIAGRAMS")
public class Container extends StaticElement {

    @Column
    private String technology;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Container_Components",
            joinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

    public Container() {
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }
}
