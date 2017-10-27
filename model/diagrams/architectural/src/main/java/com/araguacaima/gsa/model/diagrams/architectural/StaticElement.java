package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.*;
import com.araguacaima.gsa.model.diagrams.core.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is the superclass for model elements that describe the static structure
 * of a software system, namely Person, SoftwareSystem, Container and Component.
 */
public abstract class StaticElement extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "/";
    private Model model;
    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;

    protected StaticElement() {
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Determines whether this element has afferent (incoming) relationships.
     *
     * @return true if this element has afferent relationships, false otherwise
     */
    public boolean hasAfferentRelationships() {
        return getModel().getRelationships().stream().filter(r -> ((Relationship) r).getDestination() == this).count() > 0;
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and software system.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a software system.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(SoftwareSystem destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and container.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a container.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Container destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and component.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional "uses" style relationship between this element and a component.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship uses(Component destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "sends e-mail to")
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Consumer destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination the target of the relationship
     * @param description a description of the relationship (e.g. "sends e-mail to")
     * @param technology  the technology details (e.g. JSON/HTTPS)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Consumer destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Adds a unidirectional relationship between this element and a person.
     *
     * @param destination      the target of the relationship
     * @param description      a description of the relationship (e.g. "sends e-mail to")
     * @param technology       the technology details (e.g. JSON/HTTPS)
     * @param interactionStyle the interaction style (sync vs async)
     * @return the relationship that has just been created and added to the model
     */
    public Relationship delivers(Consumer destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

    @Override
    public String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }


    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

}
