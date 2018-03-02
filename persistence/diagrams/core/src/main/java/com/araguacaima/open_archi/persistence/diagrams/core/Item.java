package com.araguacaima.open_archi.persistence.diagrams.core;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({@NamedQuery(name = Item.GET_ITEM_ID_BY_NAME,
        query = "select a "+
                "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.name=:name and a.kind=:kind"),
        @NamedQuery(name = Item.GET_ALL_CHILDREN,
                query = "select a.children " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.id=:id"),
        @NamedQuery(name = Item.GET_META_DATA,
                query = "select a.metaData " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.id=:id"),
        @NamedQuery(name = Item.GET_ALL_PROTOTYPES,
                query = "select a.metaData " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=true"),
        @NamedQuery(name = Item.GET_ALL_PROTOTYPE_NAMES,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a)) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=true"),
        @NamedQuery(name = Item.GET_ALL_DIAGRAM_NAMES,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a)) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=false")})
public class Item extends Taggable {

    public static final String GET_ALL_CHILDREN = "get.all.children";
    public static final String GET_META_DATA = "get.meta.data";
    public static final String GET_ALL_PROTOTYPES = "get.all.prototypes";
    public static final String GET_ALL_PROTOTYPE_NAMES = "get.all.prototype.names";
    public static final String GET_ALL_DIAGRAM_NAMES = "get.all.diagram.names";
    public static final String GET_ITEM_ID_BY_NAME = "get.item.id.by.name";

    @Column
    protected String name;

    @Column
    @Enumerated(EnumType.STRING)
    protected ElementKind kind;

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

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Can_Be_Connected_From_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Can_Be_Connected_From_Id",
                    referencedColumnName = "Id")})
    protected Set<ConnectTrigger> canBeConnectedFrom;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Can_Be_Connected_To_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Can_Be_Connected_To_Id",
                    referencedColumnName = "Id")})
    protected Set<ConnectTrigger> canBeConnectedTo;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected MetaData metaData;

    @Column
    protected boolean prototype;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
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

    public Set<ConnectTrigger> getCanBeConnectedFrom() {
        return canBeConnectedFrom;
    }

    public void setCanBeConnectedFrom(Set<ConnectTrigger> canBeConnectedFrom) {
        this.canBeConnectedFrom = canBeConnectedFrom;
    }

    public Set<ConnectTrigger> getCanBeConnectedTo() {
        return canBeConnectedTo;
    }

    public void setCanBeConnectedTo(Set<ConnectTrigger> canBeConnectedTo) {
        this.canBeConnectedTo = canBeConnectedTo;
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

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public void copy(Item source) {
        super.copy(source);
        this.name = source.getName();
        this.description = source.getDescription();
        this.location = source.getLocation();
        this.parent = source.getParent();
        this.children = source.getChildren();
        this.shape = source.getShape();
        this.canBeConnectedFrom = source.getCanBeConnectedFrom();
        this.canBeConnectedTo = source.getCanBeConnectedTo();
        this.metaData = source.getMetaData();
        this.prototype = source.isPrototype();
    }
}