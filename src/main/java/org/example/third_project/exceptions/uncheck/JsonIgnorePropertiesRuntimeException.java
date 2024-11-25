package org.example.third_project.exceptions.uncheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class JsonIgnorePropertiesRuntimeException extends RuntimeException {

    public JsonIgnorePropertiesRuntimeException(String message) {
        super(message);
    }
}
