package com.araguacaima.gsa.persistence.diagrams.classes;

import java.util.HashMap;
import java.util.Map;

public class UmlClass extends UmlItem {

    private Map<String, UmlField> fields = new HashMap<>();
    private Map<String, UmlMethod> methods = new HashMap<>();

    /**
     * Gets the collection of name-value fields pairs associated with this element, as a Map.
     *
     * @return a Map (String, String) (empty if there are no fields)
     */
    public Map<String, UmlField> getFields() {
        return new HashMap<>(fields);
    }

    public void setFields(Map<String, UmlField> fields) {
        if (fields != null) {
            this.fields = fields;
        }
    }

    /**
     * Gets the collection of name-value methods pairs associated with this element, as a Map.
     *
     * @return a Map (String, String) (empty if there are no methods)
     */
    public Map<String, UmlMethod> getMethods() {
        return new HashMap<>(methods);
    }

    public void setMethods(Map<String, UmlMethod> methods) {
        if (methods != null) {
            this.methods = methods;
        }
    }
}
