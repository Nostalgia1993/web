package cn.enilu.flash.service.github;


import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.bean.entity.github.Email;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.dao.github.GithubRepository;

import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GithubService extends BaseService<Github,Long,GithubRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GithubRepository githubRepository;

    @Resource
    private EmailService emailService;

    public void readAccountAndCommit() {
        Page<Github> page = new PageFactory<Github>().defaultPage();
        page.addFilter("emailAddress", SearchFilter.Operator.LIKE, "liunian0714@163.com");
        Page<Github> githubPage = this.queryPage(page);
        System.out.println(githubPage.getRecords().get(0));

        for (Github record : githubPage.getRecords()) {
            submitCode(record);
        }
    }

    public void submitCode(Github record) {
        try {

            String filePath = "C:\\Users\\liunian-jk\\Desktop\\doc\\github\\web\\";
            String fileName = filePath + "README2.md";

            //修改代码:向readme文件写入一行代码
            updateReadMe(fileName);

            //切换用户
            switchUser(record);

            //添加代码到暂存区
            String addCommand = String.format("git add %s", fileName);
            List<String> addResult = execCommand(addCommand);
            System.out.println(addResult);

            //提交代码
            //获取需要提交的代码命令

            String commitCommand = getCommitCommand(filePath);
            //切换用户
            switchUser(record);
            List<String> commitResult = execCommand(commitCommand);
            System.out.println(commitResult);

            //push代码
            String emailAddress = record.getEmailAddress();
            String pushCode;
            if (emailAddress.equals("liunian0714@163.com")) {
                pushCode = "git push origin master";
            } else {
                pushCode = "git push " + emailAddress.substring(0, emailAddress.indexOf("@")) + "master";
            }
            execCommand(pushCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void switchUser(Github record) {
        String command1 = "git config user.name " + record.getGithubName();
        List<String> result1 = execCommand(command1);
        System.out.println(result1);

        String command2 = "git config user.name " + record.getEmailAddress();
        List<String> result2 = execCommand(command2);
        System.out.println(result2);
    }

    private void updateReadMe(String filePath) throws Exception {
        Files.write(Paths.get(filePath), String.format("\nhello world -- %s", new Date()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }


    private String getCommitCommand(String filePath) {
        return String.format("git commit -m \"update %s \" %s", new Date(), filePath);
    }



    //这个方法比第一个好，执行时不会卡  stmt要执行的命令
    public List<String> execCommand(String command){
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        try {
            File file = new File("C:\\Users\\liunian-jk\\Desktop\\doc\\github2");
            File tmpFile = new File("C:\\Users\\liunian-jk\\Desktop\\doc\\github2\\temp.tmp");//新建一个用来存储结果的缓存文件
            if (!file.exists()){
                file.mkdirs();
            }
            if(!tmpFile.exists()) {
                tmpFile.createNewFile();
            }
            ProcessBuilder pb = new ProcessBuilder().command("cmd.exe", "/c", command).inheritIO();
            pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            pb.redirectOutput(tmpFile);//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            InputStream in = new FileInputStream(tmpFile);
            br= new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = br.readLine()) != null) {
                System.out.println(line);
                list.add(line);
            }
            br.close();
            br = null;
//            tmpFile.delete();//卸磨杀驴。
            System.out.println("执行完成");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    private void printCommand(Process process) {
        // Read the output of the command
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.destroy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

