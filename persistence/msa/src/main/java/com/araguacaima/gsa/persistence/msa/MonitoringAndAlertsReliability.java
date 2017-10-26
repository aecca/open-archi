package com.araguacaima.gsa.persistence.msa;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "MonitoringAndAlertsReliability")
public class MonitoringAndAlertsReliability extends AbstractReliability {

}
