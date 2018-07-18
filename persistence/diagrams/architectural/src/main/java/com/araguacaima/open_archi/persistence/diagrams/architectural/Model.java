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
                query = "select a.systems from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_SOFTWARE_SYSTEM,
                query = "select s from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a JOIN a.systems s where a.id=:id and s.id=:sid")})
public class Model extends Element implements DiagramableElement<Model> {

    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships";
    public static final String GET_ALL_CONSUMERS_FOR_MODEL = "get.all.consumers.for.model";
    public static final String GET_CONSUMER_FOR_MODEL = "get.consumer.for.model";
    public static final String GET_ALL_SOFTWARE_SYSTEMS = "get.all.software.systems";
    public static final String GET_SOFTWARE_SYSTEM = "get.software.system";
    public static final String SHAPE_COLOR = "#FFFFFF";

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
            name = "Architecture_Model_Layers",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Layer_Id",
                    referencedColumnName = "Id")})
    private Set<Layer> layers = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Systems",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "System_Id",
                    referencedColumnName = "Id")})
    private Set<System> systems = new LinkedHashSet<>();


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Containers",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Container_Id",
                    referencedColumnName = "Id")})
    private Set<Container> containers = new LinkedHashSet<>();


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_Components",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Component_Id",
                    referencedColumnName = "Id")})
    private Set<Component> components = new LinkedHashSet<>();

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

    public Set<Layer> getLayers() {
        return layers;
    }

    public void setLayers(Set<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public void override(Model source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        for (Consumer consumer : source.getConsumers()) {
            Consumer newConsumer = new Consumer();
            newConsumer.override(consumer, keepMeta, suffix);
            this.consumers.add(newConsumer);
        }
        for (Relationship relationship : source.getRelationships()) {
            Relationship newRelationship = new Relationship();
            newRelationship.override(relationship, keepMeta, suffix);
            this.relationships.add(newRelationship);
        }
        for (Layer layer : source.getLayers()) {
            Layer newLayer = new Layer();
            newLayer.override(layer, keepMeta, suffix);
            this.layers.add(newLayer);
        }
        for (System software : source.getSystems()) {
            System newSystem = new System();
            newSystem.override(software, keepMeta, suffix);
            this.systems.add(newSystem);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            newContainer.override(container, keepMeta, suffix);
            this.containers.add(newContainer);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            newComponent.override(component, keepMeta, suffix);
            this.components.add(newComponent);
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
        if (source.getConsumers() != null && !source.getConsumers().isEmpty()) {
            for (Consumer consumer : source.getConsumers()) {
                Consumer newConsumer = new Consumer();
                newConsumer.copyNonEmpty(consumer, keepMeta);
                this.consumers.add(newConsumer);
            }
        }
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            for (Relationship relationship : source.getRelationships()) {
                Relationship newRelationship = new Relationship();
                newRelationship.copyNonEmpty(relationship, keepMeta);
                this.relationships.add(newRelationship);
            }
        }
        if (source.getLayers() != null && !source.getLayers().isEmpty()) {
            for (Layer software : source.getLayers()) {
                Layer newLayer = new Layer();
                newLayer.copyNonEmpty(software, keepMeta);
                this.layers.add(newLayer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System software : source.getSystems()) {
                System newSystem = new System();
                newSystem.copyNonEmpty(software, keepMeta);
                this.systems.add(newSystem);
            }
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container software : source.getContainers()) {
                Container newContainer = new Container();
                newContainer.copyNonEmpty(software, keepMeta);
                this.containers.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component software : source.getComponents()) {
                Component newComponent = new Component();
                newComponent.copyNonEmpty(software, keepMeta);
                this.components.add(newComponent);
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

    public Set<System> getSystems() {
        return systems;
    }

    public void setSystems(Set<System> softwares) {
        this.systems = softwares;
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

    public Set<Container> getContainers() {
        return containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }
}