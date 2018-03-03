package com.araguacaima.open_archi.persistence.persons;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "PERSONS", name = "Responsible")
@DynamicUpdate
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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
