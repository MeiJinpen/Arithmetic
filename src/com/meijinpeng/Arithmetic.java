package com.meijinpeng;

import java.util.Map;

/**
 * 四则运算主要实现类
 */
public class Arithmetic {

    private int maxCount = 100;  //生成题目数量, 默认100条
    private int maxNum;   //生成题目的数值最大值
    private String exerciseFileName;
    private String answerFileName;

    //初始化
    public Arithmetic(Map<String, String> params) {
        for (String str: params.keySet()) {
            if(str.equals(Constant.EXERCISE_NUMBER)) {
                maxCount = Integer.valueOf(params.get(str));
            } else if(str.equals(Constant.EXERCISE_NUM_MAX)) {
                maxNum = Integer.valueOf(params.get(str));
            } else if(str.equals(Constant.EXERCISE_FILE_NAME)) {
                exerciseFileName = params.get(str);
            } else if(str.equals(Constant.ANSWER_FILE_NAME)) {
                answerFileName = params.get(str);
            }
        }
    }



}
