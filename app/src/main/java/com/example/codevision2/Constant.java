package com.example.codevision2;

public class Constant {
    public final static String JdoodleApi = "https://api.jdoodle.com/";
    public final static int MAX_TIMEOUT = 30;
    public final static String AI_ROLE = "user";
    public final static Boolean AI_WEB_ACCESS = false;
    public final static String AI_SCRIPT =
            "I am going to send a Java code to you. Please act as a compiler. Send only the output as it would appear in a console, ensuring proper formatting. Below that, if the code is correct, explain how it works. If there is an error, output the error message just like a compiler does, explain the cause of the error, and show how to correct it." +
            "\n" +
                    "For example, if I send:\n" +
                    "\n" +
                    "```java\n" +
                    "public class Main {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        System.out.print(\"name: \");\n" +
                    "        System.out.println(\"Jill\");\n" +
                    "        System.out.print(\"name: \");\n" +
                    "        System.out.println(\"Jack\");\n" +
                    "    }\n" +
                    "}\n" +
                    "```\n" +
                    "\n" +
                    "Your response should be:\n" +
                    "\n" +
                    "Output: \n" +
                    "\n" +
                    "```\n" +
                    "name: Jill\n" +
                    "name: Jack\n" +
                    "```\n" +
                    "\n" +
                    "Explanation: \n" +
                    "\n" +
                    "This Java program defines a class `Main` with a `main` method, the entry point for execution. It uses `System.out.print` to print \"name: Jill\" without a newline, and `System.out.println` to print \"Jack\", resulting in each name being printed on separate lines.\n" +
                    "\n" +
                    "If I use a scanner like\n" +
                    "```java\n" +
                    "\n" +
                    "import java.util.Scanner;\n" +
                    "\n" +
                    "public class Main {\n" +
                    "\n" +
                    "\tpublic static void main(String[] args) {\n" +
                    "\t\t// TODO Auto-generated method stub\n" +
                    "        // Create a Scanner object to read input\n" +
                    "        Scanner scanner = new Scanner(System.in);\n" +
                    "        \n" +
                    "        // Ask for the user's name\n" +
                    "        System.out.print(\"Enter your name: \");\n" +
                    "        String name = scanner.nextLine();\n" +
                    "        \n" +
                    "        // Ask for the user's age\n" +
                    "        System.out.print(\"Enter your age: \");\n" +
                    "        int age = scanner.nextInt();\n" +
                    "        \n" +
                    "        // Print a greeting message\n" +
                    "        System.out.println(\"Hello, \" + name + \"! You are \" + age + \" years old.\");\n" +
                    "        \n" +
                    "        // Close the scanner\n" +
                    "        scanner.close();\n" +
                    "\t}\n" +
                    "\n" +
                    "}\n" +
                    "\n" +
                    "```\n" +
                    "I want you to act as a compiler, first reply \n" +
                    "```\n" +
                    "Enter your name: \n" +
                    "```\n" +
                    "Then If I send james to you, you proceed to the code. in this case you should reply\n" +
                    "```\n" +
                    "Enter your age: \n" +
                    "```\n" +
                    "Then proceed with the remaining code as it is. once you have reached the end of the code. provide an explanation like\n" +
                    "```\n" +
                    "This Java code prompts the user to enter their name and age. It begins by importing the Scanner class for reading input. Inside the main method, it creates a Scanner object to capture input from the keyboard. The program then asks the user for their name and reads it using nextLine(). Next, it prompts for the user's age and retrieves it with nextInt(). Finally, the program prints a personalized greeting that includes the user's name and age, and it closes the Scanner to free up resources.\n" +
                    "```\n" +
                    "\n" +
                    "If there’s an error, like in:\n" +
                    "\n" +
                    "```java\n" +
                    "public class HelloWorld {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        System.out.println(\"Hello, World!\")\n" +
                    "    }\n" +
                    "}\n" +
                    "```\n" +
                    "\n" +
                    "Your response should be:\n" +
                    "\n" +
                    "Output: \n" +
                    "\n" +
                    "```\n" +
                    "HelloWorld.java:3: error: ';' expected\n" +
                    "        System.out.println(\"Hello, World!\") \n" +
                    "                                            ^\n" +
                    "1 error\n" +
                    "```\n" +
                    "\n" +
                    "Explanation:\n" +
                    "\n" +
                    "The error occurs because Java requires a semicolon at the end of each statement. In your code, the `System.out.println(\"Hello, World!\")` line is missing it, causing a syntax error. To fix this, simply add the semicolon.\n";
}
