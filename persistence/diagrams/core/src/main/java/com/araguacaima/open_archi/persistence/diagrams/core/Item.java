package com.araguacaima.open_archi.persistence.diagrams.core;


import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

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
                query = "select a " +
                        "from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.prototype=true order by a.kind, a.shape.type, a.name"),
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
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.kind='CONSUMER'"),
        @NamedQuery(name = Item.GET_ITEMS_BY_NAME_AND_KIND,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.kind=:kind and a.name=:name"),
        @NamedQuery(name = Item.GET_ITEMS_BY_NAME,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.name=:name"),
        @NamedQuery(name = Item.GET_ALL_MODELS,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a "),
        @NamedQuery(name = Item.GET_MODELS_BY_TYPE,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a where TYPE(a)=:modelType"),
        @NamedQuery(name = Item.GET_MODELS_BY_STATUS,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.core.Item a where a.status=:status")
})
public abstract class Item extends Taggable {

    public static final String GET_ALL_MODELS = "get.all.models";
    public static final String GET_MODELS_BY_TYPE = "get.models.by.type";
    public static final String GET_MODELS_BY_STATUS = "get.models.by.status";
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
    public static final String GET_ITEMS_BY_NAME = "get.items.by.name";
    public static final String ELEMENTS_USAGE_PARAM = "elementIds";

    @Column
    protected String name;

    @Column
    @Enumerated(EnumType.STRING)
    protected ElementKind kind = ElementKind.ITEM;

    @Column
    protected String description;

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

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Item_Relationships",
            joinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Relationship> relationships = new LinkedHashSet<>();

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

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    public Collection<BaseEntity> override(Item source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.name = StringUtils.isNotBlank(suffix) ? source.getName() + " " + suffix : source.getName();
        this.description = source.getDescription();
        this.parent = source.getParent();
        this.children = source.getChildren();
        if (source.getShape() != null) {
            Shape shape = new Shape();
            shape.override(source.getShape(), keepMeta, suffix);
            this.shape = shape;
            overriden.add(shape);
        } else {
            this.shape = null;
        }
        if (source.getImage() != null) {
            Image image = new Image();
            image.override(source.getImage(), keepMeta, suffix);
            this.image = image;
            overriden.add(image);
        } else {
            this.image = null;
        }
        this.canBeConnectedFrom = source.getCanBeConnectedFrom();
        this.canBeConnectedTo = source.getCanBeConnectedTo();
        if (source.getMetaData() != null) {
            MetaData metaData = new MetaData();
            metaData.override(source.getMetaData(), keepMeta, suffix);
            this.metaData = source.getMetaData();
            overriden.add(metaData);
        }
        Set<Relationship> relationships = source.getRelationships();
        if (relationships != null) {
            for (Relationship relationship : source.getRelationships()) {
                Relationship newRelationship = new Relationship();
                overriden.addAll(newRelationship.override(relationship, keepMeta, suffix, clonedFrom));
                if(!this.relationships.add(newRelationship)) {
                    this.relationships.remove(newRelationship);
                    this.relationships.add(newRelationship);
                }
                overriden.add(newRelationship);
            }
        }
        this.prototype = source.isPrototype();
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Item source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.name = source.getName();
        }
        if (source.getDescription() != null) {
            this.description = source.getDescription();
        }
        if (source.getParent() != null) {
            this.parent = source.getParent();
        }
        if (source.getChildren() != null && !source.getChildren().isEmpty()) {
            this.children = source.getChildren();
        }
        if (source.getShape() != null) {
            Shape shape = this.shape;
            if (shape == null) {
                shape = new Shape();
            }
            shape.copyNonEmpty(source.getShape(), keepMeta);
            this.setShape(shape);
            overriden.add(shape);
        }
        if (source.getImage() != null) {
            Image image = new Image();
            image.copyNonEmpty(source.getImage(), keepMeta);
            this.image = image;
            overriden.add(image);
        }
        if (source.getCanBeConnectedFrom() != null && !source.getCanBeConnectedFrom().isEmpty()) {
            this.canBeConnectedFrom = source.getCanBeConnectedFrom();
        }
        if (source.getCanBeConnectedTo() != null && !source.getCanBeConnectedTo().isEmpty()) {
            this.canBeConnectedTo = source.getCanBeConnectedTo();
        }
        if (source.getMetaData() != null) {
            MetaData metaData = this.metaData;
            if (metaData == null) {
                metaData = new MetaData();
            }
            metaData.copyNonEmpty(source.getMetaData(), keepMeta);
            this.setMetaData(metaData);
            overriden.add(metaData);
        }
        this.prototype = source.isPrototype();
        Set<Relationship> relationships = source.getRelationships();
        if (relationships != null && !relationships.isEmpty()) {
            for (Relationship relationship : relationships) {
                Relationship newRelationship = new Relationship();
                overriden.addAll(newRelationship.copyNonEmpty(relationship, keepMeta));
                if(!this.relationships.add(newRelationship)) {
                    this.relationships.remove(newRelationship);
                    this.relationships.add(newRelationship);
                }
                overriden.add(newRelationship);
            }
        }
        return overriden;
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