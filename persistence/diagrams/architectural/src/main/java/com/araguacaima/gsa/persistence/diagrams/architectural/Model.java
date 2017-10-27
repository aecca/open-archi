package com.araguacaima.gsa.persistence.diagrams.architectural;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Relationship;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A software architecture model.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Model", schema = "DIAGRAMS")
public class Model extends Element {

    private final Map<String, Element> elementsById = new HashMap<>();
    private final Map<String, com.araguacaima.gsa.persistence.diagrams.core.Relationship> relationshipsById = new HashMap<>();

    @Column
    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_People",
            joinColumns = {@JoinColumn(name = "People_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "People_Id",
                    referencedColumnName = "Id")})
    private Set<Consumer> people = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_SoftwareSystems",
            joinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")})
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_DeploymentNodes",
            joinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")})
    private Set<DeploymentNode> deploymentNodes = new LinkedHashSet<>();

    public Model() {
    }

    public Map<String, Element> getElementsById() {
        return elementsById;
    }

    public Map<String, Relationship> getRelationshipsById() {
        return relationshipsById;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
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