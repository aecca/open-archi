package com.araguacaima.open_archi.persistence.meta;

public interface Overridable<T extends BaseEntity> {

    void override(T source, boolean keepMeta, String suffix);

    void copyNonEmpty(T source, boolean keepMeta);
}
