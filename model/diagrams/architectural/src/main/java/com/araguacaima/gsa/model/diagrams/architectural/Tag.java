package com.araguacaima.gsa.model.diagrams.architectural;

public class Tag {

    public static final String ELEMENT = "Element";
    public static final String RELATIONSHIP = "Relationship";

    public static final String PERSON = "Person";
    public static final String SOFTWARE_SYSTEM = "Software System";
    public static final String CONTAINER = "Container";
    public static final String COMPONENT = "Component";

    public static final String DEPLOYMENT_NODE = "Deployment Node";
    public static final String CONTAINER_INSTANCE = "Container Instance";


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}