package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class);
    //删除文件夹全部文件
    public static void delete(String path){
        File file = new File(path);
        File[] list = file.listFiles();
        if(list != null){
            for (File f : list){
                delete(f.getAbsolutePath());
            }
        }
        file.delete();
    }
    //读取JSON文件，返回JSON对象
    public static JSONObject readJsonFile(String path) throws IOException {
        File jsonFile = new File(path);
        FileReader fileReader = new FileReader(jsonFile);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
        int ch = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while ((ch = reader.read())!=-1){
            stringBuffer.append((char)ch);
        }
        fileReader.close();
        reader.close();
        String jsonString = stringBuffer.toString();
        JSONObject object = JSONObject.parseObject((jsonString));
        return object;
    }
    //返回文件夹下的所有文件名
    public static String[] getNames(String path){
        File file = new File(path);
        return file.list();
    }


    //读取文件内容
    public static ArrayList<String> getContent(String path) {
        ArrayList<String> content  = new ArrayList<>();
        String thisLine = "";
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            BufferedReader bfr = new BufferedReader(fr);
            while ((thisLine = bfr.readLine()) != null) {
                content.add(thisLine);
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    //在待提交文件夹删除上一个项目，导入下一个项目内容
    public static void deleteAndCopy(String deletePath, String inPath){
        try {
            File file = new File(deletePath);
            File[] files = file.listFiles();
            for (File f :files){
                if (!f.getName().equals(".git")){
                    delete(f.getAbsolutePath());
                }
            }
            File inFile = new File(inPath);
            logger.info(inPath);
            File[] inFiles = inFile.listFiles();
            for (File f :inFiles){
                if (!f.getName().equals(".git")){
                   if(!f.isFile()){
                       org.apache.commons.io.FileUtils.copyDirectoryToDirectory(new File(f.getAbsolutePath()),new File(deletePath));
                   }else{
                       org.apache.commons.io.FileUtils.copyFileToDirectory(new File(f.getAbsolutePath()),new File(deletePath));
                   }

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //将workflow的提交触发分支更改为项目名。
    public static void changePushBranch(String path,String branchName){
        File file = new File(path);
        logger.info(path);
        File[] listFile = file.listFiles();

        for(File temp :listFile){
            logger.info(temp.getAbsolutePath());
            //在workflow下是文件夹
            if(!temp.isFile()){
                continue;
            }

            ArrayList<String> list = getContent(temp.getAbsolutePath());
            int begin = 0,flag = 0;
            for(int i = 0;i<list.size();i++){
                //找到on：的行
                if(list.get(i).equals("on:")){
                    begin = i;
                    flag = 1;
                    continue;
                }
                if(list.get(i).equals("jobs:")){
                    break;
                }
                //删除on：到jobs：的内容
                if (flag == 1){
                    list.set(i,"");
                }

            }
            list.add(begin+1,"  push:");
            list.add(begin+2,"    branches: [ "+branchName+" ]");
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(temp.getAbsolutePath()));
                for(String s : list){
                    out.write(s+"\n");
                }
                out.close();
            } catch (IOException e) {
            }
        }
    }
}
