package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An architecture model.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "ArchitectureModel")
@NamedQueries({@NamedQuery(name = Model.GET_ALL_RELATIONSHIPS,
        query = "select a.relationships from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_ALL_CONSUMERS_FOR_MODEL,
                query = "select a.consumers from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_CONSUMER_FOR_MODEL,
                query = "select c from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a JOIN a.consumers c where a.id=:id and c.id=:cid"),
        @NamedQuery(name = Model.GET_ALL_SOFTWARE_SYSTEMS,
                query = "select a.softwareSystems from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_SOFTWARE_SYSTEM,
                query = "select s from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a JOIN a.softwareSystems s where a.id=:id and s.id=:sid")})
public class Model extends Element implements DiagramableElement<Model> {

    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships";
    public static final String GET_ALL_CONSUMERS_FOR_MODEL = "get.all.consumers.for.model";
    public static final String GET_CONSUMER_FOR_MODEL = "get.consumer.for.model";
    public static final String GET_ALL_SOFTWARE_SYSTEMS = "get.all.software.systems";
    public static final String GET_SOFTWARE_SYSTEM = "get.software.system";

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Consumers",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Consumer_Id",
                    referencedColumnName = "Id")})
    private Set<Consumer> consumers = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_SoftwareSystems",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")})
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_DeploymentNodes",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")})
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();

    public Model() {
        setKind(ElementKind.ARCHITECTURE_MODEL);
    }

    @Override
    public void override(Model source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        for (Relationship relationship : source.getRelationships()) {
            Relationship newRelationship = new Relationship();
            newRelationship.override(relationship, keepMeta, suffix);
            this.relationships.add(newRelationship);
        }
        for (Consumer consumer : source.getConsumers()) {
            Consumer newConsumer = new Consumer();
            newConsumer.override(consumer, keepMeta, suffix);
            this.consumers.add(newConsumer);
        }
        for (SoftwareSystem softwareSystem : source.getSoftwareSystems()) {
            SoftwareSystem newSoftwareSystem = new SoftwareSystem();
            newSoftwareSystem.override(softwareSystem, keepMeta, suffix);
            this.softwareSystems.add(newSoftwareSystem);
        }
        for (DeploymentNode deploymentNode : source.getDeploymentNodes()) {
            DeploymentNode newDeploymentNode = new DeploymentNode();
            newDeploymentNode.override(deploymentNode, keepMeta, suffix);
            this.deploymentNodes.add(newDeploymentNode);
        }
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            for (Relationship relationship : source.getRelationships()) {
                Relationship newRelationship = new Relationship();
                newRelationship.copyNonEmpty(relationship, keepMeta);
                this.relationships.add(newRelationship);
            }
        }
        if (source.getConsumers() != null && !source.getConsumers().isEmpty()) {
            for (Consumer consumer : source.getConsumers()) {
                Consumer newConsumer = new Consumer();
                newConsumer.copyNonEmpty(consumer, keepMeta);
                this.consumers.add(newConsumer);
            }
        }
        if (source.getSoftwareSystems() != null && !source.getSoftwareSystems().isEmpty()) {
            for (SoftwareSystem softwareSystem : source.getSoftwareSystems()) {
                SoftwareSystem newSoftwareSystem = new SoftwareSystem();
                newSoftwareSystem.copyNonEmpty(softwareSystem, keepMeta);
                this.softwareSystems.add(newSoftwareSystem);
            }
        }
        if (source.getDeploymentNodes() != null && !source.getDeploymentNodes().isEmpty()) {
            for (DeploymentNode deploymentNode : source.getDeploymentNodes()) {
                DeploymentNode newDeploymentNode = new DeploymentNode();
                newDeploymentNode.copyNonEmpty(deploymentNode, keepMeta);
                this.deploymentNodes.add(newDeploymentNode);
            }
        }
    }

    public Set<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<Consumer> people) {
        this.consumers = people;
    }

    public Set<SoftwareSystem> getSoftwareSystems() {
        return softwareSystems;
    }

    public void setSoftwareSystems(Set<SoftwareSystem> softwareSystems) {
        this.softwareSystems = softwareSystems;
    }

    public Set<DeploymentNode> getDeploymentNodes() {
        return deploymentNodes;
    }

    public void setDeploymentNodes(Set<DeploymentNode> deploymentNodes) {
        this.deploymentNodes = deploymentNodes;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }
}