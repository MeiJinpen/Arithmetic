package com.meijinpeng.arithmetic;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * 四则运算题目操作类
 */
public class Operation {

    int maxCount = 100;  //生成题目数量，默认100条
    int maxNum = 100;   //生成题目的数值最大值，默认为100
    int derBound = 20; //分母的范围，默认为20
    String exerciseFileName;  // 题目文件名
    String answerFileName;  // 答案文件名
    int correctCount;  //正确数目
    int wrongCount;    //错误数目

    //初始化
    public Operation(Map<String, String> params) {
        for (String str : params.keySet()) {
            if (str.equals(Constant.EXERCISE_NUMBER)) {
                maxCount = Integer.valueOf(params.get(str));
            } else if (str.equals(Constant.EXERCISE_NUM_MAX)) {
                maxNum = Integer.valueOf(params.get(str));
            } else if (str.equals(Constant.EXERCISE_FILE_NAME)) {
                exerciseFileName = params.get(str);
            } else if (str.equals(Constant.ANSWER_FILE_NAME)) {
                answerFileName = params.get(str);
            } else if (str.equals(Constant.EXERCISE_DER_MAX)) {
                derBound = Integer.valueOf(params.get(str));
            }
        }
    }

    private ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 生成随机题目
     */
    public void generateExercisesAnswers() {
        StringBuilder exercises = new StringBuilder();
        StringBuilder answers = new StringBuilder();
        List<Exercise> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 1; i <= maxCount;) {
            Exercise exercise = new Exercise(this, true);
            if (!list.contains(exercise)) {
                String[] strs = exercise.print().split("=");
                exercises.append(i).append(". ").append(strs[0]).append("\n");
                answers.append(i).append(".").append(strs[1]).append("\n");
                list.add(exercise);
                i++;
            }
            long end = System.currentTimeMillis();
            if(end - start > 10000) {
                throw new RuntimeException("生成题目时间过长，可能传入参数范围过大或过小，请重试");
            }
        }
        executor.execute(() -> FileUtil.writeFile(exercises.toString(), Constant.EXERCISE_FILE_DEFAULT));
        executor.execute(() -> FileUtil.writeFile(answers.toString(), Constant.ANSWER_FILE_DEFAULT));
        //开启线程池执行任务后，关闭线程池释放资源
        executor.shutdown();
        try {
            boolean loop = true;
            while (loop) {
                loop = !executor.awaitTermination(30, TimeUnit.SECONDS);  //超时等待阻塞，直到线程池里所有任务结束
            } //等待所有任务完成
            long end = System.currentTimeMillis();
            System.out.println("生成的" + maxCount + "道题和答案存放在当前目录下的Exercises.txt和Answers.txt，耗时为" + (end - start) + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void checkExercises() {
        long start = System.currentTimeMillis();
        List<String> correctNums = new ArrayList<>();
        List<String> wrongNums = new ArrayList<>();
        FileUtil.readFile((exercise, answer) -> {
            String[] strs1 = exercise.split("\\.");
            String[] strs2 = answer.split("\\.");
            if(strs1[0].equals(strs2[0])) {
                Exercise exes = new Exercise(this, false);
                exes.build(strs1[1].trim());
                if(exes.getResult().equals(new Fraction(strs2[1].trim()))) {
                    correctNums.add(strs1[0]);
                    correctCount++;
                } else {
                    wrongNums.add(strs1[0]);
                    wrongCount++;
                }
            }
        }, exerciseFileName, answerFileName);
        FileUtil.writeFile(printResult(correctNums, wrongNums), Constant.GRADE_FILE_DEFAULT);
        long end = System.currentTimeMillis();
        System.out.println("题目答案对错统计存在当前目录下的Grade.txt文件下，耗时为：" + (end - start) + "ms");
    }

    private String printResult(List<String> correctNums, List<String> wrongNums) {
        StringBuilder builder = new StringBuilder();
        builder.append("Correct: ").append(correctCount).append(" (");
        for (int i = 0 ;i < correctNums.size(); i++) {
            if (i == correctNums.size() - 1) {
                builder.append(correctNums.get(i));
                break;
            }
            builder.append(correctNums.get(i)).append(", ");
        }
        builder.append(")").append("\n");
        builder.append("Wrong: ").append(wrongCount).append(" (");
        for (int i = 0; i < wrongNums.size(); i++) {
            if(i == wrongNums.size() - 1) {
                builder.append(wrongNums.get(i));
                break;
            }
            builder.append(wrongNums.get(i)).append(", ");
        }
        builder.append(")").append("\n");
        return builder.toString();
    }

}
