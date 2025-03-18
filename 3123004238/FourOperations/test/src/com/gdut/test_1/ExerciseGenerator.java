package com.gdut.test_1;

import java.util.*;

public class ExerciseGenerator {
    private static final Random random = new Random();

    public static void generateExercises(int num, int range) {
        List<String> exercises = new ArrayList<>(num);
        List<String> answers = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            String exercise = generateExercise(range);
            exercises.add(exercise);
            answers.add(ArithmeticCalculator.calculateAnswer(exercise.substring(0, exercise.length() - 2))); // Remove " ="
        }

        FileUtil.saveToFile("Exercises.txt", exercises);
        FileUtil.saveToFile("Answers.txt", answers);
    }

    private static String generateExercise(int range) {
        int numOperands = random.nextInt(3) + 1; // 随机选择1, 2或3个操作数
        List<String> operands = new ArrayList<>(numOperands);
        List<String> operators = new ArrayList<>(numOperands - 1);

        for (int i = 0; i < numOperands; i++) {
            operands.add(generateOperand(range));
        }

        for (int i = 0; i < numOperands - 1; i++) {
            operators.add(generateOperator());
        }

        return buildExpression(operands, operators);
    }

    private static String generateOperand(int range) {
        int type = random.nextInt(2); // 0表示自然数，1表示分数
        if (type == 0) {
            return String.valueOf(random.nextInt(range) + 1); // 生成自然数（1到range）
        } else {
            int denominator = random.nextInt(range - 1) + 2; // 分母范围：2到range
            int numerator = random.nextInt(denominator - 1) + 1; // 分子范围：1到分母-1
            return numerator + "/" + denominator; // 生成真分数
        }
    }

    private static String generateOperator() {
        String[] ops = {"+", "-", "*", "÷"}; // 将除号替换为“÷”
        return ops[random.nextInt(ops.length)];
    }

    private static String buildExpression(List<String> operands, List<String> operators) {
        StringBuilder sb = new StringBuilder();
        sb.append(operands.get(0));
        for (int i = 1; i < operands.size(); i++) {
            sb.append(" ").append(operators.get(i - 1)).append(" ");
            sb.append(operands.get(i));
        }
        sb.append(" = ");
        return sb.toString();
    }
}