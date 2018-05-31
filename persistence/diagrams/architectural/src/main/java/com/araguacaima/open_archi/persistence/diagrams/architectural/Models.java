package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Elements;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A software architecture model.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "ArchitectureModels")
@NamedQueries({@NamedQuery(name = Models.GET_ALL_RELATIONSHIPS,
        query = "select a.relationships from Models a where a.id=:id"),})
public class Models extends Elements {

    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships_list";

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    protected Set<Relationships> relationships = new LinkedHashSet<>();

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.ARCHITECTURE_MODEL;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_People",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Consumer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Consumer_Id",
                    referencedColumnName = "Id")})
    private Set<Consumers> consumers = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Systems",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")})
    private Set<Systems> softwareSystems = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_DeploymentNodes",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")})
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
        return softwareSystems;
    }

    public void setSystems(Set<Systems> softwareSystems) {
        this.softwareSystems = softwareSystems;
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

    public void override(Models source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        for (Relationships relationship : source.getRelationships()) {
            Relationships newRelationship = new Relationships();
            newRelationship.override(relationship, keepMeta, suffix);
            this.relationships.add(newRelationship);
        }
        for (Consumers consumer : source.getConsumers()) {
            Consumers newConsumer = new Consumers();
            newConsumer.override(consumer, keepMeta, suffix);
            this.consumers.add(newConsumer);
        }
        for (Systems softwareSystem : source.getSystems()) {
            Systems newSystem = new Systems();
            newSystem.override(softwareSystem, keepMeta, suffix);
            this.softwareSystems.add(newSystem);
        }
        for (DeploymentNodes deploymentNode : source.getDeploymentNodes()) {
            DeploymentNodes newDeploymentNode = new DeploymentNodes();
            newDeploymentNode.override(deploymentNode, keepMeta, suffix);
            this.deploymentNodes.add(newDeploymentNode);
        }
    }

    public void copyNonEmpty(Models source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            for (Relationships relationship : source.getRelationships()) {
                Relationships newRelationship = new Relationships();
                newRelationship.copyNonEmpty(relationship, keepMeta);
                this.relationships.add(newRelationship);
            }
        }
        if (source.getConsumers() != null && !source.getConsumers().isEmpty()) {
            for (Consumers consumer : source.getConsumers()) {
                Consumers newConsumer = new Consumers();
                newConsumer.copyNonEmpty(consumer, keepMeta);
                this.consumers.add(newConsumer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (Systems softwareSystem : source.getSystems()) {
                Systems newSystem = new Systems();
                newSystem.copyNonEmpty(softwareSystem, keepMeta);
                this.softwareSystems.add(newSystem);
            }
        }
        if (source.getDeploymentNodes() != null && !source.getDeploymentNodes().isEmpty()) {
            for (DeploymentNodes deploymentNode : source.getDeploymentNodes()) {
                DeploymentNodes newDeploymentNode = new DeploymentNodes();
                newDeploymentNode.copyNonEmpty(deploymentNode, keepMeta);
                this.deploymentNodes.add(newDeploymentNode);
            }
        }
    }
}