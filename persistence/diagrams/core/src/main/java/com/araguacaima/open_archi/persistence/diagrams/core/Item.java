package com.araguacaima.open_archi.persistence.diagrams.core;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({@NamedQuery(name = Item.GET_ALL_CHILDREN,
        query = "select a.children from Item a where a.id=:id"),
        @NamedQuery(name = Item.GET_META_DATA,
                query = "select a.metaData from Item a where a.id=:id")})
public class Item extends Taggable {

    public static final String GET_ALL_CHILDREN = "get.all.children";
    public static final String GET_META_DATA = "get.meta.data";

    @Column
    protected String name;
    @Column
    protected String description;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected Point location;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected CompositeElement parent;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Children_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Child_Id",
                    referencedColumnName = "Id")})
    protected Set<CompositeElement> children = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected Shape shape;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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