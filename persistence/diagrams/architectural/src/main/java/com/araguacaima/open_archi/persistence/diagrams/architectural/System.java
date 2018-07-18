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
 * See <a href="https://structurizr.com/help/model#System">Model - Software System</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = System.GET_ALL_CONTAINERS,
                query = "select a.containers from System a where a.id=:id"),
        @NamedQuery(name = System.GET_CONTAINER,
                query = "select c from System a JOIN a.containers c where a.id=:id and c.id=:cid")})
public class System extends StaticElement {

    public static final String GET_ALL_CONTAINERS = "get.all.containers";
    public static final String GET_CONTAINER = "get.container";
    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Systems",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Child_System_Id",
                    referencedColumnName = "Id")})
    private Set<System> systems = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Containers",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Components",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

    public static final String SHAPE_COLOR = "#01203A";

    public System() {
        setKind(ElementKind.SYSTEM);
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

    public Set<System> getSystems() {
        return systems;
    }

    public void setSystems(Set<System> systems) {
        this.systems = systems;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public void override(System source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setScope(source.getScope());
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            newContainer.override(container, keepMeta, suffix);
            this.containers.add(newContainer);
        }
        for (System system : source.getSystems()) {
            System newSystem = new System();
            newSystem.override(system, keepMeta, suffix);
            this.systems.add(newSystem);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            newComponent.override(component, keepMeta, suffix);
            this.components.add(newComponent);
        }
    }

    public void copyNonEmpty(System source, boolean keepMeta) {
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
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                newSystem.copyNonEmpty(system, keepMeta);
                this.systems.add(newSystem);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component component : source.getComponents()) {
                Component newComponent = new Component();
                newComponent.copyNonEmpty(component, keepMeta);
                this.components.add(newComponent);
            }
        }
    }
}
