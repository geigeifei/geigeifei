package com.gdut.test_1;

import java.math.BigInteger;
import java.util.Stack;

public class ArithmeticCalculator {
    public static String calculateAnswer(String expression) {
        expression = expression.replace("=", "").replace("÷", "/"); // 将“÷”替换回“/”以便计算
        String[] tokens = expression.split("\\s+");

        Stack<Fraction> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (!token.isEmpty()) {
                if (isOperand(token)) {
                    values.push(parseOperand(token));
                } else if (isOperator(token)) {
                    while (!operators.isEmpty() && precedence(token) <= precedence(operators.peek())) {
                        values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(token);
                }
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOp(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop().toProperFractionString();
    }

    private static Fraction parseOperand(String operand) {
        if (operand.contains("/")) {
            String[] fraction = operand.split("/");
            return new Fraction(fraction[0], fraction[1]);
        } else {
            return new Fraction(operand, "1");
        }
    }

    private static Fraction applyOp(String op, Fraction b, Fraction a) {
        switch (op) {
            case "+":
                return a.add(b);
            case "-":
                return a.subtract(b);
            case "*":
                return a.multiply(b);
            case "/":
                return a.divide(b);
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }

    private static int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    private static boolean isOperand(String token) {
        return !isOperator(token);
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}