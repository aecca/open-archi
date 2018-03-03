package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.persons.Responsible;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "VersionControl")
@DynamicUpdate
public class VersionControl extends BaseEntity {

    @Column
    private String description;
    @Column
    private Date issueDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(schema = "PERSONS",
            name = "VersionControl_Responsibles",
            joinColumns = {
                    @JoinColumn(name = "VersionControl_Id", referencedColumnName = "Id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "Responsible_Id", referencedColumnName = "Id")})
    private Responsible responsible;
    @ManyToOne
    private Asm asm;
    @Column
    private String version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible Responsible) {
        this.responsible = Responsible;
    }

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void validateRequest() throws EntityError {
        super.validateRequest();
        //Do nothing. All request are valid on this entity
    }

    @Override
    public void validateCreation() {
        super.validateCreation();
        if (description == null || responsible == null || issueDate == null) {
            throw new EntityError(resourceBundle.getString(getCreationErrorMessageKey()));
        }
    }

    @Override
    public void validateModification() throws EntityError {
        super.validateModification();
        if (issueDate != null) {
            throw new EntityError(resourceBundle.getString(getModificationErrorMessageKey()));
        }
    }
}
