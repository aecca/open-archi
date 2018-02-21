package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "BulkProcessing",
        schema = "DIAGRAMS")
public class BulkProcessing extends BaseEntity {
}
