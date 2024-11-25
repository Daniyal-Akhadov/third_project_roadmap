package org.example.third_project.exceptions.check;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class JsonIgnorePropertiesException extends Exception {

    public JsonIgnorePropertiesException(String message) {
        super(message);
    }
}
