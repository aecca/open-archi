package com.araguacaima.open_archi.persistence.asm;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "MonitoringAndAlertsReliability")
public class MonitoringAndAlertsReliability extends AbstractReliability {

}
