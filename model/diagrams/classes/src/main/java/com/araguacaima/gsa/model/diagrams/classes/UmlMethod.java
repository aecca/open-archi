package com.araguacaima.gsa.model.diagrams.classes;

import java.util.Collection;

public class UmlMethod {
    private String name;
    private String type;
    private Collection<UmlParameter> parameters;
    private Visibility visibility = Visibility.PACKAGE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<UmlParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<UmlParameter> parameters) {
        this.parameters = parameters;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
