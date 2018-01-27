package com.araguacaima.gsa.persistence.meta;

import com.araguacaima.gsa.persistence.commons.exceptions.EntityError;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Defines common properties implemented by Entities in the model
 */
public interface BasicEntity {
    String getId();

    @JsonIgnore
    void validateRequest() throws EntityError;

    @JsonIgnore
    void validateCreation() throws EntityError;

    @JsonIgnore
    void validateModification() throws EntityError;
}
