package com.araguacaima.gsa.persistence.msa;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "msa")
@Table(schema = "MSA",
        name = "AvailabilityReliability")
public class AvailabilityReliability extends AbstractReliability {

}
