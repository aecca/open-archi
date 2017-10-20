package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.util.Url;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public abstract class Element extends Taggable {

    static final String CANONICAL_NAME_SEPARATOR = "/";

    @OneToOne
    private Model model;

    @OneToOne
    private MetaData metaData;

    @Column
    private String id = "";

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String url;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @CollectionTable(schema = "AM", name = "Element_Properties", joinColumns = @JoinColumn(name = "Property_Id"))
    private Map<String, String> properties = new HashMap<>();

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Element_Relationships",
            joinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    protected Element() {
    }

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    protected void setModel(Model model) {
        this.model = model;
    }

    /**
     * Gets the ID of this element in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of this element.
     *
     * @return the name, as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this element.
     *
     * @param name the name, as a String
     */
    public void setName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The name of an element must not be null or empty.");
        }

        this.name = name;
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

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = properties;
        }
    }

    /**
     * Gets a description of this element.
     *
     * @return the description, as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this element.
     *
     * @param description the description, as a String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the parent of this element.
     *
     * @return the parent Element, or null if this element doesn't have a parent (i.e. a Person or SoftwareSystem)
     */
    public abstract Element getParent();

    /**
     * Gets the set of outgoing relationships.
     *
     * @return a Set of Relationship objects, or an empty set if none exist
     */
    public Set<Relationship> getRelationships() {
        return new LinkedHashSet<>(relationships);
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
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
        return getName().equals(element.getName());
    }

}