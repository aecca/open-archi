package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Feature;
import com.araguacaima.gsa.model.diagrams.core.FeatureRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The word "component" is a hugely overloaded term in the software development
 * industry, but in this context a component is simply a grouping of related
 * functionality encapsulated behind a well-defined interface. If you're using a
 * language like Java or C#, the simplest way to think of a component is that
 * it's a collection of implementation classes behind an interface. Aspects such
 * as how those components are packaged (e.g. one component vs many components
 * per JAR file, DLL, shared library, etc) is a separate and orthogonal concern.
 */
public class Component extends StacticElement {

    private String technology;
    private Set<Feature> features = new HashSet<>();
    private long size;

    Component() {
    }


    /**
     * Gets the technology associated with this component (e.g. "Spring Bean").
     *
     * @return the technology, as a String,
     * or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Sets the technology associated with this component (e.g. "Spring Bean").
     *
     * @param technology the technology, as a String
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }



    /**
     * Gets the size of this Component (e.g. number of lines).
     *
     * @return the size of this component, as a long
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of this component (e.g. number of lines).
     *
     * @param size the size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Gets the Java package of this component (i.e. the package of the primary feature).
     *
     * @return the package name, as a String
     */
    @JsonIgnore
    public String getPackage() {
        if (getType() != null) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(getType()).getPackage().getName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return build(Tag.ELEMENT, Tag.COMPONENT);
    }

}