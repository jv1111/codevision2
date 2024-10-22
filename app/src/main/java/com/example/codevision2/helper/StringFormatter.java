package com.example.codevision2.helper;

public class StringFormatter {

    public static boolean containsScanner(String input) {
        if (input == null) {
            return false; // Handle null input
        }
        return input.contains("##SCANNER##"); // Check for the presence of "##SCANNER##"
    }

    public static String removeSCANNERTag(String input){
        if (input == null) {
            return null; // Handle null input
        }
        return input.replace("##SCANNER##", "");
    }

    public static String removeBackticks(String input) {
        if (input == null) {
            return null; // Handle null input
        }
        return input.replace("```", ""); // Remove all occurrences of "```"
    }

    public void formatCodeFromOCR(String code){

    }
}
