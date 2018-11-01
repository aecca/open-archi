package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * However you think about your users (as actors, roles, persons, etc),
 * people are the various human users of your system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Consumer">Model - CONSUMER</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Consumer extends LeafStaticElement {

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


    public Collection<BaseEntity> override(Consumers source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setScope(source.getScope());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Consumers source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        return overriden;
    }


}