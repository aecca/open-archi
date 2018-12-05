package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */

public abstract class Items extends Taggable {

    protected String name;
    protected String description;
    protected Point location;
    protected CompositeElement parent;
    protected Set<CompositeElement> children = new HashSet<>();
    protected Shape shape = new Shape();

    public Items() {
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

    public Set<CompositeElement> getChildren() {
        return children;
    }

    public void setChildren(Set<CompositeElement> children) {
        this.children = children;
    }

    public Collection<BaseEntity> override(Item source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
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

        if (source.getMetaData() != null) {
            MetaData metaData = new MetaData();
            metaData.override(source.getMetaData(), keepMeta, suffix);
        }
        Set<Relationship> relationships = source.getRelationships();
        if (relationships != null) {
            for (Relationship relationship : source.getRelationships()) {
                Relationship newRelationship = new Relationship();
                overriden.addAll(newRelationship.override(relationship, keepMeta, suffix, clonedFrom));
                overriden.add(newRelationship);
            }
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Item source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
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
            this.shape = source.getShape();
        }
        return overriden;
    }

}