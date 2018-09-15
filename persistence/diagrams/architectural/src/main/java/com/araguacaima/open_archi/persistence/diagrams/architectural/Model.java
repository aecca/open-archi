package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.ModelElement;
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
@NamedQueries({
        @NamedQuery(name = Model.GET_ALL_MODEL_PROTOTYPES,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.prototype=true"),
        @NamedQuery(name = Model.GET_ALL_RELATIONSHIPS,
                query = "select a.relationships from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_ALL_CONSUMERS_FOR_MODEL,
                query = "select a.consumers from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_CONSUMER_FOR_MODEL,
                query = "select c from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a JOIN a.consumers c where a.id=:id and c.id=:cid"),
        @NamedQuery(name = Model.GET_ALL_SYSTEMS_FROM_MODEL,
                query = "select a.systems from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a where a.id=:id"),
        @NamedQuery(name = Model.GET_SYSTEM,
                query = "select s from com.araguacaima.open_archi.persistence.diagrams.architectural.Model a JOIN a.systems s where a.id=:id and s.id=:sid")})
public class Model extends ModelElement implements DiagramableElement<Model> {

    public static final String GET_ALL_MODEL_PROTOTYPES = "get.all.model.prototypes";
    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships";
    public static final String GET_ALL_CONSUMERS_FOR_MODEL = "get.all.consumers.for.model";
    public static final String GET_CONSUMER_FOR_MODEL = "get.consumer.for.model";
    public static final String GET_ALL_SYSTEMS_FROM_MODEL = "get.all.systems.from.model";
    public static final String GET_SYSTEM = "get.system";

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

    public Set<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(Set<Consumer> people) {
        this.consumers = people;
    }

    public Set<System> getSystems() {
        return systems;
    }

    public void setSystems(Set<System> systems) {
        this.systems = systems;
    }

    public Set<DeploymentNode> getDeploymentNodes() {
        return deploymentNodes;
    }

    public void setDeploymentNodes(Set<DeploymentNode> deploymentNodes) {
        this.deploymentNodes = deploymentNodes;
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

    @Override
    public void override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        for (Consumer consumer : source.getConsumers()) {
            Consumer newConsumer = new Consumer();
            newConsumer.override(consumer, keepMeta, suffix, clonedFrom);
            this.consumers.add(newConsumer);
        }
        for (Layer layer : source.getLayers()) {
            Layer newLayer = new Layer();
            newLayer.override(layer, keepMeta, suffix, clonedFrom);
            this.layers.add(newLayer);
        }
        for (System system : source.getSystems()) {
            System newSystem = new System();
            newSystem.override(system, keepMeta, suffix, clonedFrom);
            this.systems.add(newSystem);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            newContainer.override(container, keepMeta, suffix, clonedFrom);
            this.containers.add(newContainer);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            newComponent.override(component, keepMeta, suffix, clonedFrom);
            this.components.add(newComponent);
        }
        for (DeploymentNode deploymentNode : source.getDeploymentNodes()) {
            DeploymentNode newDeploymentNode = new DeploymentNode();
            newDeploymentNode.override(deploymentNode, keepMeta, suffix, clonedFrom);
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

        if (source.getLayers() != null && !source.getLayers().isEmpty()) {
            for (Layer system : source.getLayers()) {
                Layer newLayer = new Layer();
                newLayer.copyNonEmpty(system, keepMeta);
                this.layers.add(newLayer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                newSystem.copyNonEmpty(system, keepMeta);
                this.systems.add(newSystem);
            }
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container system : source.getContainers()) {
                Container newContainer = new Container();
                newContainer.copyNonEmpty(system, keepMeta);
                this.containers.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component system : source.getComponents()) {
                Component newComponent = new Component();
                newComponent.copyNonEmpty(system, keepMeta);
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
}