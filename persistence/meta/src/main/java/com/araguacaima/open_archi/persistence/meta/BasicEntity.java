package com.araguacaima.open_archi.persistence.meta;

/**
 * Defines common properties implemented by Entities in the model
 */
public interface BasicEntity <T extends BaseEntity> extends Valuable {
    String getId();

    void override(T source, boolean keepMeta);

    void copyNonEmpty(T source, boolean keepMeta);
}
