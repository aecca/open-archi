package com.araguacaima.gsa.model.diagrams.architectural;

public class DeploymentStatus {

    private LifeCycle currentStatus;

    public LifeCycle getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(LifeCycle currentStatus) {
        this.currentStatus = currentStatus;
    }
}
