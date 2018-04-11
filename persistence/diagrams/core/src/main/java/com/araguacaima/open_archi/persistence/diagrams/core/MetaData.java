package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.meta.View;
import com.araguacaima.open_archi.persistence.persons.Responsible;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "MetaData", schema = "DIAGRAMS")
@DynamicUpdate
public class MetaData extends BaseEntity {

    @ManyToMany(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_Responsibles",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_Collaborators",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id", table = "Responsible")},
            inverseJoinColumns = {@JoinColumn(name = "Collaborator_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> collaborators;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_RelatedWith",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "RelatedWith_Id",
                    referencedColumnName = "Id")})
    private Collection<Taggable> relatedWith;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(schema = "DIAGRAMS",
            name = "MetaData_UsedIn",
            joinColumns = {@JoinColumn(name = "MetaData_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "UsedIn_Id",
                    referencedColumnName = "Id")})
    private Collection<Taggable> usedIn;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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

    public Collection<View> getViews() {
        return views;
    }

    public void setViews(Collection<View> views) {
        this.views = views;
    }

    public void override(MetaData source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.responsibles = source.getResponsibles();
        this.collaborators = source.getCollaborators();
        this.relatedWith = source.getRelatedWith();
        this.usedIn = source.getUsedIn();
        this.views = source.getViews();
    }

    public void copyNonEmpty(MetaData source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getResponsibles() != null && !source.getResponsibles().isEmpty()) {
            this.responsibles = source.getResponsibles();
        }
        if (source.getCollaborators() != null && !source.getCollaborators().isEmpty()) {
            this.collaborators = source.getCollaborators();
        }
        if (source.getRelatedWith() != null && !source.getRelatedWith().isEmpty()) {
            this.relatedWith = source.getRelatedWith();
        }
        if (source.getUsedIn() != null && !source.getUsedIn().isEmpty()) {
            this.usedIn = source.getUsedIn();
        }
        if (source.getViews() != null && !source.getViews().isEmpty()) {
            this.views = source.getViews();
        }
    }
}
