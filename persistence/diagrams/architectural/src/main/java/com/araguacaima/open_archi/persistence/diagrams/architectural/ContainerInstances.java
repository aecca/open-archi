package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Elements;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class ContainerInstances extends Elements {

    @Column
    private String containerId;

    @Column
    private int instanceId;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.CONTAINER;

    public ContainerInstances() {
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Collection<BaseEntity> override(ContainerInstances source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setContainerId(source.getContainerId());
        this.setInstanceId(source.getInstanceId());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(ContainerInstances source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getContainerId() != null) {
            this.setContainerId(source.getContainerId());
        }
        if (source.getInstanceId() != 0) {
            this.setInstanceId(source.getInstanceId());
        }
        return overriden;
    }

    @Override
    public boolean isIsGroup() {
        return false;
    }

    @Override
    public void setIsGroup(boolean container) {

    }
}