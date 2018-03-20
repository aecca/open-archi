package com.araguacaima.open_archi.persistence.diagrams.core;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Category implements ItemCategory<FeatureCategory> {

    @Id
    @Enumerated(EnumType.STRING)
    private FeatureCategory type;

    @Override
    public FeatureCategory getType() {
        return this.type;
    }

    @Override
    public void setType(FeatureCategory type) {
        this.type = type;
    }
}

enum FeatureCategory {
    CATEGORY_1,
    CATEGORY_2,
}

