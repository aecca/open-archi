package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.ItemCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Category implements ItemCategory<FlowchartCategory> {
    @Id
    @Enumerated(EnumType.STRING)
    private FlowchartCategory type;

    public FlowchartCategory getType() {
        return type;
    }

    public void setType(FlowchartCategory category) {
        this.type = category;
    }
}

