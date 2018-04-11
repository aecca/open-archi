package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "GanttModel")
public class Model extends Element implements DiagramableElement<Model> {

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Gantt_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Gantt_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

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

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public void override(Model source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setRelationships(source.getRelationships());
        this.setGantts(source.getGantts());
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getGantts() != null && !source.getGantts().isEmpty()) {
            this.setGantts(source.getGantts());
        }
    }
}
