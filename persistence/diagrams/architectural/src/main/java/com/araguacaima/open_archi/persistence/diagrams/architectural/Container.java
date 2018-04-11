package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

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
@PersistenceUnit(unitName = "open-archi")
public class Container extends StaticElement {

    @Column
    private String technology;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Container_Components",
            joinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

    public static final String SHAPE_COLOR = "#08427B";

    public Container() {
        setKind(ElementKind.CONTAINER);
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

    public void override(Container source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setTechnology(source.getTechnology());
        this.setComponents(source.getComponents());
    }

    public void copyNonEmpty(Container source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            this.setComponents(source.getComponents());
        }
    }

}
