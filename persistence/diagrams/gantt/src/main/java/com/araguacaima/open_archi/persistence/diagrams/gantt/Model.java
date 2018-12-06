package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.ModelElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "GanttModel")
public class Model extends ModelElement implements DiagramableElement<Model> {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Gantt_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Gantt_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Gantt_Model_Gantts",
            joinColumns = {@JoinColumn(name = "Gantt_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Gantt_Id",
                    referencedColumnName = "Id")})
    private Collection<Gantt> gantts;

    public Model() {
        setKind(ElementKind.GANTT_MODEL);
    }

    public Collection<Gantt> getGantts() {
        return gantts;
    }

    public void setGantts(Collection<Gantt> gantt) {
        this.gantts = gantt;
    }

    @Override
    public Collection<BaseEntity> override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setRelationships(source.getRelationships());
        this.setGantts(source.getGantts());
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Model source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getGantts() != null && !source.getGantts().isEmpty()) {
            this.setGantts(source.getGantts());
        }
        return overriden;
    }
}
