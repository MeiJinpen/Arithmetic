package com.meijinpeng;

public class Constant {

    //参数
    public static final String EXERCISE_NUMBER = "-n";    //生成的题目数量
    public static final String EXERCISE_NUM_MAX = "-r";   //运算的数值大小, 2以上
    public static final String EXERCISE_FILE_NAME = "-e"; //指定的题目文件名
    public static final String ANSWER_FILE_NAME = "-a";   //指定的答案文件名

    //默认生成的文件名
    public static final String ANSWER_FILE_DEFAULT = "Answer.txt";
    public static final String EXERCISE_FILE_DEFAULT = "Exercise.txt";
    public static final String GRADE_FILE_DEFAULT = "Grade.txt";

    public static final String ADDITION = "+";
    public static final String SUBTRACTION = "-";
    public static final String MULTIPLICATION= "x";
    public static final String DIVISION = "÷";

    public static final String[] SYMBOLS = {
            ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION
    };

}
