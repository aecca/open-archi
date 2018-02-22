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
public class Model extends Element implements DiagramableElement {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    protected Set<Relationship> relationships = new LinkedHashSet<>();
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Flowcharts",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Flowchart_Id",
                    referencedColumnName = "Id")})
    private Collection<Flowchart> flowcharts;
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.FLOWCHART_MODEL;

    public Collection<Flowchart> getFlowcharts() {
        return flowcharts;
    }

    public void setFlowcharts(Collection<Flowchart> flowchart) {
        this.flowcharts = flowchart;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }
}
