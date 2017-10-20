package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.am.Model;
import com.araguacaima.gsa.model.common.BaseEntity;

public class Diagram extends BaseEntity {

    private Markdown description;

    private Model diagram;

    private String name;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Model getDiagram() {
        return diagram;
    }

    public void setDiagram(Model diagram) {
        this.diagram = diagram;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
