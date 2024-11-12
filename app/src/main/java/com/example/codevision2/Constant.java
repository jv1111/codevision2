package com.example.codevision2;

public class Constant {
    public final static int MAX_TIMEOUT = 30;
    public final static int AI_ANALYZE = 1;
    public final static int AI_EXPLAIN_CODE = 2;

    public static final int UCROP_REQUEST_CODE = 104;

    public static String SAMPLE_ANALYZE_RESPONSE = "```java\nimport java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner scanner = new Scanner(System.in);\n        System.out.print(\"Enter your name: \");\n        String name = scanner.nextLine();\n        System.out.print(\"Enter your age: \");\n        \n        // Check to ensure that input is an integer\n        while (!scanner.hasNextInt()) {\n            System.out.println(\"Please enter a valid age.\");\n            scanner.next(); // Clear the invalid input\n        }\n        \n        int age = scanner.nextInt();\n        System.out.println(\"Hello, \" + name + \"! You are \" + age + \" years old.\");\n        \n        scanner.close(); // Close the scanner to prevent resource leak\n    }\n}\n```\n\n1. Added a loop to check if the input for age is an integer (\"while (!scanner.hasNextInt())\"). This ensures that the user enters a valid integer for age.\n2. Added \"scanner.next();\" inside the loop to consume the invalid input so that the user can try again.\n3. Added \"scanner.close();\" at the end of the code to properly close the scanner and prevent resource leaks.";
}
