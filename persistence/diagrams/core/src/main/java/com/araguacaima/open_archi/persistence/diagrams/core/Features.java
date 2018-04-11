package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.*;

/**
 * Represents a feature, such as a Java class or interface,
 * that is part of the implementation of a component.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Features extends Items {

    /**
     * the role of the feature ... Primary or Supporting
     */
    @Column
    @Enumerated(EnumType.STRING)
    private FeatureRole role = FeatureRole.Supporting;

    /**
     * a URL; e.g. a reference to the feature in source code control
     */
    @Column
    private String url;

    /**
     * the visibility of the feature; e.g. public, package, private
     */
    @Column
    private String visibility;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FEATURE;

    public Features() {
    }

    public FeatureRole getRole() {
        return role;
    }

    public void setRole(FeatureRole role) {
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public void override(Features source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setRole(source.getRole());
        this.setUrl(source.getUrl());
        this.setVisibility(source.getVisibility());
    }

    public void copyNonEmpty(Features source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRole() != null) {
            this.setRole(source.getRole());
        }
        if (source.getUrl() != null) {
            this.setUrl(source.getUrl());
        }

        if (source.getVisibility() != null) {
            this.setVisibility(source.getVisibility());
        }
    }

}