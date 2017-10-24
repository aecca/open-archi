package com.araguacaima.gsa.model.diagrams.common;

import com.araguacaima.gsa.model.diagrams.architectural.Relationship;

public interface IdGenerator {

    String generateId(Element element);

    String generateId(Relationship relationship);

    void found(String id);

}
