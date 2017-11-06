package com.araguacaima.gsa.persistence.diagrams.flowchart;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Flowchart_Model", schema = "DIAGRAMS")
@DiscriminatorValue(value = "FlowchartModel")
public class Model extends Element {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Flowcharts",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Flowchart_Id",
                    referencedColumnName = "Id")})
    private Collection<Flowchart> flowchart;
    @Column
    private ElementKind kind = ElementKind.FLOWCHART_MODEL;

    public Collection<Flowchart> getFlowchart() {
        return flowchart;
    }

    public void setFlowchart(Collection<Flowchart> flowchart) {
        this.flowchart = flowchart;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
