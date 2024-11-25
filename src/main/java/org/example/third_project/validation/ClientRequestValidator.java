package org.example.third_project.validation;

import org.example.third_project.exceptions.check.IncorrectParamsException;

import java.util.function.Predicate;


public final class ClientRequestValidator {


    public static void validateForm(String... params) throws IncorrectParamsException {
        for (String element : params) {
            if (element == null || element.isEmpty()) {
                throw new IncorrectParamsException("Your request contains null or empty params");
            }
        }
    }

    private final Object[] values;
    private final StringBuilder errors;

    private ClientRequestValidator(Object... values) {
        this.values = values;
        this.errors = new StringBuilder();
    }

    public static ClientRequestValidator validate(Object... value) {
        return new ClientRequestValidator(value);
    }

    public ClientRequestValidator notNull() {
        for (Object value : values) {
            if (value == null) {
                errors.append("The required form field is missing. ");
                return this;
            }
        }

        return this;
    }

    public ClientRequestValidator notEmpty() {
        for (Object value : values) {
            if (String.valueOf(value).isEmpty()) {
                errors.append("Form value must not be empty. ");
                return this;
            }
        }

        return this;
    }

    private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";

    public ClientRequestValidator isCurrency() {
        for (Object value : values) {
            if (String.valueOf(value).matches(CURRENCY_CODE_REGEX) != true) {
                errors.append("Currency code must be a valid currency format. ");
            }
        }
//        runThrough(value -> value instanceof String string && string.matches(CURRENCY_CODE_REGEX) != true,
//                "Value must be a valid currency format. ");

        return this;
    }

    public void end() throws IncorrectParamsException {
        if (errors.isEmpty() != true) {
            throw new IncorrectParamsException(errors.toString().trim());
        }
    }

    private ClientRequestValidator runThrough(Predicate<Object> predicate, String messageError) {
        for (Object value : values) {
            if (predicate.test(value)) {
                errors.append(messageError);
                return this;
            }
        }

        return this;
    }

    public ClientRequestValidator isNumber() {
        for (Object value : values) {
            try {
                Double.parseDouble(String.valueOf(value));
            } catch (NumberFormatException e) {
                errors.append("Number from form must be a valid number. ");
                return this;
            }
        }

        return this;
    }

    public ClientRequestValidator isStringEquals(boolean supported) {
        if (supported != true) {
            for (Object valueOut : values) {
                for (Object valueIn : values) {
                    if (valueOut != valueIn) {
                        if (String.valueOf(valueOut).equals(String.valueOf(valueIn))) {
                            errors.append(valueOut).append(" should not equal to ").append(valueIn).append(". ");
                            return this;
                        }
                    }
                }
            }
        }

        return this;
    }

    public ClientRequestValidator limitLength(int max) {
        runThrough(element -> String.valueOf(element).length() > max, "Too long values. Max " + max);
        return this;
    }
}
