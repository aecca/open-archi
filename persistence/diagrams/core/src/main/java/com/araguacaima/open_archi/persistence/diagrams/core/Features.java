package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.diagrams.core.reliability.Volumetry;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

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
     * the category of feature; e.g. class, interface, etc
     */
    @Column
    private FeatureCategory category;

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

    public FeatureCategory getCategory() {
        return category;
    }

    public void setCategory(FeatureCategory category) {
        this.category = category;
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
}