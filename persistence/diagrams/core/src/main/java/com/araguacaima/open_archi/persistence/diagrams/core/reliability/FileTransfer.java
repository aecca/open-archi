package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "FileTransfer", schema = "DIAGRAMS")
@DynamicUpdate
public class FileTransfer extends BaseEntity {

    public void override(FileTransfer source, boolean keepMeta) {
        super.override(source, keepMeta);
    }

    public void copyNonEmpty(FileTransfer source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }
}
