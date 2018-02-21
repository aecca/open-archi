package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "Markdown")
public class Markdown extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
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
