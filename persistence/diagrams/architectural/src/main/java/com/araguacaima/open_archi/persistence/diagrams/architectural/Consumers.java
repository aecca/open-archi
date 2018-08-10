package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * However you think about your users (as actors, roles, persons, etc),
 * people are the various human users of your system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Consumer">Model - Consumer</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Consumers extends StaticElements {

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.CONSUMER;

    public Consumers() {
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }


    public void override(Consumers source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setScope(source.getScope());
    }

    public void copyNonEmpty(Consumers source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setGroup(boolean container) {

    }
}