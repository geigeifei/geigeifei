package com.gdut.test_1;

import java.io.*;

public class MyApp {
    public static void main(String[] args) {
        // 检查命令行参数数量是否正确
        if (args.length == 0 || args.length > 4) {
            System.out.print("Usage: java MyApp -n <number> -r <range>" + System.lineSeparator() +
                             "       java MyApp -e <exercisefile>.txt -a <answerfile>.txt" + System.lineSeparator());
            return;
        }

        // 根据参数调用相应的功能模块
        if (args[0].equals("-n")) {
            if (args.length != 4) {
                System.out.println("Invalid arguments for generating exercises.");
                return;
            }
            int num = Integer.parseInt(args[1]); // 题目数量
            int range = Integer.parseInt(args[3]); // 数值范围
            ExerciseGenerator.generateExercises(num, range);
        } else if (args[0].equals("-e")) {
            if (args.length != 4) {
                System.out.println("Invalid arguments for grading.");
                return;
            }
            String exerciseFile = args[1]; // 题目文件
            String answerFile = args[3]; // 答案文件
            GradeChecker.gradeExercises(exerciseFile, answerFile);
        } else {
            System.out.println("Invalid arguments.");
        }
    }
}