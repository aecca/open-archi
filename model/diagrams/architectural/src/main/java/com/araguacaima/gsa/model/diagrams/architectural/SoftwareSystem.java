package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class SoftwareSystem extends StacticElement {

    private Scope scope = Scope.Unspecified;

    private Set<Container> containers = new LinkedHashSet<>();

    SoftwareSystem() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    /**
     * Gets the location of this software system.
     *
     * @return a Scope
     */
    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        if (scope != null) {
            this.scope = scope;
        } else {
            this.scope = Scope.Unspecified;
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

    /**
     * Adds a container with the specified name, description and technology
     * (unless one exists with the same name already).
     *
     * @param name        the name of the container (e.g. "Web Application")
     * @param description a short description/list of responsibilities
     * @param technology  the technoogy choice (e.g. "Spring MVC", "Java EE", etc)
     * @return the newly created Container instance added to the model (or null)
     */
    public Container addContainer(String name, String description, String technology) {
        return getModel().addContainer(this, name, description, technology);
    }

    /**
     * @param name the name of the {@link Container}
     * @return the container with the specified name (or null if it doesn't exist).
     */
    public Container getContainerWithName(String name) {
        for (Container container : getContainers()) {
            if (container.getName().equals(name)) {
                return container;
            }
        }

        return null;
    }

    /**
     * @param id the {@link Container#getId()} of the container
     * @return Gets the container with the specified ID (or null if it doesn't exist).
     */
    public Container getContainerWithId(String id) {
        for (Container container : getContainers()) {
            if (container.getId().equals(id)) {
                return container;
            }
        }

        return null;
    }

    @Override
    public String getCanonicalName() {
        return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    public String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }

    @Override
    protected Set<String> getRequiredTags() {
        return build(Tag.ELEMENT, Tag.SOFTWARE_SYSTEM);
    }

}
