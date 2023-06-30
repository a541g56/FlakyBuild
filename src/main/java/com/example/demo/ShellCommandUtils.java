package com.example.demo;


import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.*;
import java.util.List;

public class ShellCommandUtils {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(ShellCommandUtils.class);


    public static void clone(String path, String position) {
        try {
            String command = "D:/WorkSoft/Git/bin/sh.exe D:/WorkSpace/FlakyDataSet/git-clone.sh" + " " + path + " " + position;
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
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

    //自动push代码
    public static void GitPusher(String path, String name) throws GitAPIException, IOException {
        //对待提交数据做出更改
        File file = new File(path + "/add.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            BufferedWriter bf = null;
            try {
                bf = new BufferedWriter(new FileWriter(file, true));
                bf.append("1");
                bf.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String command = "D:/WorkSoft/Git/bin/sh.exe D:/WorkSpace/FlakyDataSet/git-push.sh" + " " + name;
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
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


}
