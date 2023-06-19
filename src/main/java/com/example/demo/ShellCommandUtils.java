package com.example.demo;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ShellCommandUtils {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(ShellCommandUtils.class);


    public static void clone(String path,String position){
        try {
            String command = "D:/WorkSoft/Git/bin/sh.exe D:/WorkSpace/FlakyDataSet/git-clone.sh"+" "+path+" "+position;
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine())!=null){
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println(exitCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void GitPusher(String branchName){
        String[] cmd = {"D:/WorkSoft/Git/bin/sh.exe","-c","sh D:/WorkSpace/FlakyDataSet/git-push.sh "+branchName};
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = reader.readLine())!=null){
                System.out.println(line);
            }
            process.waitFor();
            System.out.println("Git push completed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
