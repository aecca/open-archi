package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

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
public class Systems extends GroupStaticElements {

    private Scope scope = Scope.Unspecified;
    private Set<Containers> containers = new LinkedHashSet<>();
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

    public Collection<BaseEntity> override(Systems source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setScope(source.getScope());
        for (Containers container : source.getContainers()) {
            Containers newContainer = new Containers();
            overriden.addAll(newContainer.override(container, keepMeta, suffix, clonedFrom));
            if (!this.containers.add(newContainer)) {
                this.containers.remove(newContainer);
                this.containers.add(newContainer);
            }
            overriden.add(newContainer);
        }

        for (Components component : source.getComponents()) {
            Components newComponent = new Components();
            overriden.addAll(newComponent.override(component, keepMeta, suffix, clonedFrom));
            if (!this.components.add(newComponent)) {
                this.components.remove(newComponent);
                this.components.add(newComponent);
            }
            overriden.add(newComponent);
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Systems source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Containers container : source.getContainers()) {
                Containers newContainer = new Containers();
                overriden.addAll(newContainer.copyNonEmpty(container, keepMeta));
                if (!this.containers.add(newContainer)) {
                    this.containers.remove(newContainer);
                    this.containers.add(newContainer);
                }
                overriden.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Components component : source.getComponents()) {
                Components newComponent = new Components();
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
