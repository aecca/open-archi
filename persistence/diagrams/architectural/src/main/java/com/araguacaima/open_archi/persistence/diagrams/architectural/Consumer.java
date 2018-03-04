package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * However you think about your users (as actors, roles, persons, etc),
 * people are the various human users of your software system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Consumer">Model - Consumer</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Consumer extends StaticElement {

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    public Consumer() {
        setKind(ElementKind.CONSUMER);
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void override(Consumer source) {
        super.override(source);
        this.setScope(source.getScope());
    }

    public void copyNonEmpty(Consumer source) {
        super.copyNonEmpty(source);
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
    }


}