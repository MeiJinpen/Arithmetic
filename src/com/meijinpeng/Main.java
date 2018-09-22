package com.meijinpeng;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
        Map<String, String> params = checkParams(args);
        Arithmetic arithmetic = new Arithmetic(params);

    }

    private static Map<String, String> checkParams(String... args) {
        Map<String, String> params = new HashMap<>();
        if(args.length == 0)  {
            System.out.println("必须输入参数：\n" + "-n: 生成题目的个数\n" +
                    "-r: 生成题目的数值大小\n" + "-e <exercisefile>.txt -a <answerfile>.txt: " +
                    "对给定的题目文件和答案文件，判定答案中的对错并进行数量统计");
            System.exit(-1);
        } else {
            if(args.length / 2 != 0) {
                System.out.println("参数格式输入错误...");
            }
            for (int i = 0; i < args.length; i = i + 2) {
                params.put(args[i], args[i+1]);
            }
        }
        return params;
    }

}
