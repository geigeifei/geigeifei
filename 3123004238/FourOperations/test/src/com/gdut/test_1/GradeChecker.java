package com.gdut.test_1;

import java.io.*;
import java.util.List;

public class GradeChecker {
    public static void gradeExercises(String exerciseFile, String answerFile) {
        List<String> exercises = FileUtil.readFromFile(exerciseFile);
        List<String> answers = FileUtil.readFromFile(answerFile);

        int correct = 0;
        int wrong = 0;
        StringBuilder correctIds = new StringBuilder();
        StringBuilder wrongIds = new StringBuilder();

        for (int i = 0; i < exercises.size(); i++) {
            String exercise = exercises.get(i);
            String answer = answers.get(i);
            String correctAnswer = ArithmeticCalculator.calculateAnswer(exercise);

            if (correctAnswer.equals(answer)) {
                correct++;
                correctIds.append(i + 1).append(", ");
            } else {
                wrong++;
                wrongIds.append(i + 1).append(", ");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Grade.txt"))) {
            writer.write("Correct: " + correct + " (" + correctIds.toString().trim().replaceAll(", $", "") + ")");
            writer.newLine();
            writer.write("Wrong: " + wrong + " (" + wrongIds.toString().trim().replaceAll(", $", "") + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}