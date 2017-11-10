package com.araguacaima.gsa.persistence.persons;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@PersistenceUnit(unitName = "gsa")
@Entity
@Table(schema = "PERSONS",
        name = "Responsible")
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Person responsible;

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setResponsible(Person Responsible) {
        this.responsible = Responsible;
    }

}
