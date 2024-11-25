package org.example.third_project.validation;

import org.example.third_project.exceptions.check.IncorrectParamsException;

import java.util.function.Predicate;


public final class ClientRequestValidator {

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
                break;
            }
        }

        return this;
    }

    public ClientRequestValidator notEmpty() {
        for (Object value : values) {
            if (String.valueOf(value).isEmpty()) {
                errors.append("Form value must not be empty. ");
                break;
            }
        }

        return this;
    }

    private static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";

    public ClientRequestValidator isCurrencyCode() {
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

    public ClientRequestValidator isPositiveNumber() {
        for (Object value : values) {
            try {
                double number = Double.parseDouble(String.valueOf(value));

                if (number <= 0) {
                    errors.append("Negative numbers are not allowed. ");
                    break;
                }
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
       return runThrough(element -> String.valueOf(element).length() > max, "Too long values. Max " + max);
    }

    public ClientRequestValidator hasOnlyEnglishLetters() {
        return runThrough(element -> String.valueOf(element).matches("[a-zA-Z]+") != true, "Must be English letters only.");
    }

    public ClientRequestValidator isCurrencySign() {
        return runThrough(element -> String.valueOf(element).matches("^\\p{Sc}$") != true, "Must be a valid currency sign.");
    }

    private ClientRequestValidator runThrough(Predicate<Object> predicate, String messageError) {
        for (Object value : values) {
            if (predicate.test(value)) {
                errors.append(messageError);
                break;
            }
        }

        return this;
    }
}
