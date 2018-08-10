package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = Layer.GET_ALL_LAYERS,
                query = "select l from Layer l"),
        @NamedQuery(name = Layer.GET_LAYER,
                query = "select l from Layer l where l.id=:lid")})
public class Layer extends GroupStaticElement {

    public static final String GET_ALL_LAYERS = "get.all.layers";
    public static final String GET_LAYER = "get.layer";

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Layer_Systems",
            joinColumns = {@JoinColumn(name = "Layer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")})
    private Set<System> systems = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Layer_Containers",
            joinColumns = {@JoinColumn(name = "Layer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Layer_Components",
            joinColumns = {@JoinColumn(name = "Layer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

    public Layer() {
        setKind(ElementKind.LAYER);
    }

    public Set<System> getSystems() {
        return systems;
    }

    public void setSystems(Set<System> systems) {
        this.systems = systems;
    }

    public Set<Container> getContainers() {
        return containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public void override(Layer source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        for (System system : source.getSystems()) {
            System newSystem = new System();
            newSystem.override(system, keepMeta, suffix, clonedFrom);
            this.systems.add(newSystem);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            newContainer.override(container, keepMeta, suffix, clonedFrom);
            this.containers.add(newContainer);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            newComponent.override(component, keepMeta, suffix, clonedFrom);
            this.components.add(newComponent);
        }
    }

    public void copyNonEmpty(Layer source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                newSystem.copyNonEmpty(system, keepMeta);
                this.systems.add(newSystem);
            }
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container container : source.getContainers()) {
                Container newContainer = new Container();
                newContainer.copyNonEmpty(container, keepMeta);
                this.containers.add(newContainer);
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
