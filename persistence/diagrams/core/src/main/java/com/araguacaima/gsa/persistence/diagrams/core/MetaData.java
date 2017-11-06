package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.meta.Type;
import com.araguacaima.gsa.persistence.meta.Version;
import com.araguacaima.gsa.persistence.meta.View;
import com.araguacaima.gsa.persistence.persons.Responsible;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "MetaData", schema = "DIAGRAMS")
public class MetaData extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_Responsibles",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_Collaborators",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id", table = "Responsible")},
            inverseJoinColumns = {@JoinColumn(name = "Collaborator_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> collaborators;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_RelatedWith",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "RelatedWith_Id",
                    referencedColumnName = "Id")})
    private Collection<Taggable> relatedWith;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_UsedIn",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "UsedIn_Id",
                    referencedColumnName = "Id")})
    private Collection<Taggable> usedIn;

    @OneToOne
    @JoinTable(schema = "META",
            name = "MetaData_Version",
            joinColumns = {@JoinColumn(name = "MetaData_Id", referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Version_Id", referencedColumnName = "Id")})
    private Version version;

    @Column
    private Type type;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_Views",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
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

    public Collection<Taggable> getRelatedWith() {
        return relatedWith;
    }

    public void setRelatedWith(Collection<Taggable> relatedWith) {
        this.relatedWith = relatedWith;
    }

    public Collection<Taggable> getUsedIn() {
        return usedIn;
    }

    public void setUsedIn(Collection<Taggable> usedId) {
        this.usedIn = usedId;
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
