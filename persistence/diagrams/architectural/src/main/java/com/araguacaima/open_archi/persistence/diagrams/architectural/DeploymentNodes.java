package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Elements;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;

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
public class DeploymentNodes extends Elements {

    private String technology;
    private int instances = 1;
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

    public Collection<BaseEntity> override(DeploymentNodes source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setInstances(source.getInstances());
        this.setTechnology(source.getTechnology());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(DeploymentNodes source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getInstances() != 0) {
            this.setInstances(source.getInstances());
        }
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
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