package org.example.third_project.exceptions.uncheck;

public class CurrencyNotFoundException extends JsonIgnorePropertiesRuntimeException {
    public CurrencyNotFoundException() {
        super("Currency not found");
    }

    public CurrencyNotFoundException(String message) {
        super(message);
    }
}

