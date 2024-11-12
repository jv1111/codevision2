package com.example.codevision2.helper;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    public static String getTheCodeFromString(String input) {
        // Split the input string at the specific phrase
        String[] parts = input.split("Here's the corrected version:");

        // Check if we have the correct parts to extract the code
        if (parts.length > 1) {
            String code = parts[1].trim();
            return code;
        } else {
            // If the phrase is not found, return an empty string or handle it as needed
            return "";
        }
    }

    public static String formatSampleCode(String input){
        String code = input;
        code = code.replace("```java", "").trim();
        code = code.replace("```", "");
        Log.i("myTag new code: ", code);
        return code;
    }

    public static String extractCode(String input) {
        // Check if the string starts with "```java" and ends with "```"

        if (input.startsWith("```java") && input.endsWith("```")) {
            // Remove the "```java" at the beginning and "```" at the end
            Log.i("myTag", "extracted");
            return input.substring(7, input.length() - 3).trim();
        }
        Log.i("myTag", "returning the original input.");
        return input;
    }

    public static String extractCodeExtended(String input) {
        // Define the regular expression to match content between ```java and ```
        String regex = "```java(.*?)```";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        // If a match is found, return the captured content
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public static boolean hasValue(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
