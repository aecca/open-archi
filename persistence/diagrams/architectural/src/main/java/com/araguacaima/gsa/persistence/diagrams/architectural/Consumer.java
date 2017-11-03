package com.araguacaima.gsa.persistence.diagrams.architectural;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

/**
 * However you think about your users (as actors, roles, persons, etc),
 * people are the various human users of your software system.
 * <p>
 * See <a href="https://structurizr.com/help/model#Consumer">Model - Consumer</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Consumer", schema = "DIAGRAMS")
public class Consumer extends StaticElement {

    @Column
    private Scope scope = Scope.Unspecified;

    public Consumer() {
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}