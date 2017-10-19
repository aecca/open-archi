package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.persons.Responsible;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class Msa extends BaseEntity {

    private Byte[] attachments;

    private Country country;

    private Date expirationDate;

    private Initiative initiative;

    private IntermediateSolution intermediateSolution;

    private Date issueDate;

    private Collection<Link> linksOfInterests;

    private ProjectMetaData projectMetaData;

    private ProposedSolution proposedSolution;

    private Collection<Responsible> responsibles;

    private Set<Status> status;

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
