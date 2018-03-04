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
public class Model extends Element implements DiagramableElement {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Gantt_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Gantt_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();
    @OneToMany
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

    public void override(Model source) {
        super.override(source);
        this.setRelationships(source.getRelationships());
        this.setGantts(source.getGantts());
    }

    public void copyNonEmpty(Model source) {
        super.copyNonEmpty(source);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getGantts() != null && !source.getGantts().isEmpty()) {
            this.setGantts(source.getGantts());
        }
    }
}
