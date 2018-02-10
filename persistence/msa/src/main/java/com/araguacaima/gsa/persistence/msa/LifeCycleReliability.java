package com.araguacaima.gsa.persistence.msa;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "LifeCycleReliability")
public class LifeCycleReliability extends AbstractReliability {

}
