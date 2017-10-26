package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.persistence.diagrams.core.RelationshipType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * A relationship between two elements.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Relationship", schema = "AM")
public class Relationship extends Taggable {

    @Column
    protected String id = "";

    @OneToOne
    private Element source;

    @Column
    private String sourceId;

    @OneToOne
    private Element destination;

    @Column
    private String destinationId;

    @Column
    private String description;

    @Column
    private String technology;

    @Column
    private InteractionStyle interactionStyle = InteractionStyle.Synchronous;

    @Column
    private RelationshipType type;

    public Relationship() {
    }

    Relationship(Element source, Element destination, String description, String technology, InteractionStyle interactionStyle) {
        this();

        this.source = source;
        this.destination = destination;
        this.description = description;
        this.technology = technology;
        setInteractionStyle(interactionStyle);
    }

    @JsonIgnore
    public Element getSource() {
        return source;
    }

    public void setSource(Element source) {
        this.source = source;
    }

    /**
     * Gets the ID of the source element.
     *
     * @return the ID of the source element, as a String
     */
    public String getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * Gets the ID of this relationship in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public Element getDestination() {
        return destination;
    }

    public void setDestination(Element destination) {
        this.destination = destination;
    }

    /**
     * Gets the ID of the destination element.
     *
     * @return the ID of the destination element, as a String
     */
    public String getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the technology associated with this relationship (e.g. HTTPS, JDBC, etc).
     *
     * @return the technology as a String,
     * or null if a technology is not specified
     */
    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the interaction style (synchronous or asynchronous).
     *
     * @return an InteractionStyle,
     * or null if an interaction style has not been specified
     */
    public InteractionStyle getInteractionStyle() {
        return interactionStyle;
    }

    public void setInteractionStyle(InteractionStyle interactionStyle) {
        this.interactionStyle = interactionStyle;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relationship that = (Relationship) o;

        if (!getDescription().equals(that.getDescription())) return false;
        if (!getDestination().equals(that.getDestination())) return false;
        if (!getSource().equals(that.getSource())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getSourceId().hashCode();
        result = 31 * result + getDestinationId().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return source.toString() + " ---[" + description + "]---> " + destination.toString();
    }

}