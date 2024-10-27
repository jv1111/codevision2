package com.example.codevision2.helper;

import android.util.Log;

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

    public static boolean hasValue(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
