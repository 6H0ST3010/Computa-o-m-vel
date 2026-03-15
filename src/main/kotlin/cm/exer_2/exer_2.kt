package org.example.cm.exer_2

fun main() {
    println("Choose an option:" +
            "\nBasic arithmetic operations - Addiction(+) | Subtraction(-) | Multiplication(*) | Division(/)" +
            "\nBoolean operators - AND(&&) | OR(||) | NOT(!)" +
            "\nBitwise shift operators - Left shift(shl) | Right shift(shr)");
    val option = readln();
    val possible_options = arrayOf("+", "-", "*", "/", "and", "or", "not", "shl", "shr");
    if(option.lowercase() !in possible_options) {
        throw IllegalArgumentException("Invalid option");
    }

    print("Value for n1: ");
    var n1: Int
        try {
            n1 = readln().toInt()
            if (n1 < 0) {
                throw IllegalArgumentException("The number has to be positive");
            }
        }
        catch (e: NumberFormatException) {
            println("Invalid number");
            throw e;
        }

    print("Value for n2: ");
    var n2: Int
        try {
            n2 = readln().toInt()
            if (n2 < 0) {
                throw IllegalArgumentException("The number has to be positive");
            }
        }
        catch (e: NumberFormatException) {
            println("Invalid number");
            throw e;
        }
    when(option){
        "+" -> println("Result: ${n1 + n2} | In Hexadecimal: ${(n1 + n2).toHexString()}");
        "-" -> println("Result: ${n1 - n2} | In Hexadecimal: ${(n1 - n2).toHexString()}");
        "*" -> println("Result: ${n1 * n2} | In Hexadecimal: ${(n1 * n2).toHexString()}");
        "/" -> {
            if (n2 == 0){
                throw ArithmeticException("Division by zero");
            }
            println("Result: ${n1 / n2} | In Hexadecimal: ${(n1 / n2).toHexString()}");
        };
        "and" -> {
            if (n1 >= 1){
                n1 = 1;
            }

            if (n2 >= 1) {
                n2 = 1;
            }

            val bool1 = n1 == 1;
            val bool2 = n2 == 1;
            println("Result: ${bool1 && bool2}");
        }
        "or" -> {
            if (n1 >= 1){
                n1 = 1;
            }

            if (n2 >= 1) {
                n2 = 1;
            }

            val bool1 = n1 == 1;
            val bool2 = n2 == 1;
            println("Result: ${bool1 || bool2}");
        }
        "not" -> {
            if (n1 >= 1){
                n1 = 1;
            }

            if (n2 >= 1) {
                n2 = 1;
            }

            val bool1 = n1 == 1;
            val bool2 = n2 == 1;
            println("Results- n1: ${!bool1} | n2: ${!bool2}");
        }
        "shl" -> {
            println("Results- ${n1 shl n2}")
        }
        "shr" -> {
            println("Results- ${n1 shr n2}")
        }
    }
}