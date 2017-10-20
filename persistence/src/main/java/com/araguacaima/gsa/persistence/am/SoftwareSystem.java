package com.araguacaima.gsa.persistence.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
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
@PersistenceContext(unitName = "gsa")
@Table(name = "SoftwareSystem", schema = "AM")
public class SoftwareSystem extends Element {

    @Column
    private Location location = Location.Unspecified;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "SoftwareSystem_Containers",
            joinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

    public SoftwareSystem() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    /**
     * Gets the location of this software system.
     *
     * @return a Location
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    void add(Container container) {
        containers.add(container);
    }

    /**
     * Gets the set of containers within this software system.
     *
     * @return a Set of Container objects
     */
    public Set<Container> getContainers() {
        return new HashSet<>(containers);
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }
}
