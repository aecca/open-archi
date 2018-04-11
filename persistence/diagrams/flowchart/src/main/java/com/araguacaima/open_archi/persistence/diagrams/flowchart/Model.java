package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "FlowchartModel")
public class Model extends Element implements DiagramableElement<Model> {

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Flowcharts",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Flowchart_Id",
                    referencedColumnName = "Id")})
    private Collection<Flowchart> flowcharts;

    public Model() {
        setKind(ElementKind.FLOWCHART_MODEL);
    }

    public Collection<Flowchart> getFlowcharts() {
        return flowcharts;
    }

    public void setFlowcharts(Collection<Flowchart> flowchart) {
        this.flowcharts = flowchart;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public void override(Model source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setFlowcharts(source.getFlowcharts());
        this.setRelationships(source.getRelationships());
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getFlowcharts() != null && !source.getFlowcharts().isEmpty()) {
            this.setFlowcharts(source.getFlowcharts());
        }
    }
}
