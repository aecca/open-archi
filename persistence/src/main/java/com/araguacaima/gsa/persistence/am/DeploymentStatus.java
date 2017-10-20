package com.araguacaima.gsa.persistence.am;


import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "DeploymentStatus", schema = "AM")
public class DeploymentStatus extends BaseEntity {

    @Column
    private LifeCycle currentStatus;

    public LifeCycle getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(LifeCycle currentStatus) {
        this.currentStatus = currentStatus;
    }
}
