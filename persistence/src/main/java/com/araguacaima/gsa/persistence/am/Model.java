package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.persistence.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A software architecture model.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Model", schema = "AM")
public class Model extends BaseEntity {

    @OneToMany
    @CollectionTable(name = "Model_Elements",
            schema = "AM")
    @MapKeyColumn(name = "Element")
    private final Map<String, Element> elementsById = new HashMap<>();

    @OneToMany
    @CollectionTable(name = "Model_Relationships",
            schema = "AM")
    @MapKeyColumn(name = "Relationship")
    private final Map<String, Relationship> relationshipsById = new HashMap<>();

    @OneToOne
    private Enterprise enterprise;

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Model_People",
            joinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Person_Id",
                    referencedColumnName = "Id")})
    private Set<Consumer> people = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Model_SoftwareSystems",
            joinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")})
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(schema = "AM",
            name = "Model_DeploymentNodes",
            joinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")})
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();

    public Model() {
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Map<String, Element> getElementsById() {
        return elementsById;
    }

    public Map<String, Relationship> getRelationshipsById() {
        return relationshipsById;
    }

    public Set<Consumer> getPeople() {
        return people;
    }

    public void setPeople(Set<Consumer> people) {
        this.people = people;
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
}