package com.araguacaima.open_archi.persistence.diagrams.gantt;


import com.araguacaima.open_archi.persistence.diagrams.core.ItemCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Category implements ItemCategory<GanttCategory> {

    @Id
    @Enumerated(EnumType.STRING)
    private GanttCategory type;

    @Override
    public GanttCategory getType() {
        return this.type;
    }

    @Override
    public void setType(GanttCategory type) {
        this.type = type;
    }
}

enum GanttCategory {
    ACTIVITY,
    START,
    END
}
