package cn.enilu.flash.service.github;

import cn.enilu.flash.BaseApplicationStartTest;
import cn.enilu.flash.bean.entity.github.Email;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class EmailServiceTest extends BaseApplicationStartTest {

    @Resource
    private EmailService emailService;

    @Test
    public void run() {
        List<Email> emails = emailService.queryAll();
        System.out.println(emails);
    }

}