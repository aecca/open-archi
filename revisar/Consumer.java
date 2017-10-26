package com.araguacaima.gsa.persistence.am;

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
 * See <a href="https://structurizr.com/help/model#Consumer">Model - Consumer</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Consumer", schema = "AM")
public class Consumer extends Element {

    @Column
    private Location location = Location.Unspecified;

    public Consumer() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    /**
     * Gets the location of this consumer.
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

}