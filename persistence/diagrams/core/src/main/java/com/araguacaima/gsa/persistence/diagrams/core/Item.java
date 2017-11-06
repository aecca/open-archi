package com.araguacaima.gsa.persistence.diagrams.core;


import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "gsa" )
public class Item extends Taggable {

    protected String name;
    @Column
    protected String description;
    @OneToOne
    protected Point location;
    @OneToOne(targetEntity = Taggable.class)
    protected Taggable parent;
    @OneToOne
    protected Shape shape;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Relationships",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    protected Set<Relationship> relationships = new LinkedHashSet<>();

    @OneToOne
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

    public Taggable getParent() {
        return parent;
    }

    public void setParent(Taggable parent) {
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
}