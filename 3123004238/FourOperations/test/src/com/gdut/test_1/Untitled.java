package com.gdut.test_1;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FractionTest {
    @Test
    //测试分数构造
    void testFractionConstructor() {
        Fraction fraction = new Fraction(new BigInteger("1"), new BigInteger("2"));
        assertEquals("1/2", fraction.toProperFractionString());
    }

    @Test
    //测试分母为零时抛出异常
    void testFractionConstructorWithZeroDenominator() {
        assertThrows(ArithmeticException.class, () -> new Fraction(new BigInteger("1"), new BigInteger("0")));
    }

    @Test
    //测试分数加法
    void testAdd() {
        Fraction f1 = new Fraction(new BigInteger("1"), new BigInteger("2"));
        Fraction f2 = new Fraction(new BigInteger("1"), new BigInteger("3"));
        assertEquals("5/6", f1.add(f2).toProperFractionString());
    }

    @Test
    //测试分数减法
    void testSubtract() {
        Fraction f1 = new Fraction(new BigInteger("1"), new BigInteger("2"));
        Fraction f2 = new Fraction(new BigInteger("1"), new BigInteger("3"));
        assertEquals("1/6", f1.subtract(f2).toProperFractionString());
    }

    @Test
    //测试分数乘法
    void testMultiply() {
        Fraction f1 = new Fraction(new BigInteger("1"), new BigInteger("2"));
        Fraction f2 = new Fraction(new BigInteger("1"), new BigInteger("3"));
        assertEquals("1/6", f1.multiply(f2).toProperFractionString());
    }

    @Test
    //测试分数除法
    void testDivide() {
        Fraction f1 = new Fraction(new BigInteger("1"), new BigInteger("2"));
        Fraction f2 = new Fraction(new BigInteger("1"), new BigInteger("3"));
        assertEquals("1’1/2", f1.divide(f2).toProperFractionString());
    }

    @Test
    //测试分数转换为带分数字符串
    void testToProperFractionString() {
        Fraction f1 = new Fraction(new BigInteger("5"), new BigInteger("2"));
        assertEquals("2’1/2", f1.toProperFractionString());
    }
}

//算术计算器测试类
class ArithmeticCalculatorTest {
    @Test
    //测试简单算术表达式计算
    void testCalculateAnswerSimple() {
        assertEquals("3", ArithmeticCalculator.calculateAnswer("1 + 2"));
    }

    @Test
    //测试复杂算术表达式计算
    void testCalculateAnswerComplex() {
        assertEquals("7", ArithmeticCalculator.calculateAnswer("1 + 2 * 3"));
    }

    @Test
    //测试分数算术表达式计算
    void testCalculateAnswerFraction() {
        assertEquals("5/6", ArithmeticCalculator.calculateAnswer("1/2 + 1/3"));
    }
}

//练习生成器测试类
class ExerciseGeneratorTest {
    @Test
    //测试生成练习题
    void testGenerateExercises() {
        ExerciseGenerator.generateExercises(5, 10);
        List<String> exercises = FileUtil.readFromFile("Exercises.txt");
        List<String> answers = FileUtil.readFromFile("Answers.txt");
        assertEquals(5, exercises.size());
        assertEquals(5, answers.size());
    }
}

//答案检查测试类
class GradeCheckerTest {
    @Test
    //测试批改练习题
    void testGradeExercises() {
        List<String> exercises = List.of("1 + 2 = ", "3 * 4 = ");
        List<String> answers = List.of("3", "12");
        FileUtil.saveToFile("Exercises.txt", exercises);
        FileUtil.saveToFile("Answers.txt", answers);
        GradeChecker.gradeExercises("Exercises.txt", "Answers.txt");
        List<String> grade = FileUtil.readFromFile("Grade.txt");
        assertTrue(grade.get(0).contains("Correct: 2"));
    }
}

//文件测试类
class FileUtilTest {
    @Test
    //测试读取文件内容
    void testReadFromFile() {
        List<String> content = FileUtil.readFromFile("testFile.txt");
        assertEquals(List.of("Line 1", "Line 2"), content);
    }

    @Test
    //测试读取不存在的文件
    void testReadFromFileNonExistent() {
        List<String> content = FileUtil.readFromFile("nonExistentFile.txt");
        assertTrue(content.isEmpty());
    }

    @Test
    //测试保存文件内容
    void testSaveToFile() {
        List<String> content = List.of("Line 1", "Line 2");
        FileUtil.saveToFile("testFile.txt", content);
        List<String> readContent = FileUtil.readFromFile("testFile.txt");
        assertEquals(content, readContent);
    }
}

class MyAppTest {
//    @Test
//        // 测试无参数时输出用法信息
//    void testMain_NoArguments() {
//        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outContent));
//        MyApp.main(new String[]{});
//        String expectedOutput = "Usage: java MyApp -n <number> -r <range> "+ System.lineSeparator() +
//                "       java MyApp -e <exercisefile>.txt -a <answerfile>.txt" + System.lineSeparator();
//        assertEquals(expectedOutput, outContent.toString());
//    }

    @Test
        // 测试无效参数时输出错误信息
    void testMain_InvalidArguments() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MyApp.main(new String[]{"-x"});
        assertEquals("Invalid arguments." + System.lineSeparator(), outContent.toString());
    }

    @Test
        // 测试生成练习题时参数不完整时输出错误信息
    void testMain_InvalidArgumentsForGeneratingExercises() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MyApp.main(new String[]{"-n", "10"});
        assertEquals("Invalid arguments for generating exercises." + System.lineSeparator(), outContent.toString());
    }

    @Test
        // 测试批改练习题时参数不完整时输出错误信息
    void testMain_InvalidArgumentsForGrading() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        MyApp.main(new String[]{"-e", "exercises.txt"});
        assertEquals("Invalid arguments for grading." + System.lineSeparator(), outContent.toString());
    }

    @Test
    //测试生成练习题的主方法
    void testMain_GenerateExercises() {
        MyApp.main(new String[]{"-n", "10", "-r", "100"});
    }

    @Test
    //测试批改练习题的主方法
    void testMain_GradeExercises() {
        MyApp.main(new String[]{"-e", "exercises.txt", "-a", "answers.txt"});
    }
}

