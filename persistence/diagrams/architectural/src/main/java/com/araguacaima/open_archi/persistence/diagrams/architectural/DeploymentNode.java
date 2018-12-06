package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
@PersistenceUnit(unitName = "open-archi")
public class DeploymentNode extends Element implements DiagramableElement<DeploymentNode> {

    @Column
    private String technology;

    @Column
    private int instances = 0;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.DEPLOYMENT;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "DeploymentNode_ContainerInstances",
            joinColumns = {@JoinColumn(name = "DeploymentNode_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "ContainerInstance_Id",
                    referencedColumnName = "Id")})
    private Set<ContainerInstance> containerInstances = new HashSet<>();

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

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Set<ContainerInstance> getContainerInstances() {
        return containerInstances;
    }

    public void setContainerInstances(Set<ContainerInstance> containerInstances) {
        this.containerInstances = containerInstances;
    }

    @Override
    public Collection<BaseEntity> override(DeploymentNode source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setInstances(source.getInstances());
        this.setTechnology(source.getTechnology());
        for (ContainerInstance container : source.getContainerInstances()) {
            ContainerInstance newContainerInstance = new ContainerInstance();
            overriden.addAll(newContainerInstance.override(container, keepMeta, suffix, clonedFrom));
            if (!this.containerInstances.add(newContainerInstance)) {
                this.containerInstances.remove(newContainerInstance);
                this.containerInstances.add(newContainerInstance);
            }
            overriden.add(newContainerInstance);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(DeploymentNode source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getInstances() != 0) {
            this.setInstances(source.getInstances());
        }
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getContainerInstances() != null && !source.getContainerInstances().isEmpty()) {
            for (ContainerInstance container : source.getContainerInstances()) {
                ContainerInstance newContainerInstance = new ContainerInstance();
                overriden.addAll(newContainerInstance.copyNonEmpty(container, keepMeta));
                if (!this.containerInstances.add(newContainerInstance)) {
                    this.containerInstances.remove(newContainerInstance);
                    this.containerInstances.add(newContainerInstance);
                }
                overriden.add(newContainerInstance);
            }
        }
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return true;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}