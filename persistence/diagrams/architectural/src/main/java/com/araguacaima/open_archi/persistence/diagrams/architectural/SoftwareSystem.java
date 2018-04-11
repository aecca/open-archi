package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

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
@NamedQueries({
        @NamedQuery(name = SoftwareSystem.GET_ALL_CONTAINERS,
                query = "select a.containers from SoftwareSystem a where a.id=:id"),
        @NamedQuery(name = SoftwareSystem.GET_CONTAINER,
                query = "select c from SoftwareSystem a JOIN a.containers c where a.id=:id and c.id=:cid")})
public class SoftwareSystem extends StaticElement {

    public static final String GET_ALL_CONTAINERS = "get.all.containers";
    public static final String GET_CONTAINER = "get.container";
    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "SoftwareSystem_Containers",
            joinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

    public static final String SHAPE_COLOR = "#01203A";

    public SoftwareSystem() {
        setKind(ElementKind.SOFTWARE_SYSTEM);
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<Container> getContainers() {
        return containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    public void override(SoftwareSystem source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setScope(source.getScope());
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            newContainer.override(container, keepMeta);
            this.containers.add(newContainer);
        }
    }

    public void copyNonEmpty(SoftwareSystem source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container container : source.getContainers()) {
                Container newContainer = new Container();
                newContainer.copyNonEmpty(container, keepMeta);
                this.containers.add(newContainer);
            }
        }
    }
}
