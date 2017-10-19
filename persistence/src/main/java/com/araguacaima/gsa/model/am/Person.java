package com.araguacaima.gsa.model.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * However you think about your users (as actors, roles, personas, etc),
 * people are the various human users of your software system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Person">Model - Person</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Person", schema = "AM")
public class Person extends StaticStructureElement {

    @Column
    private Location location = Location.Unspecified;

    public Person() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    /**
     * Gets the location of this person.
     *
     * @return a Location
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    @Override
    public String getCanonicalName() {
        return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.PERSON));
    }

    @Override
    public Relationship delivers(Person destination, String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Person destination, String description, String technology) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        throw new UnsupportedOperationException();
    }

    public Relationship interactsWith(Person destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    public Relationship interactsWith(Person destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    public Relationship interactsWith(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

}