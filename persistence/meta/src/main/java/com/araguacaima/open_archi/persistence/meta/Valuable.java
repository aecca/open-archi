package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Valuable {


    @JsonIgnore
    void validateRequest() throws EntityError;

    @JsonIgnore
    void validateCreation() throws EntityError;

    @JsonIgnore
    void validateModification() throws EntityError;
}
