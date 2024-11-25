package org.example.third_project.exceptions.uncheck;

import org.example.third_project.exceptions.check.JsonIgnorePropertiesException;

public class ExchangeRateUnavailableException extends JsonIgnorePropertiesException {
    public ExchangeRateUnavailableException(String message) {
        super(message);
    }

    public ExchangeRateUnavailableException() {
        super("Exchange rate is unavailable by your currencies");
    }
}
