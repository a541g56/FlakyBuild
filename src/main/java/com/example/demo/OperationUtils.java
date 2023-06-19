package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class OperationUtils {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(OperationUtils.class);

    //克隆并且筛选github action
    public static void cloneAndScreen(String jsonDataPath,String dataSetPath) throws IOException, GitAPIException {
        //将数据集代码克隆到本地
        JSONObject json = FileUtils.readJsonFile(jsonDataPath);
        ArrayList<String> list = new ArrayList<String>();
        int n = json.getJSONArray("items").size();
        System.out.println(n);
        //200个
        for (int i = 0; i < 200; i++) {
            list.add(((JSONObject) json.getJSONArray("items").get(i)).getString("name"));//将name值提取出来放入数组
        }
        logger.info(list);

        //获取文件中已克隆的仓库列表
        File f = new File(dataSetPath);
        List<String> stringList = new ArrayList<String>();
        File[] files = f.listFiles();
        for(File temp : files){
            stringList.add(temp.getName());
        }
        logger.info(stringList);



        for (int i = 0; i < 200; i++) {
            String name = list.get(i);
            logger.info(name);
            if(!stringList.contains(name.split("/")[0])){
                logger.info(name);
                JGitUtils.clone("git@github.com:"+name,name);
            }
            System.out.println(name + "完成");
            //筛选使用github action的代码仓库
            File file = new File(dataSetPath + name + "/.github/workflows");
            if (!file.exists()) {
                System.out.println(name + " False");
                System.out.println(name.split("/")[0]);
                FileUtils.delete(dataSetPath + name.split("/")[0]);
            }
        }
    }


    //按星数对筛选后数据进行排序
    public static Map<String,Integer> sortByStar(String jsonDataPath,String dataSetPath) throws IOException {
        JSONObject json = FileUtils.readJsonFile(jsonDataPath);
        Map<String,Integer> maps = new HashMap<String,Integer>();
        String[] list = FileUtils.getNames(dataSetPath);

        int n = json.getJSONArray("items").size();
        for (int i = 0; i < n; i++) {
            JSONObject object= ((JSONObject) json.getJSONArray("items").get(i));
            //根据筛选后的文件名获取星星值，以键值对存入Map
            if(Arrays.asList(list).contains(object.getString("name").split("/")[0])) {
                maps.put(object.getString("name"), Integer.valueOf(object.getString("stargazers")));
            }
        }

        //按值排序
        List<Map.Entry<String,Integer>> list2 = new LinkedList<>(maps.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }
        });

        Map<String,Integer> finalMap = new LinkedHashMap<>();
        for(Map.Entry<String ,Integer> entry : list2){
            finalMap.put(entry.getKey(),entry.getValue());
        }
        return finalMap;
    }

    //对代码内容按关键词进行搜索
    public static void SearchCodeByWords (String path,List<String> keyWords,String writePath) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();
        String fileName = "";
        String filePath = "";
        ArrayList<String> content  = new ArrayList<>();
        for (File fileTemp : files){
            filePath = fileTemp.getAbsolutePath();
            if (fileTemp.isDirectory()){
                SearchCodeByWords(filePath,keyWords,writePath);
            }else{
                fileName =  fileTemp.getName();
                if (fileName.contains(".")) {
                    String fileType = fileName.substring(fileName.lastIndexOf("."));
                    if (fileType.equals(".java")) {
                        content = FileUtils.getContent(filePath);
                    }
                    //获得java文件内容后，对内容进行关键词检索
                    int i = 0;
                    BufferedWriter out = new BufferedWriter(new FileWriter(writePath,true));
                    for (String temp : content) {
                        i++;
                        for (String word : keyWords) {
                            if (temp.contains(word)) {
                                    out.append(filePath + "\t" + i+ "\t" + word+"\n");
                            }
                        }
                    }
                    out.close();
                }
            }
        }
    }
    //根据关键字筛选commitMessage，并写入指定文件
    public static void writeCommitMessage (String jsonDataPath,String dataSetPath,List<String> keyWords,String writePath) throws IOException {
        Map<String,Integer> map= sortByStar(jsonDataPath,dataSetPath);
        List<String> commitList = new ArrayList<>();
        for (String temp : map.keySet()){
            commitList.addAll(JGitUtils.screenCommitMessage(dataSetPath+temp, keyWords));
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(writePath,true));
        for(String s : commitList){
            out.append(s+"\n");
        }
        out.close();
    }

}

    //将代码复制到待提交仓库，将workflow内的提交分支更改为项目名，之后循环提交项目出发



