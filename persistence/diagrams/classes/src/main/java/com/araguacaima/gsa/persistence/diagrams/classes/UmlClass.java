package com.araguacaima.gsa.persistence.diagrams.classes;

import java.util.HashMap;
import java.util.Map;

public class UmlClass extends UmlItem {

    private Map<String, UmlField> fields = new HashMap<>();
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
