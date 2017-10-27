package com.araguacaima.gsa.persistence.diagrams.core;

import javax.persistence.*;

/**
 * Represents a feature, such as a Java class or interface,
 * that is part of the implementation of a component.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Feature extends Item {

    /**
     * the role of the feature ... Primary or Supporting
     */
    @Column
    private FeatureRole role = FeatureRole.Supporting;

    /**
     * the fully qualified type of the feature
     **/
    @Column
    private String type;

    /**
     * a URL; e.g. a reference to the feature in source code control
     */
    @Column
    private String url;

    /**
     * the programming language used to create the feature
     */
    @Column
    private String language;

    /**
     * the category of feature; e.g. class, interface, etc
     */
    @Column
    private String category;

    /**
     * the visibility of the feature; e.g. public, package, private
     */
    @Column
    private String visibility;

    /**
     * the size of the feature; e.g. the number of lines
     */
    @Column
    private long size;

    private ElementKind kind = ElementKind.FEATURE;

    public Feature() {
    }

    public FeatureRole getRole() {
        return role;
    }

    public void setRole(FeatureRole role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}