package com.araguacaima.gsa.model.msa;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "LifeCycleReliability")
public class LifeCycleReliability extends AbstractReliability {

}
