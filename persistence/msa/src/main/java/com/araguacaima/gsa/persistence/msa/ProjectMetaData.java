package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.persons.Person;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "msa")
@Table(schema = "MSA",
        name = "ProjectMetaData")
public class ProjectMetaData extends BaseEntity {

    @Column
    private String business;
    @Column
    private Country country;
    @Column
    private String documentation;
    @Column
    private String functionalInitiativeDescription;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "ProjectMetaData_GlobalSolutionArchitects",
            joinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")})
    private Collection<Person> globalSolutionArchitects;
    @Column
    private String initiativeName;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "ProjectMetaData_LocalSolutionArchitects",
            joinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")})
    private Collection<Person> localSolutionArchitects;
    @Column
    private Methodology methodology;
    @ManyToOne
    private Person productOwner;
    @ManyToOne
    private Person programTeamLeader;
    @Column
    private String projectIdentifier;
    @Column
    private Segment segment;
    @OneToOne
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
