package com.araguacaima.open_archi.persistence.diagrams.architectural;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A software system is the highest level of abstraction and describes something
 * that delivers value to its users, whether they are human or not. This includes
 * the software system you are modelling, and the other software systems upon
 * which your software system depends.
 * <p>
 * See <a href="https://structurizr.com/help/model#System">Model - Software System</a>
 * on the Structurizr website for more information.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class Systems extends StaticElements {

    @Column
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.Unspecified;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "System_Containers",
            joinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Containers> containers = new LinkedHashSet<>();

    public Systems() {
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<Containers> getContainers() {
        return containers;
    }

    public void setContainers(Set<Containers> containers) {
        this.containers = containers;
    }

    public void override(Systems source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setScope(source.getScope());
        for (Containers container : source.getContainers()) {
            Containers newContainer = new Containers();
            newContainer.override(container, keepMeta, suffix);
            this.containers.add(newContainer);
        }
    }

    public void copyNonEmpty(Systems source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getScope() != null) {
            this.setScope(source.getScope());
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Containers container : source.getContainers()) {
                Containers newContainer = new Containers();
                newContainer.copyNonEmpty(container, keepMeta);
                this.containers.add(newContainer);
            }
        }
    }
}
