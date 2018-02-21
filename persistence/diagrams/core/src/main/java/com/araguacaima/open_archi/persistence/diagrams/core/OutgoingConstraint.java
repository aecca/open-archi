package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "OutgoingConstraint", schema = "DIAGRAMS")
public class OutgoingConstraint extends Taggable{
}
