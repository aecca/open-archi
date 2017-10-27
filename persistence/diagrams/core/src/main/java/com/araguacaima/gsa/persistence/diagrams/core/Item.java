package com.araguacaima.gsa.persistence.diagrams.core;


import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */
@Entity
@PersistenceContext(unitName = "diagrams")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item extends Taggable {

    @Column
    private String name;
    @Column
    private String description;
    @OneToOne
    private Point location;
    @OneToOne
    private Item parent;
    @OneToOne
    private Shape shape;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Pelationships",
            joinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @OneToOne
    private MetaData metaData;

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

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
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