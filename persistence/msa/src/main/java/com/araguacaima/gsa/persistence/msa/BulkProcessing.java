package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "msa")
@Table(name = "BulkProcessing",
        schema = "MSA")
public class BulkProcessing extends BaseEntity {
}
