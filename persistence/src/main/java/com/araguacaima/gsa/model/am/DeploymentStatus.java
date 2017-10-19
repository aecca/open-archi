package com.araguacaima.gsa.model.am;


import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "DeploymentStatus", schema = "AM")
public class DeploymentStatus extends BaseEntity {

    @Column
    private LifeCycle currentStatus;
}
