package com.araguacaima.gsa.persistence.diagrams.classes;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@PersistenceContext(unitName = "diagrams")
@Table(name = "UmlClass", schema = "DIAGRAMS")
public class UmlClass extends UmlItem {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlClass_Fields",
            joinColumns = {@JoinColumn(name = "Fields_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Fields_Id",
                    referencedColumnName = "Id")})
    private Map<String, UmlField> fields = new HashMap<>();
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlClass_Methods",
            joinColumns = {@JoinColumn(name = "Method_Id",
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
}
