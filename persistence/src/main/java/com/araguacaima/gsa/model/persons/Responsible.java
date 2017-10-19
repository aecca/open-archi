package com.araguacaima.gsa.model.persons;


import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.msa.Msa;

import javax.persistence.*;

@Entity
@Table(schema = "Msa",
       name = "Responsible")
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne
    private Person Responsible;
    @ManyToOne
    private Msa msa;

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Person getResponsible() {
        return Responsible;
    }

    public void setResponsible(Person Responsible) {
        this.Responsible = Responsible;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }
}
