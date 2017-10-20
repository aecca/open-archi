package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
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
