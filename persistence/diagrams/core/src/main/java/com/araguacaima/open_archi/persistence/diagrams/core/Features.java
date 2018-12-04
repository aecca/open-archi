package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a feature, such as a Java class or interface,
 * that is part of the implementation of a component.
 */

public class Features extends Items {

    /**
     * the type of the feature ... Primary or Supporting
     */
    private FeatureType type = FeatureType.Primary;

    /**
     * a URL; e.g. a reference to the feature in source code control
     */
    private String url;

    /**
     * the visibility of the feature; e.g. public, package, private
     */
    private String visibility;

    private ElementKind kind = ElementKind.FEATURE;

    public Features() {
    }

    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
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

    public Collection<BaseEntity> override(Features source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setRole(source.getRole());
        this.setUrl(source.getUrl());
        this.setVisibility(source.getVisibility());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Features source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getRole() != null) {
            this.setRole(source.getRole());
        }
        if (source.getUrl() != null) {
            this.setUrl(source.getUrl());
        }

        if (source.getVisibility() != null) {
            this.setVisibility(source.getVisibility());
        }
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}