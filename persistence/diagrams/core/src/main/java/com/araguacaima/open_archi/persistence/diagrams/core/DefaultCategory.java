package com.araguacaima.open_archi.persistence.diagrams.core;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class DefaultCategory implements ItemCategory<DefaultCategoryItem> {
    @Id
    @Enumerated(EnumType.STRING)
    private DefaultCategoryItem type;

    @Override
    public DefaultCategoryItem getType() {
        return this.type;
    }

    @Override
    public void setType(DefaultCategoryItem type) {
        this.type = type;
    }
}

