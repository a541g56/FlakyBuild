package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class test {

    private static final Logger logger = Logger.getLogger(test.class);


    public static void main(String[] args) throws GitAPIException, IOException {
        //数据集json文件存放的位置
        String jsonDataPath = "D:/WorkSpace/maven_FlakyBuild/data/results.json";
        //clone代码存放的文件位置
        String dataSetPath = "D:/WorkSpace/FlakyDataSet/";
        //clone代码存放的文件位置
        String pushPath = "D:/WorkSpace/FlakyDataSet/_blank/FlakyBuild/";
        //将查询到的commit信息和包含关键字的代码信息写入的文件的路径
        String writePath = "C:/Users/10247/Desktop/flaky_Message.txt";
        //关键字列表
        List<String> keyWords = new ArrayList<>();
        keyWords.add("flaky");
        keyWords.add("intermittent");
        //根据json文件的地址将代码拉取到本地并筛选使用github action的代码
        //只运行一次
        //OperationUtils.cloneAndScreen(jsonDataPath,dataSetPath);

        //根据关键字筛选commitMessage，并写入指定文件
        OperationUtils.writeCommitMessage(jsonDataPath,dataSetPath,keyWords,writePath);

        //根据对java代码进行筛选，查找包含关键字的信息
        OperationUtils.SearchCodeByWords(dataSetPath,keyWords,writePath);

        //自动push代码
        //OperationUtils.antoPush(jsonDataPath,pushPath,dataSetPath);

    }
}
