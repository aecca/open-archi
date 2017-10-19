package com.araguacaima.gsa.model.msa;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "Msa",
        name = "LifeCycleReliability")
public class LifeCycleReliability extends AbstractReliability {

}
