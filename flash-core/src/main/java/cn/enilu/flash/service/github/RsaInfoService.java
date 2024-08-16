package cn.enilu.flash.service.github;


import cn.enilu.flash.bean.entity.test.RsaInfo;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.common.utils.SSHKeyGeneratorUtils;
import cn.enilu.flash.dao.test.RsaInfoRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class RsaInfoService extends BaseService<RsaInfo,Long,RsaInfoRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RsaInfoRepository rsaInfoRepository;

    public static final String USER_HOME_PATH = System.getProperty("user.home");

    public RsaInfo queryByAddress(String emailAddress) {
        SearchFilter addressFilter = SearchFilter.build("emailAddress", SearchFilter.Operator.EQ, emailAddress);
        return this.get(addressFilter);
    }

    public String generalRas(String email) {
        //截取邮件别名
        String userName = email.substring(0, email.indexOf("@"));
        try {
            //生成本地ssh key
            SSHKeyGeneratorUtils.generateSSHKey(userName, email);

            //在config文件后面追加用户配置
            String configPath = USER_HOME_PATH + "/.ssh/config";
            //在config后面追加以下内容
            //Host github_170.com
            //HostName github.com
            //User git
            //IdentityFile ~/.ssh/id_rsa_170
            StringBuilder sb = new StringBuilder();
            String appendString = sb.append("\r\nHost github_").append(userName).append(".com\n")
                    .append("HostName github.com\n")
                    .append("User git\n").append("IdentityFile ~/.ssh/id_rsa_").append(userName).append("\n")
                    .toString();
            System.out.println(appendString);
            Files.write(Paths.get(configPath), appendString.getBytes(), StandardOpenOption.APPEND);

            //读取key并存到数据库
            String publicKeyPath = USER_HOME_PATH + "/.ssh/id_rsa_" + userName + ".pub";
            String privateKeyPath = USER_HOME_PATH + "/.ssh/id_rsa_" + userName;

            //将sshKey保存到数据库
            String publicKey = new String(Files.readAllBytes(Paths.get(publicKeyPath)));
            String privateKey = new String(Files.readAllBytes(Paths.get(privateKeyPath)));

            //将sshKey保存到数据库
            RsaInfo rsaInfo = new RsaInfo();
            rsaInfo.setEmailAddress(email);
            rsaInfo.setUserRsaName(userName);
            rsaInfo.setIdRsaPublic(publicKey);
            rsaInfo.setIdRsa(privateKey);
            rsaInfo.setStatus("0");
            insert(rsaInfo);
            System.out.println(publicKey);
            return publicKey;

        } catch (Exception e) {
            System.out.println("生成ssh key失败, e:" + e.getMessage());
            return null;
        }
    }
}

