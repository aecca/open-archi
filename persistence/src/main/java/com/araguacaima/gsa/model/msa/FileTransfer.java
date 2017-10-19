package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FileTransfer",
        schema = "Msa")
public class FileTransfer extends BaseEntity {
}
