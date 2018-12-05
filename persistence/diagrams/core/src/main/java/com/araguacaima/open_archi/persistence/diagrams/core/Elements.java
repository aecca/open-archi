package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is the superclass for all model elements.
 */


public abstract class Elements extends Items {

    protected String url;
    protected Set<Features> features = new LinkedHashSet<>();

    public Elements() {
    }

    /**
     * Gets the URL where more information about this element can be found.
     *
     * @return a URL as a String
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Features> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Features> features) {
        this.features = features;
    }

    public Collection<BaseEntity> override(Elements source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.override(source, keepMeta, suffix);
        if (clonedFrom != null) {
            this.setClonedFrom(clonedFrom);
        }
        this.url = source.getUrl();
        for (Features feature : source.getFeatures()) {
            Features newFeature = new Features();
            overriden.addAll(newFeature.override(feature, keepMeta, suffix, clonedFrom));
            if(!this.features.add(newFeature)) {
                this.features.remove(newFeature);
                this.features.add(newFeature);
            }
            overriden.add(newFeature);
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Elements source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getUrl() != null) {
            this.url = source.getUrl();
        }
        if (source.getFeatures() != null && !source.getFeatures().isEmpty()) {
            for (Features feature : source.getFeatures()) {
                Features newFeature = new Features();
                overriden.addAll(newFeature.copyNonEmpty(feature, keepMeta));
                if(!this.features.add(newFeature)) {
                    this.features.remove(newFeature);
                    this.features.add(newFeature);
                }
                overriden.add(newFeature);
            }
        }
        return overriden;
    }
}