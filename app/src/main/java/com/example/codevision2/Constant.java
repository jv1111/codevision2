package com.example.codevision2;

public class Constant {
    public final static String JdoodleApi = "https://api.jdoodle.com/";
    public final static int MAX_TIMEOUT = 30;
    public final static String AI_ROLE = "user";
    public final static Boolean AI_WEB_ACCESS = false;
    public final static String AI_SCRIPT =
            "I am going to send a Java code to you. Please act as a compiler. Send only the output as it would appear in a console, ensuring proper formatting. Below that, if the code is correct, explain how it works. If there is an error, output the error message just like a compiler does, explain the cause of the error, and show how to correct it.\n" +
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
                    "I want you to act as a compiler but I want a TAG ##SCANNER## at the top of the response if it requires an input, first reply \n" +
                    "```\n" +
                    "##SCANNER##\n" +
                    "Enter your name: \n" +
                    "```\n" +
                    "Then If I send \"(My input = \"james\")\" to you, you proceed to the code. in this case you should reply\n" +
                    "```\n" +
                    "##SCANNER##\n" +
                    "Enter your age: \n" +
                    "```\n" +
                    "Then proceed with the remaining code as it is. once you have reached the end of the code. provide an explanation like\n" +
                    "```\n" +
                    "This Java code prompts the user to enter their name and age. It begins by importing the Scanner class for reading input. Inside the main method, it creates a Scanner object to capture input from the keyboard. The program then asks the user for their name and reads it using nextLine(). Next, it prompts for the user's age and retrieves it with nextInt(). Finally, the program prints a personalized greeting that includes the user's name and age, and it closes the Scanner to free up resources.\n" +
                    "```\n" +
                    "\n" +
                    "If the print statement is not inside a loop or if there is no print statement to print just response a TAG ##SCANNER## to indicate that I have to send an input\n" +
                    "```\n" +
                    "import java.util.Scanner;\n" +
                    "\n" +
                    "public class NumberCollector {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        Scanner scanner = new Scanner(System.in);\n" +
                    "        int[] numbers = new int[5];\n" +
                    "\n" +
                    "        System.out.println(\"Enter 5 numbers:\");//This is not inside a loop;\n" +
                    "\n" +
                    "        for (int i = 0; i < numbers.length; i++) {\n" +
                    "\t//This does not have a print statement\n" +
                    "            numbers[i] = scanner.nextInt();\n" +
                    "        }\n" +
                    "\n" +
                    "        System.out.println(\"You entered:\");\n" +
                    "        for (int number : numbers) {\n" +
                    "            System.out.println(number);\n" +
                    "        }\n" +
                    "\n" +
                    "        scanner.close();\n" +
                    "    }\n" +
                    "}\n" +
                    "```\n" +
                    "\n" +
                    "Our conversation would be like\n" +
                    "```\n" +
                    "You:\n" +
                    "\t##SCANNER##\n" +
                    "\tEnter 5 numbers: \n" +
                    "\n" +
                    "Me: My input = 1\n" +
                    "\n" +
                    "You:\n" +
                    "\t##SCANNER##\n" +
                    "\n" +
                    "Me: My input = 2\n" +
                    "\n" +
                    "You: \n" +
                    "\t##SCANNER##\n" +
                    "\n" +
                    "Me: My input = 3\n" +
                    "\n" +
                    "You: \n" +
                    "\t##SCANNER##\n" +
                    "\n" +
                    "Me: My input = 4\n" +
                    "\n" +
                    "You: \n" +
                    "\t##SCANNER##\n" +
                    "\n" +
                    "Me: My input = 5\n" +
                    "\n" +
                    "You: \n" +
                    "\tYou entered:\n" +
                    "\t5\n" +
                    "\t4\n" +
                    "\t3\n" +
                    "\t2\n" +
                    "\t1\n" +
                    "\n" +
                    "\n" +
                    "```\n" +
                    "\n" +
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
                    "The error occurs because Java requires a semicolon at the end of each statement. In your code, the `System.out.println(\"Hello, World!\")` line is missing it, causing a syntax error. To fix this, simply add the semicolon.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "========== INPUT TYPE FOR THE SCANNER ========\n" +
                    "(My input = \"jowiiojpqoe\") Base on the code if the input should be a number but I did (My input = \"jowiiojpqoe\") output the error and tell my why don't add any more prompt like \n" +
                    "\n" +
                    "Output\n" +
                    "\n" +
                    "Exception in thread \"main\" java.util.InputMismatchException\n" +
                    "        at java.base/java.util.Scanner.throwFor(Scanner.java:937)\n" +
                    "        at java.base/java.util.Scanner.next(Scanner.java:1594)\n" +
                    "        at java.base/java.util.Scanner.nextInt(Scanner.java:2240)\n" +
                    "        at java.base/java.util.Scanner.nextInt(Scanner.java:2130)\n" +
                    "        at ScannerExample.main(ScannerExample.java:9)\n" +
                    "\n" +
                    "Explanation:\n" +
                    "\n" +
                    "The error occurs because the program expects an integer input for age, but a non-numeric string (\"twenty three\") was provided. The Scanner.nextInt() method throws an InputMismatchException when the input does not match the expected type. To fix this, ensure that the input for age is a valid integer (e.g., \"23\").\n" +
                    "\n" +
                    ", if my input matches proceed with the code.\n" +
                    "\n" +
                    "This is my code for you to compile \n" +
                    "\"\n";
    public static String AI_CODE_TAIL =
            "\n\"\n";
    public static String codeSample = "import java.util.Scanner;  // Import the Scanner class\n" +
            "\n" +
            "public class Main {\n" +
            "  public static void main(String[] args) {\n" +
            "    Scanner myObj = new Scanner(System.in);  // Create a Scanner object\n" +
            "    System.out.println(\"Enter username\");\n" +
            "\n" +
            "    String userName = myObj.nextLine();  // Read user input\n" +
            "    System.out.println(\"Username is: \" + userName);  // Output user input\n" +
            "        \n" +
            "    String age = myObj.nextLine();  // Read user input\n" +
            "    System.out.println(\"age is: \" + age);  // Output user input\n" +
            "  }\n" +
            "}";
    public static String resultSample = "```\n##SCANNER##\nHi:\n```";
}
