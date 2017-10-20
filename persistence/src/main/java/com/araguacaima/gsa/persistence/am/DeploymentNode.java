package com.araguacaima.gsa.persistence.am;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "DeploymentNode", schema = "AM")
public class DeploymentNode extends Element {

    @OneToOne
    private DeploymentNode parent;

    @Column
    private String technology;

    @Column
    private int instances = 1;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "DeploymentNode_Children",
            joinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")})
    private Set<DeploymentNode> children = new HashSet<>();

    @OneToMany
    @JoinTable(schema = "AM",
            name = "DeploymentNode_ContainerInstances",
            joinColumns = {@JoinColumn(name = "ContainerInstance_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "ContainerInstance_Id",
                    referencedColumnName = "Id")})
    private Set<ContainerInstance> containerInstances = new HashSet<>();

    @Override
    public DeploymentNode getParent() {
        return parent;
    }

    public void setParent(DeploymentNode parent) {
        this.parent = parent;
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

    public Set<DeploymentNode> getChildren() {
        return children;
    }

    public void setChildren(Set<DeploymentNode> children) {
        this.children = children;
    }

    public Set<ContainerInstance> getContainerInstances() {
        return containerInstances;
    }

    public void setContainerInstances(Set<ContainerInstance> containerInstances) {
        this.containerInstances = containerInstances;
    }
}