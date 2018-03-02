package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({@NamedQuery(name = Element.GET_ALL_FEATURES,
        query = "select a.features from Element a where a.id=:id")})
public class Element extends Item {

    public static final String GET_ALL_FEATURES = "get.all.features";
    @Column
    protected String url;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @CollectionTable(schema = "DIAGRAMS", name = "Element_Properties", joinColumns = @JoinColumn(name = "Property_Id"))
    protected Map<String, String> properties = new HashMap<>();

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Element_FeatureIds",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")})
    protected Set<Feature> features = new LinkedHashSet<>();

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

    public void copy(Element source) {
        super.copy(source);
        this.url = source.getUrl();
        this.properties = source.getProperties();
        this.features = source.getFeatures();
    }
}