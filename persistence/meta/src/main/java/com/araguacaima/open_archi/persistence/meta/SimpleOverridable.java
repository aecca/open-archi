package com.araguacaima.open_archi.persistence.meta;

public interface SimpleOverridable<T> {

    void override(T source, boolean keepMeta, String suffix);

    void copyNonEmpty(T source, boolean keepMeta);
}
