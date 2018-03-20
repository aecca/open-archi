package com.araguacaima.open_archi.persistence.diagrams.core;

public interface ItemCategory<T extends Enum> {

    T getType();

    void setType(T type);
}
