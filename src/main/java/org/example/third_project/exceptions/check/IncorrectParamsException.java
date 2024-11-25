package org.example.third_project.exceptions.check;

public class IncorrectParamsException extends JsonIgnorePropertiesException {
    public IncorrectParamsException() {
        super("Incorrect parameters");
    }

    public IncorrectParamsException(String message) {
        super(message);
    }
}
