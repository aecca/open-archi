package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "Markdown")
public class Markdown extends BaseEntity {

    @Column
    private MarkdownFlavour flavour;
    @Column
    private String value;

    public MarkdownFlavour getFlavour() {
        return flavour;
    }

    public void setFlavour(MarkdownFlavour flavour) {
        this.flavour = flavour;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
