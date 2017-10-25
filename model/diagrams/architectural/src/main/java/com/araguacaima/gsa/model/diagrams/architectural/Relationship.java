package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.Item;
import com.araguacaima.gsa.model.diagrams.core.RelationshipType;

import java.util.Set;

/**
 * A relationship between two architectural elements.
 */
public class Relationship<T extends Item> extends com.araguacaima.gsa.model.diagrams.core.Relationship {

    private String technology;
    private InteractionStyle interactionStyle = InteractionStyle.Synchronous;
    private RelationshipType type;

    public Relationship() {
    }

    public Relationship(T source, T destination, String description, String technology, InteractionStyle interactionStyle) {

        super(source, destination, description);
        this.technology = technology;
        setInteractionStyle(interactionStyle);
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

        if (interactionStyle == InteractionStyle.Synchronous) {
            removeTag(InteractionStyle.Asynchronous.name());
            addTags(InteractionStyle.Synchronous.name());
        } else {
            removeTag(InteractionStyle.Synchronous.name());
            addTags(InteractionStyle.Asynchronous.name());
        }
    }

    @Override
    public RelationshipType getType() {
        return type;
    }

    @Override
    public void setType(RelationshipType type) {
        this.type = type;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return build(Tags.RELATIONSHIP);
    }

}