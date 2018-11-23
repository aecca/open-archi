package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.ModelElements;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A system architecture model.
 */

public class Models extends ModelElements {

    protected Set<Relationships> relationships = new LinkedHashSet<>();
    private ElementKind kind = ElementKind.ARCHITECTURE_MODEL;
    private Set<Consumers> consumers = new LinkedHashSet<>();
    private Set<Systems> systems = new LinkedHashSet<>();
    private Set<DeploymentNodes> deploymentNodes = new LinkedHashSet<>();

    public Models() {
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Set<Consumers> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<Consumers> people) {
        this.consumers = people;
    }

    public Set<Systems> getSystems() {
        return systems;
    }

    public void setSystems(Set<Systems> systems) {
        this.systems = systems;
    }

    public Set<DeploymentNodes> getDeploymentNodes() {
        return deploymentNodes;
    }

    public void setDeploymentNodes(Set<DeploymentNodes> deploymentNodes) {
        this.deploymentNodes = deploymentNodes;
    }

    public Set<Relationships> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationships> relationships) {
        this.relationships = relationships;
    }

    public Collection<BaseEntity> override(Models source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (Relationships relationship : source.getRelationships()) {
            Relationships newRelationship = new Relationships();
            overriden.addAll(newRelationship.override(relationship, keepMeta, suffix, clonedFrom));
            this.relationships.add(newRelationship);
            overriden.add(newRelationship);
        }
        for (Consumers consumer : source.getConsumers()) {
            Consumers newConsumer = new Consumers();
            overriden.addAll(newConsumer.override(consumer, keepMeta, suffix, clonedFrom));
            this.consumers.add(newConsumer);
            overriden.add(newConsumer);
        }
        for (Systems system : source.getSystems()) {
            Systems newSystem = new Systems();
            overriden.addAll(newSystem.override(system, keepMeta, suffix, clonedFrom));
            this.systems.add(newSystem);
            overriden.add(newSystem);
        }
        for (DeploymentNodes deploymentNode : source.getDeploymentNodes()) {
            DeploymentNodes newDeploymentNode = new DeploymentNodes();
            overriden.addAll(newDeploymentNode.override(deploymentNode, keepMeta, suffix, clonedFrom));
            this.deploymentNodes.add(newDeploymentNode);
            overriden.add(newDeploymentNode);
        }
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Models source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            for (Relationships relationship : source.getRelationships()) {
                Relationships newRelationship = new Relationships();
                overriden.addAll(newRelationship.copyNonEmpty(relationship, keepMeta));
                this.relationships.add(newRelationship);
                overriden.add(newRelationship);
            }
        }
        if (source.getConsumers() != null && !source.getConsumers().isEmpty()) {
            for (Consumers consumer : source.getConsumers()) {
                Consumers newConsumer = new Consumers();
                overriden.addAll(newConsumer.copyNonEmpty(consumer, keepMeta));
                this.consumers.add(newConsumer);
                overriden.add(newConsumer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (Systems system : source.getSystems()) {
                Systems newSystem = new Systems();
                overriden.addAll(newSystem.copyNonEmpty(system, keepMeta));
                this.systems.add(newSystem);
                overriden.add(newSystem);
            }
        }
        if (source.getDeploymentNodes() != null && !source.getDeploymentNodes().isEmpty()) {
            for (DeploymentNodes deploymentNode : source.getDeploymentNodes()) {
                DeploymentNodes newDeploymentNode = new DeploymentNodes();
                overriden.addAll(newDeploymentNode.copyNonEmpty(deploymentNode, keepMeta));
                this.deploymentNodes.add(newDeploymentNode);
                overriden.add(newDeploymentNode);
            }
        }
        return overriden;
    }
}