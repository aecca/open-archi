package com.araguacaima.gsa.persistence.diagrams.architectural;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;

/**
 * Represents a deployment instance of a {@link Container}, which can be added to a {@link DeploymentNode}.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "ContainerInstance", schema = "DIAGRAMS")
public class ContainerInstance extends Element {

    @OneToOne
    private Container container;

    @Column
    private String containerId;

    @Column
    private int instanceId;

    @OneToOne
    private Model model;

    @Column
    private ElementKind kind = ElementKind.ARCHITECTURAL_MODEL;

    public ContainerInstance() {
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}