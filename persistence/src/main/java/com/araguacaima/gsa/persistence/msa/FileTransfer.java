package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "FileTransfer",
        schema = "MSA")
public class FileTransfer extends BaseEntity {
}
