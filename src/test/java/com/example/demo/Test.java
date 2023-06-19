package com.example.demo;

import java.io.*;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        String jsonDataPath = "D:/WorkSpace/maven_FlakyBuild/data/results.json";
        //clone代码存放的文件位置
        String dataSetPath = "D:/WorkSpace/FlakyDataSet/";
        Map<String,Integer> map= OperationUtils.sortByStar(jsonDataPath,dataSetPath);
        System.out.println(map);
    }
}
