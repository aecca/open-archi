package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.persistence.common.BaseEntity;
import com.araguacaima.gsa.model.common.Version;
import com.araguacaima.gsa.persistence.persons.Responsible;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "MetaData", schema = "AM")
public class MetaData extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_Responsibles",
            joinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_Collaborators",
            joinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> collaborators;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_RelatedWith",
            joinColumns = {@JoinColumn(name = "Element_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Element_Id",
                    referencedColumnName = "Id")})
    private Collection<Element> relatedWith;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_UsedIn",
            joinColumns = {@JoinColumn(name = "Element_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Element_Id",
                    referencedColumnName = "Id")})
    private Collection<Element> usedIn;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_Groupings",
            joinColumns = {@JoinColumn(name = "Grouping_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Grouping_Id",
                    referencedColumnName = "Id")})
    private Collection<Grouping> groupings;

    @OneToOne
    private DeploymentStatus deploymentStatus;

    @OneToOne
    private Responsible validatedBy;

    @OneToOne
    private Version version;

    @Column
    private Type type;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "MetaData_Views",
            joinColumns = {@JoinColumn(name = "View_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "View_Id",
                    referencedColumnName = "Id")})
    private Collection<View> views;


    public Collection<Responsible> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Collection<Responsible> responsibles) {
        this.responsibles = responsibles;
    }

    public Collection<Responsible> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Collection<Responsible> collaborators) {
        this.collaborators = collaborators;
    }

    public Collection<Element> getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(Collection<Element> relatedWith) {
        this.relatedWith = relatedWith;
    }

    public Collection<Element> getUsedIn() {
        return usedIn;
    }

    public void setUsedIn(Collection<Element> usedIn) {
        this.usedIn = usedIn;
    }

    public Collection<Grouping> getGroupings() {
        return groupings;
    }

    public void setGroupings(Collection<Grouping> groupings) {
        this.groupings = groupings;
    }

    public DeploymentStatus getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(DeploymentStatus deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public Responsible getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(Responsible validatedBy) {
        this.validatedBy = validatedBy;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Collection<View> getViews() {
        return views;
    }

    public void setViews(Collection<View> views) {
        this.views = views;
    }
}
