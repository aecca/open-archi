package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
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
                query = "select m from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m where m.prototype=true"),
        @NamedQuery(name = Model.GET_ALL_RELATIONSHIPS,
                query = "select m.relationships from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m where m.id=:id"),
        @NamedQuery(name = Model.GET_ALL_CONSUMERS_FOR_MODEL,
                query = "select m.consumers from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m where m.id=:id"),
        @NamedQuery(name = Model.GET_CONSUMER_FOR_MODEL,
                query = "select c from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m JOIN m.consumers c where m.id=:id and c.id=:cid"),
        @NamedQuery(name = Model.GET_ALL_SYSTEMS_FROM_MODEL,
                query = "select m.systems from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m where m.id=:id"),
        @NamedQuery(name = Model.GET_SYSTEM,
                query = "select s from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m JOIN m.systems s where m.id=:id and s.id=:sid"),
        @NamedQuery(name = Model.GET_MODELS_USAGE_BY_ELEMENT_ID_LIST,
                query = "select m " +
                        "from com.araguacaima.open_archi.persistence.diagrams.architectural.Model m " +
                        "   left join m.layers lay " +
                        "   left join m.systems sys " +
                        "   left join m.containers con " +
                        "   left join m.components com " +
                        "where lay.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or sys.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or con.id in :" + Item.ELEMENTS_USAGE_PARAM +
                        "   or com.id in :" + Item.ELEMENTS_USAGE_PARAM)})
public class Model extends ModelElement implements DiagramableElement<Model> {

    public static final String GET_ALL_MODEL_PROTOTYPES = "get.all.model.prototypes";
    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships";
    public static final String GET_ALL_CONSUMERS_FOR_MODEL = "get.all.consumers.for.model";
    public static final String GET_CONSUMER_FOR_MODEL = "get.consumer.for.model";
    public static final String GET_ALL_SYSTEMS_FROM_MODEL = "get.all.systems.from.model";
    public static final String GET_SYSTEM = "get.system";
    public static final String GET_MODELS_USAGE_BY_ELEMENT_ID_LIST = "get.models.usage.by.element.id.list";

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
    public Collection<BaseEntity> override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (Consumer consumer : source.getConsumers()) {
            Consumer newConsumer = new Consumer();
            overriden.addAll(newConsumer.override(consumer, keepMeta, suffix, clonedFrom));
            this.consumers.add(newConsumer);
            overriden.add(newConsumer);
        }
        for (Layer layer : source.getLayers()) {
            Layer newLayer = new Layer();
            overriden.addAll(newLayer.override(layer, keepMeta, suffix, clonedFrom));
            this.layers.add(newLayer);
            overriden.add(newLayer);
        }
        for (System system : source.getSystems()) {
            System newSystem = new System();
            overriden.addAll(newSystem.override(system, keepMeta, suffix, clonedFrom));
            this.systems.add(newSystem);
            overriden.add(newSystem);
        }
        for (Container container : source.getContainers()) {
            Container newContainer = new Container();
            overriden.addAll(newContainer.override(container, keepMeta, suffix, clonedFrom));
            this.containers.add(newContainer);
            overriden.add(newContainer);
        }
        for (Component component : source.getComponents()) {
            Component newComponent = new Component();
            overriden.addAll(newComponent.override(component, keepMeta, suffix, clonedFrom));
            this.components.add(newComponent);
            overriden.add(newComponent);
        }
        for (DeploymentNode deploymentNode : source.getDeploymentNodes()) {
            DeploymentNode newDeploymentNode = new DeploymentNode();
            overriden.addAll(newDeploymentNode.override(deploymentNode, keepMeta, suffix, clonedFrom));
            this.deploymentNodes.add(newDeploymentNode);
            overriden.add(newDeploymentNode);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Model source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getConsumers() != null && !source.getConsumers().isEmpty()) {
            for (Consumer consumer : source.getConsumers()) {
                Consumer newConsumer = new Consumer();
                overriden.addAll(newConsumer.copyNonEmpty(consumer, keepMeta));
                this.consumers.add(newConsumer);
                overriden.add(newConsumer);
            }
        }
        if (source.getLayers() != null && !source.getLayers().isEmpty()) {
            for (Layer system : source.getLayers()) {
                Layer newLayer = new Layer();
                overriden.addAll(newLayer.copyNonEmpty(system, keepMeta));
                this.layers.add(newLayer);
                overriden.add(newLayer);
            }
        }
        if (source.getSystems() != null && !source.getSystems().isEmpty()) {
            for (System system : source.getSystems()) {
                System newSystem = new System();
                overriden.addAll(newSystem.copyNonEmpty(system, keepMeta));
                this.systems.add(newSystem);
                overriden.add(newSystem);
            }
        }
        if (source.getContainers() != null && !source.getContainers().isEmpty()) {
            for (Container system : source.getContainers()) {
                Container newContainer = new Container();
                overriden.addAll(newContainer.copyNonEmpty(system, keepMeta));
                this.containers.add(newContainer);
                overriden.add(newContainer);
            }
        }
        if (source.getComponents() != null && !source.getComponents().isEmpty()) {
            for (Component system : source.getComponents()) {
                Component newComponent = new Component();
                overriden.addAll(newComponent.copyNonEmpty(system, keepMeta));
                this.components.add(newComponent);
                overriden.add(newComponent);
            }
        }
        if (source.getDeploymentNodes() != null && !source.getDeploymentNodes().isEmpty()) {
            for (DeploymentNode deploymentNode : source.getDeploymentNodes()) {
                DeploymentNode newDeploymentNode = new DeploymentNode();
                overriden.addAll(newDeploymentNode.copyNonEmpty(deploymentNode, keepMeta));
                this.deploymentNodes.add(newDeploymentNode);
                overriden.add(newDeploymentNode);
            }
        }
        return overriden;
    }
}