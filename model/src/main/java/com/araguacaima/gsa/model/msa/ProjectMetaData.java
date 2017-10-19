package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.persons.Person;

import java.util.Collection;

public class ProjectMetaData extends BaseEntity {

    private String business;

    private Country country;

    private String documentation;

    private String functionalInitiativeDescription;

    private Collection<Person> globalSolutionArchitects;

    private String initiativeName;

    private Collection<Person> localSolutionArchitects;

    private Methodology methodology;

    private Person productOwner;

    private Person programTeamLeader;

    private String projectIdentifier;

    private Segment segment;

    private Msa msa;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getFunctionalInitiativeDescription() {
        return functionalInitiativeDescription;
    }

    public void setFunctionalInitiativeDescription(String functionalInitiativeDescription) {
        this.functionalInitiativeDescription = functionalInitiativeDescription;
    }

    public Collection<Person> getGlobalSolutionArchitects() {
        return globalSolutionArchitects;
    }

    public void setGlobalSolutionArchitects(Collection<Person> globalSolutionArchitects) {
        this.globalSolutionArchitects = globalSolutionArchitects;
    }

    public String getInitiativeName() {
        return initiativeName;
    }

    public void setInitiativeName(String initiativeName) {
        this.initiativeName = initiativeName;
    }

    public Collection<Person> getLocalSolutionArchitects() {
        return localSolutionArchitects;
    }

    public void setLocalSolutionArchitects(Collection<Person> solutionArchitects) {
        this.localSolutionArchitects = solutionArchitects;
    }

    public Methodology getMethodology() {
        return methodology;
    }

    public void setMethodology(Methodology methodology) {
        this.methodology = methodology;
    }

    public Person getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(Person productOwner) {
        this.productOwner = productOwner;
    }

    public Person getProgramTeamLeader() {
        return programTeamLeader;
    }

    public void setProgramTeamLeader(Person programTeamLeader) {
        this.programTeamLeader = programTeamLeader;
    }

    public String getProjectIdentifier() {
        return projectIdentifier;
    }

    public void setProjectIdentifier(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }
}
