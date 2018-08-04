package com.araguacaima.open_archi.persistence.diagrams.core;


import org.apache.commons.lang3.StringUtils;
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
        query = "select a " +
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
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=true"),
        @NamedQuery(name = Item.GET_ALL_CONSUMER_NAMES,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.kind=Consumer"),
        @NamedQuery(name = Item.GET_ALL_DIAGRAM_NAMES,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=false"),
        @NamedQuery(name = Item.GET_ALL_MODEL_NAMES_BY_TYPE,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where TYPE(a)=:type"),
        @NamedQuery(name = Item.GET_ALL_PROTOTYPE_NAMES_BY_TYPE,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where TYPE(a)=:type " +
                        "and a.prototype=true"),
        @NamedQuery(name = Item.GET_ALL_NON_CLONED_PROTOTYPE_NAMES_BY_TYPE,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where TYPE(a)=:type " +
                        "and a.prototype=true and a.clonedFrom IS NULL"),
        @NamedQuery(name = Item.GET_MODEL_NAMES_BY_NAME_AND_TYPE,
                query = "select new com.araguacaima.open_archi.persistence.commons.IdName(a.id, a.name, TYPE(a), a.kind) " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.name like concat(:name,'%') and TYPE(a)=:type "),
        @NamedQuery(name = Item.GET_ALL_CONSUMERS,
                query = "select a " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.kind='CONSUMER'"),
        @NamedQuery(name = Item.GET_ITEMS_BY_NAME_AND_KIND,
                query = "select a " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.kind=:kind and a.name=:name")})
public class Item extends Taggable {

    public static final String GET_ALL_CHILDREN = "get.all.children";
    public static final String GET_META_DATA = "get.meta.data";
    public static final String GET_ALL_PROTOTYPES = "get.all.prototypes";
    public static final String GET_ALL_PROTOTYPE_NAMES = "get.all.prototype.names";
    public static final String GET_ALL_DIAGRAM_NAMES = "get.all.diagram.names";
    public static final String GET_ITEM_ID_BY_NAME = "get.item.id.by.name";
    public static final String GET_ALL_CONSUMER_NAMES = "get.all.consumer.names";
    public static final String GET_ALL_CONSUMERS = "get.all.consumers";
    public static final String GET_MODEL_NAMES_BY_NAME_AND_TYPE = "get.model.names.by.name.and.type";
    public static final String GET_ALL_MODEL_NAMES_BY_TYPE = "get.all.model.names.by.type";
    public static final String GET_ALL_PROTOTYPE_NAMES_BY_TYPE = "get.all.prototype.names.by.type";
    public static final String GET_ALL_NON_CLONED_PROTOTYPE_NAMES_BY_TYPE = "get.all.non.cloned.prototype.names.by.type";
    public static final String GET_ITEMS_BY_NAME_AND_KIND = "get.items.by.name.and.kind";

    public static final String PROTOTYPE_SHAPE_COLOR = "#E00000";

    @Column
    protected String name;

    @OneToOne(targetEntity = DefaultCategory.class)
    protected ItemCategory category;

    @Column
    @Enumerated(EnumType.STRING)
    protected ElementKind kind;

    @Column
    protected String description;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected Point location;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    protected CompositeElement parent;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Children_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Child_Id",
                    referencedColumnName = "Id")})
    protected Set<CompositeElement> children = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected Shape shape;


    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    protected Image image;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Can_Be_Connected_From_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Can_Be_Connected_From_Id",
                    referencedColumnName = "Id")})
    protected Set<ConnectTrigger> canBeConnectedFrom;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Can_Be_Connected_To_Ids",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Can_Be_Connected_To_Id",
                    referencedColumnName = "Id")})
    protected Set<ConnectTrigger> canBeConnectedTo;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public void override(Item source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.name = StringUtils.isNotBlank(suffix) ? source.getName() + " " + suffix : source.getName();
        this.description = source.getDescription();
        this.location = source.getLocation();
        this.parent = source.getParent();
        this.children = source.getChildren();
        if (source.getShape() != null) {
            Shape shape = new Shape();
            shape.override(source.getShape(), keepMeta, suffix);
            this.shape = shape;
        }
        this.image = source.getImage();
        this.canBeConnectedFrom = source.getCanBeConnectedFrom();
        this.canBeConnectedTo = source.getCanBeConnectedTo();
        if (source.getMetaData() != null) {
            MetaData metaData = new MetaData();
            metaData.override(source.getMetaData(), keepMeta, suffix);
            this.metaData = source.getMetaData();
        }
        this.prototype = source.isPrototype();
    }

    public void copyNonEmpty(Item source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.name = source.getName();
        }
        if (source.getDescription() != null) {
            this.description = source.getDescription();
        }
        if (source.getLocation() != null) {
            this.location = source.getLocation();
        }
        if (source.getParent() != null) {
            this.parent = source.getParent();
        }
        if (source.getChildren() != null && !source.getChildren().isEmpty()) {
            this.children = source.getChildren();
        }
        if (source.getShape() != null) {
            Shape shape = new Shape();
            shape.copyNonEmpty(source.getShape(), keepMeta);
            this.shape = shape;
        }
        if (source.getImage() != null) {
            this.image = source.getImage();
        }
        if (source.getCanBeConnectedFrom() != null && !source.getCanBeConnectedFrom().isEmpty()) {
            this.canBeConnectedFrom = source.getCanBeConnectedFrom();
        }
        if (source.getCanBeConnectedTo() != null && !source.getCanBeConnectedTo().isEmpty()) {
            this.canBeConnectedTo = source.getCanBeConnectedTo();
        }
        if (source.getMetaData() != null) {
            MetaData metaData = new MetaData();
            metaData.copyNonEmpty(source.getMetaData(), keepMeta);
            this.metaData = source.getMetaData();
        }
        this.prototype = source.isPrototype();

    }

    public CompositeElement buildCompositeElement() {
        CompositeElement compositeElement = new CompositeElement();
        compositeElement.setId(this.getId());
        compositeElement.setType(this.getKind());
        compositeElement.setLink("/models/" + this.getId());
        compositeElement.setVersion(this.getMeta().getActiveVersion());
        return compositeElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return (name != null ? name.equals(item.name) : item.name == null)
                && kind == item.kind
                && (description != null ? description.equals(item.description) : item.description == null);
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (StringUtils.isNotBlank(name)) {
            result = name.hashCode();
        }
        result = 31 * result + kind.hashCode();
        return result;
    }
}