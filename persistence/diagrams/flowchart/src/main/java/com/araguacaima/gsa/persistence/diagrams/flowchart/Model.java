package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "FlowchartModel")
public class Model extends Element {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Flowchart_Model_Flowcharts",
            joinColumns = {@JoinColumn(name = "Flowchart_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Flowchart_Id",
                    referencedColumnName = "Id")})
    private Collection<Flowchart> flowcharts;
    @Column
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
}
