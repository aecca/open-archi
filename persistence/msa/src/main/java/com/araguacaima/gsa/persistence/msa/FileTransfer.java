package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "msa")
@Table(name = "FileTransfer",
        schema = "MSA")
public class FileTransfer extends BaseEntity {
}
