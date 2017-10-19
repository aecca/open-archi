package com.araguacaima.gsa.model.am;

import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.persons.Responsible;
import com.araguacaima.gsa.model.common.Version;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "MetaData", schema = "AM")
public class MetaData extends BaseEntity {

    private Collection<Responsible> responsibles;
    private Collection<Responsible> collaborators;
    private Collection<Element> relatedWith;
    private Collection<Element> usedId;
    private Collection<Grouping> groupings;
    private DeploymentStatus deploymentStatus;

    @OneToOne
    private Version version;

    @Column
    private Type type;
    private Collection<View> views;

}
