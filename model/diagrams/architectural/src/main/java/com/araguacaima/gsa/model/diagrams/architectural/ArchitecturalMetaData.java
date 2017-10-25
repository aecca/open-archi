package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.Grouping;
import com.araguacaima.gsa.model.diagrams.core.MetaData;
import com.araguacaima.gsa.model.meta.Version;
import com.araguacaima.gsa.model.persons.Responsible;

import java.util.Collection;

public class ArchitecturalMetaData implements MetaData {

    private Collection<Responsible> responsibles;
    private Collection<Responsible> collaborators;
    private Collection<Element> relatedWith;
    private Collection<Element> usedId;
    private Collection<Grouping> groupings;
    private DeploymentStatus deploymentStatus;
    private Version version;
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
