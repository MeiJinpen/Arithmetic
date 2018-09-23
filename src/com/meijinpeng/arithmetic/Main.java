package com.meijinpeng.arithmetic;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
        args = new String[]{"-n", "10000", "-r", "100", "-d", "10"};
        Map<String, String> params = checkParams(args);
        Operation operation = new Operation(params);
        operation.generateExercisesAnswers();
    }

    private static Map<String, String> checkParams(String... args) {
        Map<String, String> params = new HashMap<>();
        if(args.length == 0)  {
            throw new RuntimeException("必须输入参数：\n" + "-n: 生成题目的个数\n" +
                    "-r: 生成题目的数值大小\n" + "-e <exercisefile>.txt -a <answerfile>.txt: " +
                    "对给定的题目文件和答案文件，判定答案中的对错并进行数量统计");
        } else {
            if(args.length % 2 != 0) {
                throw new RuntimeException("参数格式输入错误...");
            }
            for (int i = 0; i < args.length; i = i + 2) {
                params.put(args[i], args[i+1]);
            }
        }
        return params;
    }

}
