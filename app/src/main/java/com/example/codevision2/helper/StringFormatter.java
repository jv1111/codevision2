package com.example.codevision2.helper;

public class StringFormatter {
    public static String getTheCodeFromString(String input) {
        // Split the input string at the specific phrase
        String[] parts = input.split("Here's the corrected version");

        // Check if we have the correct parts to extract the code
        if (parts.length > 1) {
            // Return the part after the phrase, trimmed of any leading or trailing whitespace
            return parts[1].trim();
        } else {
            // If the phrase is not found, return an empty string or handle it as needed
            return "";
        }
    }
}
