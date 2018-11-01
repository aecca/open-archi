package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = Layer.GET_ALL_LAYERS,
                query = "select l from Layer l"),
        @NamedQuery(name = Layer.GET_LAYER,
                query = "select l from Layer l where l.id=:lid"),
        @NamedQuery(name = Layer.GET_LAYERS_USAGE_BY_ELEMENT_ID_LIST,
                query = "select l " +
                        "from Layer l " +
                        "   left join l.systems sys " +
                        "   left join l.containers con " +
                        "   left join l.components com " +
                        "where sys.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or con.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or com.id in :" + Item.ELEMENTS_USAGE_PARAM)})
public class Layer extends GroupStaticElement implements DiagramableElement<Layer> {

    public static final String GET_ALL_LAYERS = "get.all.layers";
    public static final String GET_LAYER = "get.layer";
    public static final String GET_LAYERS_USAGE_BY_ELEMENT_ID_LIST = "get.layers.usage.by.element.id.list";

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

    @Override
    public Collection<BaseEntity> override(Layer source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (System system : source.getSystems()) {
            System newSystem = new System();
            overriden.addAll(newSystem.override(system, keepMeta, suffix, clonedFrom));
            this.systems.add(newSystem);
            overriden.add(newSystem);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            overriden.addAll(newContainer.override(container, keepMeta, suffix, clonedFrom));
            this.containers.add(newContainer);
            overriden.add(newContainer);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            overriden.addAll(newComponent.override(component, keepMeta, suffix, clonedFrom));
            this.components.add(newComponent);
            overriden.add(newComponent);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Layer source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                overriden.addAll(newSystem.copyNonEmpty(system, keepMeta));
                this.systems.add(newSystem);
                overriden.add(newSystem);
            }
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container container : source.getContainers()) {
                Container newContainer = new Container();
                overriden.addAll(newContainer.copyNonEmpty(container, keepMeta));
                this.containers.add(newContainer);
                overriden.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component component : source.getComponents()) {
                Component newComponent = new Component();
                overriden.addAll(newComponent.copyNonEmpty(component, keepMeta));
                this.components.add(newComponent);
                overriden.add(newComponent);
            }
        }
        return overriden;
    }
}
