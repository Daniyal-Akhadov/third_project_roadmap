package org.example.third_project.utils;

import lombok.experimental.UtilityClass;
import org.example.third_project.exceptions.check.IncorrectParamsException;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class CurrencyExtractor {
    public static final String CODE_REGEX = "(?:/)?([A-Z]{3})([A-Z]{3})*";

    public static String[] extract(String input) throws IncorrectParamsException {
        Pattern pattern = Pattern.compile(CODE_REGEX);
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches() != true){
            throw new IncorrectParamsException("Currency codes of the pair are missing in the address");
        }

        return getCodes(matcher);
    }

    private static String[] getCodes(Matcher matcher) {
        String[] codes = new String[matcher.groupCount()];

        for (int i = 0; i < codes.length; i++){
            codes[i] = matcher.group(i + 1);
            System.out.println(matcher.group(i + 1));
        }

        System.out.println(Arrays.toString(codes));

        return codes;
    }
}
