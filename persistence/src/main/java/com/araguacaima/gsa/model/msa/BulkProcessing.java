package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BulkProcessing",
        schema = "Msa")
public class BulkProcessing extends BaseEntity {
}
