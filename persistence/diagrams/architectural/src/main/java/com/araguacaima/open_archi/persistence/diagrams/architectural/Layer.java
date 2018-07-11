package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = Layer.GET_ALL_LAYERS,
                query = "select l from Layer l"),
        @NamedQuery(name = Layer.GET_LAYER,
                query = "select l from Layer l where l.id=:lid")})
public class Layer extends StaticElement {

    public static final String GET_ALL_LAYERS = "get.all.layers";
    public static final String GET_LAYER = "get.layer";

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Layer_Items",
            joinColumns = {@JoinColumn(name = "Layer_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Item_Id",
                    referencedColumnName = "Id")})
    private Set<Item> items = new LinkedHashSet<>();

    public static final String SHAPE_COLOR = "#01203A";

    public Layer() {
        setKind(ElementKind.LAYER);
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void override(Layer source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        for (Item item : source.getItems()) {
            Item newItem = new Item();
            newItem.override(item, keepMeta, suffix);
            this.items.add(newItem);
        }
    }

    public void copyNonEmpty(Layer source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getItems() != null && !source.getItems().isEmpty()) {
            for (Item item : source.getItems()) {
                Item newItem = new Item();
                newItem.copyNonEmpty(item, keepMeta);
                this.items.add(newItem);
            }
        }
    }
}
