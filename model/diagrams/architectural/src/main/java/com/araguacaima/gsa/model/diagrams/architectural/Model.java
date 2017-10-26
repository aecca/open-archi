package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;
import com.araguacaima.gsa.model.diagrams.core.Item;
import com.araguacaima.gsa.model.diagrams.core.SequentialIdGeneratorStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A software architecture model.
 */
public class Model extends Element {

    private final Map<String, Element> elementsById = new HashMap<>();
    private final Map<String, com.araguacaima.gsa.model.diagrams.core.Relationship> relationshipsById = new HashMap<>();
    private SequentialIdGeneratorStrategy idGenerator = new SequentialIdGeneratorStrategy();

    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;
    private Set<Consumer> people = new LinkedHashSet<>();
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();

    public Model() {
    }

    /**
     * Creates a software system (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(String name, String description) {
        return addSoftwareSystem(Scope.Unspecified, name, description);
    }

    /**
     * Creates a software system and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location    the location of the software system (e.g. internal, external, etc)
     * @param name        the name of the software system
     * @param description a short description of the software system
     * @return the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(Scope location, String name, String description) {
        if (getSoftwareSystemWithName(name) == null) {
            SoftwareSystem softwareSystem = new SoftwareSystem();
            softwareSystem.setScope(location);
            softwareSystem.setName(name);
            softwareSystem.setDescription(description);

            softwareSystems.add(softwareSystem);

            softwareSystem.setId(idGenerator.generateId(softwareSystem));
            softwareSystem.setModel(this);
            addElementToInternalStructures(softwareSystem);

            return softwareSystem;
        } else {
            throw new IllegalArgumentException("A software system named '" + name + "' already exists.");
        }
    }

    /**
     * Creates a person (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     */
    public Consumer addPerson(String name, String description) {
        return addPerson(Scope.Unspecified, name, description);
    }

    /**
     * Creates a person and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location    the location of the person (e.g. internal, external, etc)
     * @param name        the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description a short description of the person
     * @return the Person instance created and added to the model (or null)
     */
    public Consumer addPerson(Scope location, String name, String description) {
        if (getPersonWithName(name) == null) {
            Consumer person = new Consumer();
            person.setScope(location);
            person.setName(name);
            person.setDescription(description);

            people.add(person);

            person.setId(idGenerator.generateId(person));
            person.setModel(this);
            addElementToInternalStructures(person);

            return person;
        } else {
            throw new IllegalArgumentException("A person named '" + name + "' already exists.");
        }
    }

    Container addContainer(SoftwareSystem parent, String name, String description, String technology) {
        if (parent.getContainerWithName(name) == null) {
            Container container = new Container();
            container.setName(name);
            container.setDescription(description);
            container.setTechnology(technology);

            container.setParent(parent);
            parent.add(container);

            container.setId(idGenerator.generateId(container));
            container.setModel(this);
            addElementToInternalStructures(container);

            return container;
        } else {
            throw new IllegalArgumentException("A container named '" + name + "' already exists for this software system.");
        }
    }

    Component<Item> addComponentOfType(Container parent, String name, String type, String description, String technology) {
        Component<Item> component = new Component<>();
        component.setName(name);
        component.addFeature(type);
        component.setDescription(description);
        component.setTechnology(technology);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        component.setModel(this);
        addElementToInternalStructures(component);

        return component;
    }

    Component<Item> addComponent(Container parent, String name, String description) {
        Component<Item> component = new Component<>();
        component.setName(name);
        component.setDescription(description);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        addElementToInternalStructures(component);

        return component;
    }

    Relationship addRelationship(Element source, Element destination, String description) {
        return addRelationship(source, destination, description, null);
    }

    Relationship addRelationship(Element source, Element destination, String description, String technology) {
        return addRelationship(source, destination, description, technology, InteractionStyle.Synchronous);
    }

    Relationship addRelationship(Element source, Element destination, String description, String technology, InteractionStyle interactionStyle) {
        if (destination == null) {
            throw new IllegalArgumentException("The destination must be specified.");

        }

        Relationship relationship = new Relationship(source, destination, description, technology, interactionStyle);
        if (addRelationship(relationship)) {
            return relationship;
        } else {
            return null;
        }
    }

    public boolean addRelationship(Relationship relationship) {
        if (!relationship.getSource().has(relationship)) {
            relationship.setId(idGenerator.generateId(relationship));
            relationship.getSource().addRelationship(relationship);

            addRelationshipToInternalStructures(relationship);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ElementKind getKind() {
        return this.kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    private void addElementToInternalStructures(Element element) {
        elementsById.put(element.getId(), element);
        idGenerator.found(element.getId());
    }

    private void addRelationshipToInternalStructures(com.araguacaima.gsa.model.diagrams.core.Relationship relationship) {
        relationshipsById.put(relationship.getId(), relationship);
        idGenerator.found(relationship.getId());
    }

    /**
     * @return a set containing all elements in this model.
     */
    @JsonIgnore
    public Set<Element> getElements() {
        return new HashSet<>(this.elementsById.values());
    }

    /**
     * @param id the {@link Element#getId()} of the element
     * @return the element in this model with the specified ID (or null if it doesn't exist).
     * @see Element#getId()
     */
    public Element getElement(String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("An ID must be specified.");
        }


        return elementsById.get(id);
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return null;
    }

    /**
     * @param id the {@link Relationship#getId()} of the relationship
     * @return the relationship in this model with the specified ID (or null if it doesn't exist).
     * @see Relationship#getId()
     */
    public com.araguacaima.gsa.model.diagrams.core.Relationship getRelationship(String id) {
        return relationshipsById.get(id);
    }

    /**
     * @return a collection containing all of the Person instances in this model.
     */
    public Collection<Consumer> getPeople() {
        return new LinkedHashSet<>(people);
    }

    /**
     * @return a collection containing all of the SoftwareSystem instances in this model.
     */
    public Set<SoftwareSystem> getSoftwareSystems() {
        return new LinkedHashSet<>(softwareSystems);
    }

    /**
     * @return a collection containing all of the DeploymentNode instances in this model.
     */
    public Set<DeploymentNode> getDeploymentNodes() {
        return new LinkedHashSet<>(deploymentNodes);
    }

    public void hydrate() {
        // add all of the elements to the model
        people.forEach(this::addElementToInternalStructures);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            softwareSystem.setModel(this);
            addElementToInternalStructures(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                container.setModel(this);
                softwareSystem.add(container);
                addElementToInternalStructures(container);
                container.setParent(softwareSystem);
                Set<Component> components = container.getComponents();
                for (Component component : components) {
                    component.setModel(this);
                    container.add(component);
                    addElementToInternalStructures(component);
                    component.setParent(container);
                }
            }
        }

        deploymentNodes.forEach(dn -> hydrateDeploymentNode(dn, null));

        // now hydrate the relationships
        people.forEach(this::hydrateRelationships);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            hydrateRelationships(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                hydrateRelationships(container);
                Set<Component> components = container.getComponents();
                for (Component component : components) {
                    hydrateRelationships(component);
                }
            }
        }

        deploymentNodes.forEach(this::hydrateDeploymentNodeRelationships);
    }

    private void hydrateDeploymentNode(DeploymentNode deploymentNode, DeploymentNode parent) {
        deploymentNode.setParent(parent);
        deploymentNode.setModel(this);
        addElementToInternalStructures(deploymentNode);

        deploymentNode.getChildren().forEach(child -> hydrateDeploymentNode(child, deploymentNode));

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            Element element = getElement(containerInstance.getContainerId());
            containerInstance.setContainer((Container) element);
            containerInstance.setModel(this);
            addElementToInternalStructures(containerInstance);
        }
    }

    private void hydrateDeploymentNodeRelationships(DeploymentNode deploymentNode) {
        hydrateRelationships(deploymentNode);
        deploymentNode.getChildren().forEach(this::hydrateDeploymentNodeRelationships);
        deploymentNode.getContainerInstances().forEach(this::hydrateRelationships);
    }

    private void hydrateRelationships(Element element) {
        Set<com.araguacaima.gsa.model.diagrams.core.Relationship> relationships = element.getRelationships();
        for (com.araguacaima.gsa.model.diagrams.core.Relationship relationship : relationships) {
            relationship.setSource(getElement(relationship.getSourceId()));
            relationship.setDestination(getElement(relationship.getDestinationId()));
            addRelationshipToInternalStructures(relationship);
        }
    }

    /**
     * Determines whether this model contains the specified element.
     *
     * @param element any element
     * @return true, if the element is contained in this model
     */
    public boolean contains(Element element) {
        return elementsById.values().contains(element);
    }

    /**
     * @param name the name of a {@link SoftwareSystem}
     * @return the SoftwareSystem instance with the specified name (or null if it doesn't exist).
     */
    public SoftwareSystem getSoftwareSystemWithName(String name) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * @param id the {@link SoftwareSystem#getId()} of the softwaresystem
     * @return Gets the SoftwareSystem instance with the specified ID (or null if it doesn't exist).
     * @see SoftwareSystem#getId()
     */
    public SoftwareSystem getSoftwareSystemWithId(String id) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getId().equals(id)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * @param name the name of the person
     * @return the Person instance with the specified name (or null if it doesn't exist).
     */
    public Consumer getPersonWithName(String name) {
        for (Consumer person : getPeople()) {
            if (person.getName().equals(name)) {
                return person;
            }
        }

        return null;
    }

    /**
     * <p>Propagates all relationships from children to their parents. For example, if you have two components (AAA and BBB)
     * in different software systems that have a relationship, calling this method will add the following
     * additional implied relationships to the model: AAA-&gt;BB AAA--&gt;B AA-&gt;BBB AA-&gt;BB AA-&gt;B A-&gt;BBB A-&gt;BB A-&gt;B.</p>
     *
     * @return a set of all implicit relationships
     */
    public Set<Relationship> addImplicitRelationships() {
        Set<Relationship> implicitRelationships = new HashSet<>();

        String descriptionKey = "D";
        String technologyKey = "T";
        Map<Element, Map<Element, Map<String, HashSet<String>>>> candidateRelationships = new HashMap<>();

        Set<com.araguacaima.gsa.model.diagrams.core.Relationship> relationships = getRelationships();
        for (com.araguacaima.gsa.model.diagrams.core.Relationship relationship : relationships) {
            Element source = (Element) relationship.getSource();
            Element destination = (Element) relationship.getDestination();

            while (source != null) {
                while (destination != null) {
                    if (!source.hasEfferentRelationshipWith(destination)) {
                        if (propagatedRelationshipIsAllowed(source, destination)) {

                            if (!candidateRelationships.containsKey(source)) {
                                candidateRelationships.put(source, new HashMap<>());
                            }

                            if (!candidateRelationships.get(source).containsKey(destination)) {
                                candidateRelationships.get(source).put(destination, new HashMap<>());
                                candidateRelationships.get(source).get(destination).put(descriptionKey, new HashSet<>());
                                candidateRelationships.get(source).get(destination).put(technologyKey, new HashSet<>());
                            }

                            if (relationship.getDescription() != null) {
                                candidateRelationships.get(source).get(destination).get(descriptionKey).add(relationship.getDescription());
                            }

                            if (((Relationship) relationship).getTechnology() != null) {
                                candidateRelationships.get(source).get(destination).get(technologyKey).add(((Relationship) relationship).getTechnology());
                            }
                        }
                    }

                    destination = (Element) destination.getParent();
                }

                destination = (Element) relationship.getDestination();
                source = (Element) source.getParent();
            }
        }

        for (Element source : candidateRelationships.keySet()) {
            for (Element destination : candidateRelationships.get(source).keySet()) {
                Set<String> possibleDescriptions = candidateRelationships.get(source).get(destination).get(descriptionKey);
                Set<String> possibleTechnologies = candidateRelationships.get(source).get(destination).get(technologyKey);

                String description = "";
                if (possibleDescriptions.size() == 1) {
                    description = possibleDescriptions.iterator().next();
                }

                String technology = "";
                if (possibleTechnologies.size() == 1) {
                    technology = possibleTechnologies.iterator().next();
                }

                Relationship implicitRelationship = addRelationship(source, destination, description, technology);
                if (implicitRelationship != null) {
                    implicitRelationships.add(implicitRelationship);
                }
            }
        }

        return implicitRelationships;
    }

    private boolean propagatedRelationshipIsAllowed(Item source, Item destination) {
        if (source.equals(destination)) {
            return false;
        }

        if (source.getParent() != null) {
            if (destination.equals(source.getParent())) {
                return false;
            }

            if (source.getParent().getParent() != null) {
                if (destination.equals(source.getParent().getParent())) {
                    return false;
                }
            }
        }

        if (destination.getParent() != null) {
            if (source.equals(destination.getParent())) {
                return false;
            }

            if (destination.getParent().getParent() != null) {
                if (source.equals(destination.getParent().getParent())) {
                    return false;
                }
            }
        }

        return true;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return people.isEmpty() && softwareSystems.isEmpty();
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        return addDeploymentNode(null, name, description, technology, instances, properties);
    }

    DeploymentNode addDeploymentNode(DeploymentNode parent, String name, String description, String technology, int instances, Map<String, String> properties) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if ((parent == null && getDeploymentNodeWithName(name) == null) || (parent != null && parent.getDeploymentNodeWithName(name) == null)) {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setName(name);
            deploymentNode.setDescription(description);
            deploymentNode.setTechnology(technology);
            deploymentNode.setParent(parent);
            deploymentNode.setInstances(instances);
            if (properties != null) {
                deploymentNode.setProperties(properties);
            }

            if (parent == null) {
                deploymentNodes.add(deploymentNode);
            }

            deploymentNode.setId(idGenerator.generateId(deploymentNode));
            deploymentNode.setModel(this);
            addElementToInternalStructures(deploymentNode);

            return deploymentNode;
        } else {
            throw new IllegalArgumentException("A deployment node named '" + name + "' already exists.");
        }
    }

    /**
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        for (DeploymentNode deploymentNode : getDeploymentNodes()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    ContainerInstance addContainerInstance(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        long instanceNumber = getElements().stream().filter(e -> e instanceof ContainerInstance && ((ContainerInstance) e).getContainer().equals(container)).count();
        instanceNumber++;
        ContainerInstance containerInstance = new ContainerInstance(container, (int) instanceNumber);
        containerInstance.setId(idGenerator.generateId(containerInstance));

        // find all ContainerInstance objects
        Set<ContainerInstance> containerInstances = getElements().stream()
                .filter(e -> e instanceof ContainerInstance)
                .map(e -> (ContainerInstance) e)
                .collect(Collectors.toSet());

        // and replicate the container-container relationships
        for (ContainerInstance ci : containerInstances) {
            Container c = ci.getContainer();

            Set<com.araguacaima.gsa.model.diagrams.core.Relationship> relationships = container.getRelationships();
            for (com.araguacaima.gsa.model.diagrams.core.Relationship relationship : relationships) {
                Relationship relationship_ = (Relationship) relationship;
                if (relationship_.getDestination().equals(c)) {
                    addRelationship(containerInstance, ci, relationship_.getDescription(), relationship_.getTechnology(), relationship_.getInteractionStyle());
                }
            }

            Set<com.araguacaima.gsa.model.diagrams.core.Relationship> relationships1 = c.getRelationships();
            for (com.araguacaima.gsa.model.diagrams.core.Relationship relationship : relationships1) {
                Relationship relationship_ = (Relationship) relationship;
                if (relationship.getDestination().equals(container)) {
                    addRelationship(ci, containerInstance, relationship_.getDescription(), relationship_.getTechnology(), relationship_.getInteractionStyle());
                }
            }
        }
        containerInstance.setModel(this);
        addElementToInternalStructures(containerInstance);

        return containerInstance;
    }

    public Element getElementWithCanonicalName(String canonicalName) {
        if (canonicalName == null || canonicalName.trim().length() == 0) {
            throw new IllegalArgumentException("A canonical name must be specified.");
        }

        // canonical names start with a leading slash, so add this if it's missing
        if (!canonicalName.startsWith("/")) {
            canonicalName = "/" + canonicalName;
        }

        for (Element element : getElements()) {
            if (element.getCanonicalName().equals(canonicalName)) {
                return element;
            }
        }

        return null;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return null;
    }

 }