package com.araguacaima.gsa.persistence.diagrams.core;


public interface IdGenerator {

    String generateId(Element element);

    String generateId(Relationship relationship);

    void found(String id);

}
