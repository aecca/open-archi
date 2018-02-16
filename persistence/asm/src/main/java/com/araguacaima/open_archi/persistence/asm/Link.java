package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "Link")
public class Link extends BaseEntity {

    @Column
    private String name;
    @ManyToOne
    private Asm asm;
    @Column
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
