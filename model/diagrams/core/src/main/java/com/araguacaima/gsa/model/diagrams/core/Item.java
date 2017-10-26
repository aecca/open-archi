package com.araguacaima.gsa.model.diagrams.core;

import com.araguacaima.gsa.util.Url;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * This is the superclass for all model elements.
 */
public abstract class Item extends Taggable {

    private String name;
    private String description;
    private Point location;
    private Item parent;
    private Shape shape;
    private Set<Relationship> relationships = new LinkedHashSet<>();
    private MetaData metaData;

    protected Item() {
    }

    /**
     * Gets the ID of this element in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
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


    @JsonIgnore
    public String getCanonicalName() {
        return getParent().getCanonicalName() + getCanonicalNameSeparator() + formatForCanonicalName(getName());
    }

    /**
     * Gets the efferent (outgoing) relationship with the specified element.
     *
     * @param element the element to look for
     * @return a Relationship object if an efferent relationship exists, null otherwise
     */
    public Relationship getEfferentRelationshipWith(Element element) {
        if (element == null) {
            return null;
        }

        for (Relationship relationship : relationships) {
            if (relationship.getDestination().equals(element)) {
                return relationship;
            }
        }

        return null;
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
     * Gets the set of outgoing relationships.
     *
     * @return a Set of Relationship objects, or an empty set if none exist
     */
    public Set<Relationship> getRelationships() {
        return new LinkedHashSet<>(relationships);
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    /**
     * Determines whether this element has an efferent (outgoing) relationship with
     * the specified element.
     *
     * @param element the element to look for
     * @return true if this element has an efferent relationship with the specified element,
     * false otherwise
     */
    public boolean hasEfferentRelationshipWith(Item element) {
        return getEfferentRelationshipWith(element) != null;
    }

    /**
     * Gets the efferent (outgoing) relationship with the specified element.
     *
     * @param element the element to look for
     * @return a Relationship object if an efferent relationship exists, null otherwise
     */
    public Relationship getEfferentRelationshipWith(Item element) {
        if (element == null) {
            return null;
        }

        for (Relationship relationship : relationships) {
            if (relationship.getDestination().equals(element)) {
                return relationship;
            }
        }

        return null;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    protected abstract String getCanonicalNameSeparator();

    @JsonIgnore
    public String formatForCanonicalName(String name) {
        return name.replace(getCanonicalNameSeparator(), "");
    }

    public boolean has(Relationship relationship) {
        return relationships.contains(relationship);
    }

    public boolean addRelationship(Relationship<? extends Item> relationship) {
        return relationships.add((Relationship)relationship);
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public abstract ElementKind getKind();

    public abstract void setKind(ElementKind kind);

    @Override
    public String toString() {
        return "{" + getId() + " | " + getName() + " | " + getDescription() + "}";
    }

    /**
     * Gets the parent of this element.
     *
     * @return the parent Element, or null if this element doesn't have a parent (i.e. a Person or SoftwareSystem)
     */

    @JsonIgnore
    public Item getParent() {
        return parent;
    }

    @JsonIgnore
    public void setParent(Item parent) {
        this.parent = parent;
    }

       @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Item)) {
            return false;
        }

        if (!this.getClass().equals(o.getClass())) {
            return false;
        }

        Item element = (Item) o;
        return this.equals(element);
    }

    public MetaData getMetaData() {
        return this.metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

}