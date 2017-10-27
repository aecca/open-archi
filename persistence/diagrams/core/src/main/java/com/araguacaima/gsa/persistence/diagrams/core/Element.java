package com.araguacaima.gsa.persistence.diagrams.core;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Element extends Item {

    @Column
    private String url;
    private Map<String, String> properties = new HashMap<>();
    private Set<Feature> features = new LinkedHashSet<>();

    public Element() {
    }

    /**
     * Gets the URL where more information about this element can be found.
     *
     * @return a URL as a String
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }
}