package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
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
