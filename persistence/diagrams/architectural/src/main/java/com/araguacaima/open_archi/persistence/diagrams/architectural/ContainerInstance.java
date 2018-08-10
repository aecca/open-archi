package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
public class ContainerInstance extends Element {

    @Column
    private String containerId;

    @Column
    private int instanceId;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.CONTAINER;

    public ContainerInstance() {
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

    public void override(ContainerInstance source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setContainerId(source.getContainerId());
        this.setInstanceId(source.getInstanceId());
    }

    public void copyNonEmpty(ContainerInstance source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getContainerId() != null) {
            this.setContainerId(source.getContainerId());
        }
        if (source.getInstanceId() != 0) {
            this.setInstanceId(source.getInstanceId());
        }
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public void setGroup(boolean container) {

    }
}