package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * However you think about your users (as actors, roles, persons, etc),
 * people are the various human users of your software system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Consumer">Model - Consumer</a>
 * on the Structurizr website for more information.
 */
public class Consumer extends StaticElement {

    private Scope scope = Scope.Unspecified;

    Consumer() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    /**
     * Gets the scope of this consumer.
     *
     * @return a Scope
     */
    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        if (scope != null) {
            this.scope = scope;
        } else {
            this.scope = Scope.Unspecified;
        }
    }

    @Override
    public String getCanonicalName() {
        return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return build(Tags.ELEMENT, Tags.PERSON);
    }

    @Override
    public Relationship delivers(Consumer destination, String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Consumer destination, String description, String technology) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Consumer destination, String description, String technology, InteractionStyle interactionStyle) {
        throw new UnsupportedOperationException();
    }

    public Relationship interactsWith(Consumer destination, String description) {
        return getModel().addRelationship(this, destination, description);
    }

    public Relationship interactsWith(Consumer destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    public Relationship interactsWith(Consumer destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

}