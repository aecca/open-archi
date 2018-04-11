package com.araguacaima.open_archi.persistence.diagrams.classes;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@PersistenceUnit(unitName = "open-archi")
public class UmlClass extends UmlItem {

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlClass_Fields",
            joinColumns = {@JoinColumn(name = "UmlClass_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Field_Id",
                    referencedColumnName = "Id")})
    private Map<String, UmlField> fields = new HashMap<>();
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlClass_Methods",
            joinColumns = {@JoinColumn(name = "UmlClass_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Method_Id",
                    referencedColumnName = "Id")})
    private Map<String, UmlMethod> methods = new HashMap<>();

    public Map<String, UmlField> getFields() {
        return fields;
    }

    public void setFields(Map<String, UmlField> fields) {
        this.fields = fields;
    }

    public Map<String, UmlMethod> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, UmlMethod> methods) {
        this.methods = methods;
    }

    public void override(UmlClass source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.fields = source.getFields();
        this.methods = source.getMethods();
    }

    public void copyNonEmpty(UmlClass source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getFields() != null && !source.getFields().isEmpty()) {
            this.fields = source.getFields();
        }
        if (source.getMethods() != null && !source.getMethods().isEmpty()) {
            this.methods = source.getMethods();
        }
    }

}
