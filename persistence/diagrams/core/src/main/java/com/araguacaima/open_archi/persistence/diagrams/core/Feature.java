package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.diagrams.core.reliability.Constraint;

import javax.persistence.*;
import java.util.Set;

/**
 * Represents a feature, such as a Java class or interface,
 * that is part of the implementation of a component.
 */

@Entity
@PersistenceUnit(unitName = "open-archi")
public class Feature extends Item {

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
    @OneToOne(targetEntity = Category.class)
    private Category category;

    /**
     * the visibility of the feature; e.g. public, package, private
     */
    @Column
    private String visibility;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Feature_Incoming_Constraint",
            joinColumns = {@JoinColumn(name = "Feature_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Incoming_Constraint_Id",
                    referencedColumnName = "Id")})
    private Set<Constraint> incomingConstraints;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Feature_Outgoing_Constraint",
            joinColumns = {@JoinColumn(name = "Feature_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Outgoing_Constraint_Id",
                    referencedColumnName = "Id")})
    private Set<Constraint> outgoingConstraints;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FEATURE;

    public Feature() {
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

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(ItemCategory category) {
        this.category = (Category) category;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Set<Constraint> getIncomingConstraints() {
        return incomingConstraints;
    }

    public void setIncomingConstraints(Set<Constraint> incomingConstraints) {
        this.incomingConstraints = incomingConstraints;
    }

    public Set<Constraint> getOutgoingConstraints() {
        return outgoingConstraints;
    }

    public void setOutgoingConstraints(Set<Constraint> outgoingConstraints) {
        this.outgoingConstraints = outgoingConstraints;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public void override(Feature source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setRole(source.getRole());
        this.setUrl(source.getUrl());
        this.setCategory(source.getCategory());
        this.setVisibility(source.getVisibility());
        this.setIncomingConstraints(source.getIncomingConstraints());
        this.setOutgoingConstraints(source.getOutgoingConstraints());
    }

    public void copyNonEmpty(Feature source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRole() != null) {
            this.setRole(source.getRole());
        }
        if (source.getUrl() != null) {
            this.setUrl(source.getUrl());
        }
        if (source.getCategory() != null) {
            this.setCategory(source.getCategory());
        }
        if (source.getVisibility() != null) {
            this.setVisibility(source.getVisibility());
        }
        if (source.getIncomingConstraints() != null && !source.getIncomingConstraints().isEmpty()) {
            this.setIncomingConstraints(source.getIncomingConstraints());
        }
        if (source.getOutgoingConstraints() != null && !source.getOutgoingConstraints().isEmpty()) {
            this.setOutgoingConstraints(source.getOutgoingConstraints());
        }
    }
}