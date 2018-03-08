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
public class Elements extends Items {

    public static final String GET_ALL_FEATURES = "get.all.features_list";

    @Column
    protected String url;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Element_FeatureIds",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
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


    public void override(Elements source) {
        super.override(source);
        this.url = source.getUrl();
        this.features = source.getFeatures();
    }

    public void copyNonEmpty(Elements source) {
        super.copyNonEmpty(source);
        if (source.getUrl() != null) {
            this.url = source.getUrl();
        }
        if (source.getFeatures() != null && !source.getFeatures().isEmpty()) {
            this.features = source.getFeatures();
        }
    }
}