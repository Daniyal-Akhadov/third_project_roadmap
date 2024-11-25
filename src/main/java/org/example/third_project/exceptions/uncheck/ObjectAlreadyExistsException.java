package org.example.third_project.exceptions.uncheck;

public class ObjectAlreadyExistsException extends JsonIgnorePropertiesRuntimeException {
    public ObjectAlreadyExistsException() {
        super("Object already exists");
    }

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
