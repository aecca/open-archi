package com.araguacaima.gsa.persistence.diagrams.core;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "gsa")
public class Item extends Taggable {

    protected String name;
    @Column
    protected String description;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    protected Point location;

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    protected CompositeElement parent;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Children_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Child_Id",
                    referencedColumnName = "Id")})
    protected Set<CompositeElement> children = new HashSet<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    protected Shape shape;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Relationships",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    protected Set<Relationship> relationships = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    protected MetaData metaData;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public CompositeElement getParent() {
        return parent;
    }

    public void setParent(CompositeElement parent) {
        this.parent = parent;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Set<CompositeElement> getChildren() {
        return children;
    }

    public void setChildren(Set<CompositeElement> children) {
        this.children = children;
    }
}