package com.araguacaima.gsa.model.diagrams.core;

import com.araguacaima.gsa.util.Url;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * This is the superclass for all model elements.
 */
public abstract class Element<T extends Item> extends Item {

    private String url;
    private Map<String, String> properties = new HashMap<>();
    private Set<Feature> features = new LinkedHashSet<>();

    protected Element() {
    }

    /**
     * Gets the URL where more information about this element can be found.
     *
     * @return a URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this element can be found.
     *
     * @param url the URL as a String
     * @throws IllegalArgumentException if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the collection of name-value property pairs associated with this element, as a Map.
     *
     * @return a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    public void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    /**
     * Adds a name-value pair property to this element.
     *
     * @param name  the name of the property
     * @param value the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }


    protected abstract String getCanonicalNameSeparator();

    @JsonIgnore
    public String formatForCanonicalName(String name) {
        return name.replace(getCanonicalNameSeparator(), "");
    }

    public abstract ElementKind getKind();

    public abstract void setKind(ElementKind kind);

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    /**
     * Gets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @return the type, as a String
     */
    @JsonIgnore
    public String getType() {
        Optional<Feature> optional = features.stream().filter(ce -> ce.getRole() == FeatureRole.Primary).findFirst();
        if (optional.isPresent()) {
            return optional.get().getType();
        } else {
            return null;
        }
    }

    /**
     * Sets the type of this component (e.g. a fully qualified Java interface/class name).
     *
     * @param type the fully qualified type name
     * @return the Feature that was created
     * @throws IllegalArgumentException if the specified type is null
     */
    public Feature addFeature(String type) {
        Optional<Feature> optional = features.stream().filter(ce -> ce.getRole() == FeatureRole.Primary).findFirst();
        optional.ifPresent(feature -> features.remove(feature));

        Feature feature = new Feature(type);
        feature.setRole(FeatureRole.Primary);
        this.features.add(feature);

        return feature;
    }

    /**
     * Gets the set of Feature objects.
     *
     * @return a Set, which could be empty
     */
    public Set<Feature> getCode() {
        return new HashSet<>(features);
    }

    void setCode(Set<Feature> features) {
        this.features = features;
    }

    /**
     * Adds a supporting type to this Component.
     *
     * @param type the fully qualified type name
     * @return a Feature representing the supporting type
     * @throws IllegalArgumentException if the specified type is null
     */
    public Feature addSupportingType(String type) {
        Feature feature = new Feature(type);
        feature.setRole(FeatureRole.Supporting);
        this.features.add(feature);

        return feature;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Element)) {
            return false;
        }

        if (!this.getClass().equals(o.getClass())) {
            return false;
        }

        Element element = (Element) o;
        return getCanonicalName().equals(element.getCanonicalName());
    }

}