package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A system is the highest level of abstraction and describes something
 * that delivers value to its users, whether they are human or not. This includes
 * the system you are modelling, and the other systems upon
 * which your system depends.
 * <p>
 * See <a href="https://structurizr.com/help/model#System">Model - System System</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = System.GET_ALL_SYSTEMS,
                query = "select a from System a"),
        @NamedQuery(name = System.GET_ALL_SYSTEMS_FROM_SYSTEM,
                query = "select a.systems from System a where a.id=:id"),
        @NamedQuery(name = System.GET_ALL_COMPONENTS_FROM_SYSTEM,
                query = "select a.components from System a where a.id=:id"),
        @NamedQuery(name = System.GET_ALL_CONTAINERS_FROM_SYSTEM,
                query = "select a.containers from System a where a.id=:id"),
        @NamedQuery(name = System.GET_CONTAINER,
                query = "select c from System a JOIN a.containers c where a.id=:id and c.id=:cid"),
        @NamedQuery(name = System.GET_SYSTEMS_USAGE_BY_ELEMENT_ID_LIST,
                query = "select s " +
                        "from System s " +
                        "   left join s.systems sys " +
                        "   left join s.containers con " +
                        "   left join s.components com " +
                        "where sys.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or con.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or com.id in :" + Item.ELEMENTS_USAGE_PARAM)})
public class System extends GroupStaticElement implements DiagramableElement<System> {

    public static final String GET_ALL_SYSTEMS_FROM_SYSTEM = "get.all.systems.from.system";
    public static final String GET_ALL_COMPONENTS_FROM_SYSTEM = "get.all.components.from.system";
    public static final String GET_ALL_SYSTEMS = "get.all.systems";
    public static final String GET_ALL_CONTAINERS_FROM_SYSTEM = "get.all.containers.from.system";
    public static final String GET_CONTAINER = "get.container";
    public static final String GET_SYSTEMS_USAGE_BY_ELEMENT_ID_LIST = "get.systems.usage.by.element.id.list";

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;


    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Systems",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Child_System_Id",
                    referencedColumnName = "Id")})
    private Set<System> systems = new LinkedHashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Containers",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Components",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

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

    @Override
    public Collection<BaseEntity> override(System source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setScope(source.getScope());
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            overriden.addAll(newContainer.override(container, keepMeta, suffix, clonedFrom));
            if (!this.containers.add(newContainer)) {
                this.containers.remove(newContainer);
                this.containers.add(newContainer);
            }
            overriden.add(newContainer);
        }
        for (System system : source.getSystems()) {
            System newSystem = new System();
            overriden.addAll(newSystem.override(system, keepMeta, suffix, clonedFrom));
            if (!this.systems.add(newSystem)) {
                this.systems.remove(newSystem);
                this.systems.add(newSystem);
            }
            overriden.add(newSystem);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            overriden.addAll(newComponent.override(component, keepMeta, suffix, clonedFrom));
            if (!this.components.add(newComponent)) {
                this.components.remove(newComponent);
                this.components.add(newComponent);
            }
            overriden.add(newComponent);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(System source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container container : source.getContainers()) {
                Container newContainer = new Container();
                overriden.addAll(newContainer.copyNonEmpty(container, keepMeta));
                if (!this.containers.add(newContainer)) {
                    this.containers.remove(newContainer);
                    this.containers.add(newContainer);
                }
                overriden.add(newContainer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                overriden.addAll(newSystem.copyNonEmpty(system, keepMeta));
                if (!this.systems.add(newSystem)) {
                    this.systems.remove(newSystem);
                    this.systems.add(newSystem);
                }
                overriden.add(newSystem);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component component : source.getComponents()) {
                Component newComponent = new Component();
                overriden.addAll(newComponent.copyNonEmpty(component, keepMeta));
                if (!this.components.add(newComponent)) {
                    this.components.remove(newComponent);
                    this.components.add(newComponent);
                }
                overriden.add(newComponent);
            }
        }
        return overriden;
    }
}
