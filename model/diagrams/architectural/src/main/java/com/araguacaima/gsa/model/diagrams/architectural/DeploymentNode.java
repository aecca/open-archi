package com.araguacaima.gsa.model.diagrams.architectural;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;
import com.araguacaima.gsa.model.diagrams.core.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.araguacaima.gsa.model.diagrams.architectural.StaticElement.CANONICAL_NAME_SEPARATOR;

/**
 * <p>
 * Represents a deployment node, which is something like:
 * </p>
 * <p>
 * <ul>
 * <li>Physical infrastructure (e.g. a physical server or device)</li>
 * <li>Virtualised infrastructure (e.g. IaaS, PaaS, a virtual machine)</li>
 * <li>Containerised infrastructure (e.g. a Docker container)</li>
 * <li>Database server</li>
 * <li>Java EE web/application server</li>
 * <li>Microsoft IIS</li>
 * <li>etc</li>
 * </ul>
 */
public class DeploymentNode extends Element {

    private Model model;
    private DeploymentNode parent;
    private String technology;
    private int instances = 1;
    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;

    private Set<DeploymentNode> children = new HashSet<>();
    private Set<ContainerInstance> containerInstances = new HashSet<>();

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    /**
     * Adds a container instance to this deployment node.
     *
     * @param container the Container to add an instance of
     * @return a ContainerInstance object
     */
    public ContainerInstance add(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("A container must be specified.");
        }

        ContainerInstance containerInstance = getModel().addContainerInstance(container);
        this.containerInstances.add(containerInstance);

        return containerInstance;
    }

    /**
     * Adds a child deployment node.
     *
     * @param name        the name of the deployment node
     * @param description a short description
     * @param technology  the technology
     * @return a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology) {
        return addDeploymentNode(name, description, technology, 1);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name        the name of the deployment node
     * @param description a short description
     * @param technology  the technology
     * @param instances   the number of instances
     * @return a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances) {
        return addDeploymentNode(name, description, technology, instances, null);
    }

    /**
     * Adds a child deployment node.
     *
     * @param name        the name of the deployment node
     * @param description a short description
     * @param technology  the technology
     * @param instances   the number of instances
     * @param properties  a Map (String,String) describing name=value properties
     * @return a DeploymentNode object
     */
    public DeploymentNode addDeploymentNode(String name, String description, String technology, int instances, Map<String, String> properties) {
        DeploymentNode deploymentNode = getModel().addDeploymentNode(this, name, description, technology, instances, properties);
        if (deploymentNode != null) {
            children.add(deploymentNode);
        }
        return deploymentNode;
    }

    /**
     * Gets the DeploymentNode with the specified name.
     *
     * @param name the name of the deployment node
     * @return the DeploymentNode instance with the specified name (or null if it doesn't exist).
     */
    public DeploymentNode getDeploymentNodeWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        for (DeploymentNode deploymentNode : getChildren()) {
            if (deploymentNode.getName().equals(name)) {
                return deploymentNode;
            }
        }

        return null;
    }

    /**
     * Adds a relationship between this and another deployment node.
     *
     * @param destination the destination DeploymentNode
     * @param description a short description of the relationship
     * @param technology  the technology
     * @return a Relationship object
     */
    public Relationship uses(DeploymentNode destination, String description, String technology) {
        return getModel().addRelationship(this, destination, description, technology);
    }

    /**
     * Gets the set of child deployment nodes.
     *
     * @return a Set of DeploymentNode objects
     */
    public Set<DeploymentNode> getChildren() {
        return new HashSet<>(children);
    }

    void setChildren(Set<DeploymentNode> children) {
        this.children = children;
    }

    /**
     * Gets the set of container instances associated with this deployment node.
     *
     * @return a Set of ContainerInstance objects
     */
    public Set<ContainerInstance> getContainerInstances() {
        return new HashSet<>(containerInstances);
    }

    /**
     * Gets the parent deployment node.
     *
     * @return the parent DeploymentNode, or null if there is no parent
     */
    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    void setParent(DeploymentNode parent) {
        this.parent = parent;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    @JsonIgnore
    protected Set<String> getRequiredTags() {
        // deployment nodes don't have any tags
        return new HashSet<>();
    }

    @JsonIgnore
    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String getTags() {
        // deployment nodes don't have any tags
        return "";
    }

    @Override
    public String formatForCanonicalName(String name) {
        return name.replace(CANONICAL_NAME_SEPARATOR, "");
    }

    @Override
    public String getCanonicalName() {
        if (getParent() != null) {
            return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        } else {
            return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
        }
    }

}