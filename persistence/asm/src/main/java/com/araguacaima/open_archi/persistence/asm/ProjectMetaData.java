package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.persons.Person;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
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
    @JoinTable(schema = "ASM",
            name = "ProjectMetaData_GlobalSolutionArchitects",
            joinColumns = {@JoinColumn(name = "ProjectMetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")})
    private Collection<Person> globalSolutionArchitects;
    @Column
    private String initiativeName;
    @OneToMany
    @JoinTable(schema = "ASM",
            name = "ProjectMetaData_LocalSolutionArchitects",
            joinColumns = {@JoinColumn(name = "ProjectMetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")})
    private Collection<Person> localSolutionArchitects;
    @Column
    @Enumerated(EnumType.STRING)
    private Methodology methodology;
    @ManyToOne
    private Person productOwner;
    @ManyToOne
    private Person programTeamLeader;
    @Column
    private String projectIdentifier;
    @Column
    @Enumerated(EnumType.STRING)
    private Segment segment;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Asm asm;

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

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }
}
