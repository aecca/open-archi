package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Elements;

import javax.persistence.*;

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
public class DeploymentNodes extends Elements {

    @Column
    private String technology;

    @Column
    private int instances = 1;

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.DEPLOYMENT;

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

    public void override(DeploymentNodes source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setInstances(source.getInstances());
        this.setTechnology(source.getTechnology());
    }

    public void copyNonEmpty(DeploymentNodes source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getInstances() != 0) {
            this.setInstances(source.getInstances());
        }
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
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