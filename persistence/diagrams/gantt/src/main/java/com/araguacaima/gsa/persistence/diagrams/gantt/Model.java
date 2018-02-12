package com.araguacaima.gsa.persistence.diagrams.gantt;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "GanttModel")
public class Model extends Element {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Gantt_Model_Gantts",
            joinColumns = {@JoinColumn(name = "Gantt_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Gantt_Id",
                    referencedColumnName = "Id")})
    private Collection<Gantt> gantts;
    @Column
    private ElementKind kind = ElementKind.GANTT_MODEL;

    public Collection<Gantt> getGantts() {
        return gantts;
    }

    public void setGantts(Collection<Gantt> gantt) {
        this.gantts = gantt;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
