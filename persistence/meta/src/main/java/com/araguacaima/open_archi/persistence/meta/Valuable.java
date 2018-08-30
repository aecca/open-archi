package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public interface Valuable extends Overridable {

    @JsonIgnore
    void validateRequest(Map<String, Object> map) throws EntityError;

    @JsonIgnore
    void validateCreation(Map<String, Object> map) throws EntityError;

    @JsonIgnore
    void validateModification(Map<String, Object> map) throws EntityError;

    @JsonIgnore
    void validateReplacement(Map<String, Object> map) throws EntityError;

    @JsonIgnore
    void validateRequest() throws EntityError;

    @JsonIgnore
    void validateCreation() throws EntityError;

    @JsonIgnore
    void validateModification() throws EntityError;

    @JsonIgnore
    void validateReplacement() throws EntityError;
}
