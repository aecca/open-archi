package com.araguacaima.gsa.persistence.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
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
@PersistenceContext(unitName = "gsa")
@Table(name = "Container", schema = "AM")
public class Container extends Element {

    @OneToOne
    private SoftwareSystem parent;

    @Column
    private String technology;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Container_Components",
            joinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

    public Container() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    void setParent(SoftwareSystem parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return parent;
    }

    /**
     * Gets the technology associated with thie container (e.g. Apache Tomcat).
     *
     * @return the technology, as a String,
     * or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }


    /**
     * Gets the set of components within this software system.
     *
     * @return a Set of Component objects
     */
    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }
}
