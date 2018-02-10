package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.persons.Responsible;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "Msa")
@NamedQueries(value = {@NamedQuery(name = "Msa.getAll",
        query = "SELECT msa FROM Msa msa"), @NamedQuery(name = "Msa.getById",
        query = "SELECT msa FROM Msa msa WHERE msa.id=:id")})
public class Msa extends BaseEntity {

    @Column
    private Byte[] attachments;
    @Column
    private Country country;
    @Column(unique = false,
            nullable = false)
    @NotNull
    @Size(min = 1)
    private Date expirationDate;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Initiative initiative;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private IntermediateSolution intermediateSolution;
    @Column(unique = false,
            nullable = false)
    @NotNull
    @Size(min = 1)
    private Date issueDate;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "Msa_LinksOfInterests",
            joinColumns = {@JoinColumn(name = "Msa_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Link_Id",
                    referencedColumnName = "Id")})
    private Collection<Link> linksOfInterests;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ProjectMetaData projectMetaData;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ProposedSolution proposedSolution;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "Msa_Responsibles",
            joinColumns = {@JoinColumn(name = "Msa_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "Msa_Statuses",
            joinColumns = {@JoinColumn(name = "Msa_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Status_Id",
                    referencedColumnName = "Id")})
    private Set<Status> status;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "Msa_VersionControls",
            joinColumns = {@JoinColumn(name = "Msa_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "VersionControl_Id",
                    referencedColumnName = "Id")})
    private Collection<VersionControl> versionControl;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Collection<Link> getLinksOfInterests() {
        return linksOfInterests;
    }

    public void setLinksOfInterests(Collection<Link> linksOfInterest) {
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

    public Collection<Responsible> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Collection<Responsible> responsibles) {
        this.responsibles = responsibles;
    }

    public Set<Status> getStatus() {
        return status;
    }

    public void setStatus(Set<Status> status) {
        this.status = status;
    }

    public Collection<VersionControl> getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(Collection<VersionControl> versionControl) {
        this.versionControl = versionControl;
    }
}
