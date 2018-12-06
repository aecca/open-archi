package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.persons.Responsible;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "Asm")
@DynamicUpdate
@NamedQueries(value = {@NamedQuery(name = "Asm.getAll",
        query = "SELECT asm FROM Asm asm"), @NamedQuery(name = "Asm.getById",
        query = "SELECT asm FROM Asm asm WHERE Asm.id=:id")})
public class Asm extends BaseEntity {

    @Column
    private Byte[] attachments;
    @Column
    @Enumerated(EnumType.STRING)
    private Country country;
    @Column(unique = false,
            nullable = false)
    @NotNull
    @Size(min = 1)
    private Date expirationDate;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Initiative initiative;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private IntermediateSolution intermediateSolution;
    @Column(unique = false,
            nullable = false)
    @NotNull
    @Size(min = 1)
    private Date issueDate;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "SM",
            name = "Asm_LinksOfInterests",
            joinColumns = {@JoinColumn(name = "Asm_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Link_Id",
                    referencedColumnName = "Id")})
    private Set<Link> linksOfInterests;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ProjectMetaData projectMetaData;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ProposedSolution proposedSolution;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "SM",
            name = "Asm_Responsibles",
            joinColumns = {@JoinColumn(name = "Asm_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Set<Responsible> responsibles;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "SM",
            name = "Asm_Statuses",
            joinColumns = {@JoinColumn(name = "Asm_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Status_Id",
                    referencedColumnName = "Id")})
    private Set<Status> status;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "SM",
            name = "Asm_VersionControls",
            joinColumns = {@JoinColumn(name = "Asm_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "VersionControl_Id",
                    referencedColumnName = "Id")})
    private Set<VersionControl> versionControl;

    public Byte[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Byte[] attachments) {
        this.attachments = attachments;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Initiative getInitiative() {
        return initiative;
    }

    public void setInitiative(Initiative initiative) {
        this.initiative = initiative;
    }

    public IntermediateSolution getIntermediateSolution() {
        return intermediateSolution;
    }

    public void setIntermediateSolution(IntermediateSolution intermediateSolution) {
        this.intermediateSolution = intermediateSolution;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Set<Link> getLinksOfInterests() {
        return linksOfInterests;
    }

    public void setLinksOfInterests(Set<Link> linksOfInterest) {
        this.linksOfInterests = linksOfInterest;
    }

    public ProjectMetaData getProjectMetaData() {
        return projectMetaData;
    }

    public void setProjectMetaData(ProjectMetaData projectMetaData) {
        this.projectMetaData = projectMetaData;
    }

    public ProposedSolution getProposedSolution() {
        return proposedSolution;
    }

    public void setProposedSolution(ProposedSolution proposedSolution) {
        this.proposedSolution = proposedSolution;
    }

    public Set<Responsible> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Set<Responsible> responsibles) {
        this.responsibles = responsibles;
    }

    public Set<Status> getStatus() {
        return status;
    }

    public void setStatus(Set<Status> status) {
        this.status = status;
    }

    public Set<VersionControl> getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(Set<VersionControl> versionControl) {
        this.versionControl = versionControl;
    }
}
