package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;

import javax.persistence.*;
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
public class Systems extends GroupStaticElements {

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Containers",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Containers> containers = new LinkedHashSet<>();


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Components",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Components> components = new LinkedHashSet<>();

    public Systems() {
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<Containers> getContainers() {
        return containers;
    }

    public void setContainers(Set<Containers> containers) {
        this.containers = containers;
    }

    public Set<Components> getComponents() {
        return components;
    }

    public void setComponents(Set<Components> components) {
        this.components = components;
    }

    public void override(Systems source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setScope(source.getScope());
        for (Containers container : source.getContainers()) {
            Containers newContainer = new Containers();
            newContainer.override(container, keepMeta, suffix, clonedFrom);
            this.containers.add(newContainer);
        }

        for (Components component : source.getComponents()) {
            Components newComponent = new Components();
            newComponent.override(component, keepMeta, suffix, clonedFrom);
            this.components.add(newComponent);
        }
    }

    public void copyNonEmpty(Systems source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Containers container : source.getContainers()) {
                Containers newContainer = new Containers();
                newContainer.copyNonEmpty(container, keepMeta);
                this.containers.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Components component : source.getComponents()) {
                Components newComponent = new Components();
                newComponent.copyNonEmpty(component, keepMeta);
                this.components.add(newComponent);
            }
        }
    }
}
