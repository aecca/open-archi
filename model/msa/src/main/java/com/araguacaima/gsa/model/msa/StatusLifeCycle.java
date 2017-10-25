package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

import java.util.Collection;

public class StatusLifeCycle extends BaseEntity {

    private Collection<Status> ancestors;

    private Status current;

    private Collection<Status> descendants;

    public Collection<Status> getAncestors() {
        return ancestors;
    }

    public void setAncestors(Collection<Status> ancestors) {
        this.ancestors = ancestors;
    }

    public Status getCurrent() {
        return current;
    }

    public void setCurrent(Status current) {
        this.current = current;
    }

    public Collection<Status> getDescendants() {
        return descendants;
    }

    public void setDescendants(Collection<Status> descendants) {
        this.descendants = descendants;
    }
}
