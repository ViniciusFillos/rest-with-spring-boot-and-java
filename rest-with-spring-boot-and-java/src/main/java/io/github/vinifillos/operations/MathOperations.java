package io.github.vinifillos.operations;

import static io.github.vinifillos.operations.Validation.convertToDouble;

public class MathOperations {

    public static Double sum(String num1, String num2) {
        return Validation.convertToDouble(num1) + Validation.convertToDouble(num2);
    }

    public static Double sub(String num1, String num2) {
        return Validation.convertToDouble(num1) - Validation.convertToDouble(num2);
    }

    public static Double mul(String num1, String num2) {
        return Validation.convertToDouble(num1) * Validation.convertToDouble(num2);
    }

    public static Double div(String num1, String num2) {
        return Validation.convertToDouble(num1) / Validation.convertToDouble(num2);
    }

    public static Double exp(String num1, String num2) {
        Double result = convertToDouble(num1);
        for (int i = 1; i < convertToDouble(num2); i++) {
            result = result * convertToDouble(num1);
        }
        return result;
    }

    public static Double sqr(String num1) {
        return Math.sqrt(convertToDouble(num1));
    }

    public static Double mean(String numberOne, String numberTwo) {
        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }
}
