package com.araguacaima.gsa.persistence.meta;

import com.araguacaima.gsa.persistence.commons.exceptions.EntityError;

/**
 * Defines common properties implemented by Entities in the model
 */
public interface BasicEntity {
    String getId();
    void validateRequest() throws EntityError;
    void validateCreation() throws EntityError;
    void validateModification() throws EntityError;
}
