package com.araguacaima.gsa.persistence.diagrams.core;

import com.araguacaima.gsa.model.meta.IMetaData;
import com.araguacaima.gsa.model.meta.Type;
import com.araguacaima.gsa.model.meta.Version;
import com.araguacaima.gsa.model.meta.View;
import com.araguacaima.gsa.model.persons.Responsible;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "MetaData", schema = "DIAGRAMS")
public class MetaData implements IMetaData {

    private Collection<Responsible> responsibles;
    private Collection<Responsible> collaborators;
    private Collection<Element> relatedWith;
    private Collection<Element> usedId;
    @OneToOne
    private Version version;
    @Column
    private Type type;
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

    public Collection<Element> getUsedId() {
        return usedId;
    }

    public void setUsedId(Collection<Element> usedId) {
        this.usedId = usedId;
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
