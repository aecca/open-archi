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
        query = "select a.relationships from Model a where a.id=:id"),})
public class Models extends Elements {

    public static final String GET_ALL_RELATIONSHIPS = "get.all.relationships_list";
    @OneToMany
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
    @ManyToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_People",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Consumer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Consumer_Id",
                    referencedColumnName = "Id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))})
    private Set<Consumers> consumers = new LinkedHashSet<>();
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Architecture_Model_SoftwareSystems",
            joinColumns = {@JoinColumn(name = "Architecture_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "SoftwareSystem_Id",
                    referencedColumnName = "Id")})
    private Set<SoftwareSystems> softwareSystems = new LinkedHashSet<>();
    @OneToMany
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

    public Set<SoftwareSystems> getSoftwareSystems() {
        return softwareSystems;
    }

    public void setSoftwareSystems(Set<SoftwareSystems> softwareSystems) {
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
}