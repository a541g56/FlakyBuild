package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static com.example.demo.OperationUtils.sortByStar;

public class Test {
    public static void main(String[] args) throws IOException, GitAPIException {
        //数据集json文件存放的位置
        String jsonDataPath = "D:/WorkSpace/maven_FlakyBuild/data/results.json";
        //clone代码存放的文件位置
        String dataSetPath = "D:/WorkSpace/FlakyDataSet/";
        //clone代码存放的文件位置
        String pushPath = "D:/WorkSpace/FlakyDataSet/_blank/FlakyBuild/";
        String temp = "googlecloudplatform/spring-cloud-gcp";
        //将数据集代码克隆到本地
        JSONObject json = FileUtils.readJsonFile(jsonDataPath);
        ArrayList<String> list = new ArrayList<String>();
        int n = json.getJSONArray("items").size();
        System.out.println(n);

    }
}
