package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "Link")
public class Link extends BaseEntity {

    @Column
    private String name;
    @ManyToOne
    private Msa msa;
    @Column
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
