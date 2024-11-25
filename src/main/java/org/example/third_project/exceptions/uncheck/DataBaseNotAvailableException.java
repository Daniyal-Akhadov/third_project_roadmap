package org.example.third_project.exceptions.uncheck;

public class DataBaseNotAvailableException extends JsonIgnorePropertiesRuntimeException {
    public DataBaseNotAvailableException() {
        super("Database is not available");
    }

    public DataBaseNotAvailableException(String message) {
        super(message);
    }
}
