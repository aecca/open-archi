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
@NamedQueries({
        @NamedQuery(name = Container.GET_ALL_CONTAINERS,
                query = "select a from Container a"),
        @NamedQuery(name = Container.GET_CONTAINERS_USAGE_BY_ELEMENT_ID_LIST,
                query = "select c from Container c left join c.components co where co.id in :" + Item.ELEMENTS_USAGE_PARAM)})
public class Container extends GroupStaticElement implements DiagramableElement<Container> {

    public static final String GET_ALL_CONTAINERS = "get.all.containers";
    public static final String GET_ALL_COMPONENTS_FROM_CONTAINER = "get.all.components.from.container";
    public static final String GET_CONTAINERS_USAGE_BY_ELEMENT_ID_LIST = "get.containers.usage.by.element.id.list";

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

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Container_Containers",
            joinColumns = {@JoinColumn(name = "Container_Parent_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

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

    public Set<Container> getContainers() {
        return containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    @Override
    public Collection<BaseEntity> override(Container source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setTechnology(source.getTechnology());
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            overriden.addAll(newComponent.override(component, keepMeta, suffix, clonedFrom));
            this.components.add(newComponent);
            overriden.add(newComponent);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            overriden.addAll(newContainer.override(container, keepMeta, suffix, clonedFrom));
            this.containers.add(newContainer);
            overriden.add(newContainer);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Container source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component component : source.getComponents()) {
                Component newComponent = new Component();
                overriden.addAll(newComponent.copyNonEmpty(component, keepMeta));
                this.components.add(newComponent);
                overriden.add(newComponent);
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
        return overriden;
    }

}
