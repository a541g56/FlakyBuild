package com.example.demo;








import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileBasedConfig;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.SystemReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JGitUtils {
    private static final org.apache.log4j.Logger logger = Logger.getLogger(JGitUtils.class);


    public static Git openRpo(String dir){
        Git git = null;
        try{
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(Paths.get(dir,".git").toFile())
                    .build();
            git = new Git(repository);
        }catch (Exception e){
            e.printStackTrace();
        }
        return git;
    }

    public static Git init(String localPath){
        Git git = null;
        try {
            git = Git.init().setDirectory(new File(localPath)).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return git;
    }

    //将代码克隆到本地
    public static void clone(String remotePath,String position) throws GitAPIException {
        ShellCommandUtils.clone(remotePath,position);
    }
    //对commitMessage进行筛选
    public static List<String> screenCommitMessage(String path,List<String> keyWords) throws IOException {
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(Paths.get(path,".git").toFile())
                .build();
        String branch = repository.getBranch();
        Ref head = repository.findRef("refs/heads/"+branch);
        List<String> commits = new ArrayList<>();
        if (head != null) {
            try (RevWalk revWalk = new RevWalk(repository)) {
                revWalk.markStart(revWalk.parseCommit(head.getObjectId()));
                for (RevCommit revCommit : revWalk) {
                    for (String keyword:keyWords){
                        if (revCommit.getFullMessage().contains(keyword)) {
                            commits.add(path+":\n"+revCommit.getId()+"\t"+revCommit.getFullMessage());
                        }
                    }
//                    System.out.println("\nCommit-Message: " + revCommit.getFullMessage());
                }
                revWalk.dispose();
            }
        }
        return commits;
    }


/*

    //自动push代码
    public static void autoPush(String path,String name)  {
        //对待提交数据做出更改
        File file = new File(path+"/add.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            BufferedWriter bf = null;
            try {
                bf = new BufferedWriter(new FileWriter(file,true));
                bf.write("1");
                bf.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        //更改提交分支为项目名,进行提交
        Git git = null;
        try {
            git = Git.open(new File(path));
            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            //判断分支是否已经存在，没有则创建
            boolean branchExist = false;
            for(Ref branch : branches){
                if(branch.getName().equals("refs/heads/"+name)){
                    branchExist = true;
                    break;
                }
            }
            if(!branchExist){
                git.checkout().setCreateBranch(true).setName(name).call();
            }

            //ssh验证身份

            SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host host, Session session) {
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking","no");
                    session.setConfig(config);

                }
                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch defaulJSch = super.createDefaultJSch(fs);
                    defaulJSch.addIdentity("C:/Users/10247/.ssh/id_ed25519","");
                    return defaulJSch;
                }
            };


            git.add().addFilepattern(".").call();
            git.commit().setMessage("push "+name).call();
            git.push().setTransportConfigCallback(new TransportConfigCallback() {
                        @Override
                        public void configure(Transport transport) {
                            if (transport instanceof SshTransport){
                                SshTransport sshTransport = (SshTransport) transport;
                                sshTransport.setSshSessionFactory(sshSessionFactory);
                            }
                        }
                    }).setRemote("origin")
                    .setRefSpecs(new RefSpec(name)).call();


        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }

    }

*/

}
