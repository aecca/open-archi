package com.araguacaima.gsa.persistence.msa;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(schema = "MSA",
        name = "AvailabilityReliability")
public class AvailabilityReliability extends AbstractReliability {

}
