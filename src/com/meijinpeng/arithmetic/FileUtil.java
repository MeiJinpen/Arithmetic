package com.meijinpeng.arithmetic;

import java.io.*;

public class FileUtil {

    /**
     * 写入文件中
     * @param content 写入内容
     * @param fileName 写入文件名
     */
    public static void writeFile(String content, String fileName) {
        File file = new File(fileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            if(!file.exists()){
                file.createNewFile();
            }
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            System.out.println("文件操作失败...");
        }
    }

    /**
     * 读文件内容
     * @param fileName 文件名
     * @param callBack 回调接口，分别处理每一行
     */
    public static void readFile(String fileName, ReaderCallBack callBack) {
        File file = new File(fileName);
        if(!file.exists()) {
            System.out.println("文件不存在，请重试");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                callBack.deal(line);
            }
        } catch (IOException e) {
            System.out.println("读取文件失败...");
        }
    }

    public interface ReaderCallBack {
        void deal(String line) throws IOException;
    }

}
