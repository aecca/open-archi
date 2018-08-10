package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({@NamedQuery(name = Elements.GET_ALL_FEATURES,
        query = "select a.features from Element a where a.id=:id")})
public abstract class Elements extends Items {

    public static final String GET_ALL_FEATURES = "get.all.features_list";

    @Column
    protected String url;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Element_FeatureIds",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")})
    protected Set<Features> features = new LinkedHashSet<>();

    public Elements() {
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

    public Set<Features> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Features> features) {
        this.features = features;
    }

    public void override(Elements source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.url = source.getUrl();
        for (Features feature : source.getFeatures()) {
            Features newFeatures = new Features();
            newFeatures.override(feature, keepMeta, suffix, clonedFrom);
            this.features.add(newFeatures);
        }
    }

    public void copyNonEmpty(Elements source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getUrl() != null) {
            this.url = source.getUrl();
        }
        if (source.getFeatures() != null && !source.getFeatures().isEmpty()) {
            for (Features feature : source.getFeatures()) {
                Features newFeatures = new Features();
                newFeatures.copyNonEmpty(feature, keepMeta);
                this.features.add(newFeatures);
            }
        }
    }
}