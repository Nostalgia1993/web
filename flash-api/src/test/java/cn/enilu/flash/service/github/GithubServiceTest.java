package cn.enilu.flash.service.github;

import cn.enilu.flash.BaseApplicationStartTest;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GithubServiceTest extends BaseApplicationStartTest {

    @Autowired
    private GithubService githubService;


    @Test
    public void queryPage() {
        Page<Github> page = new PageFactory<Github>().defaultPage();
        page.addFilter("emailAddress", SearchFilter.Operator.LIKE, "NGC2237-001");
        page.addFilter("githubName", SearchFilter.Operator.EQ, "NGC2237-001");
        page.addFilter("timeSubmit", SearchFilter.Operator.GTE, DateUtil.parse("2024-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        page.addFilter("timeSubmit", SearchFilter.Operator.LTE, DateUtil.parse("2025-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        Page<Github> githubPage = githubService.queryPage(page);
        System.out.println(githubPage.getRecords().get(0));
    }

    @Test
    public void submit() {
        githubService.readAccountAndCommit();
    }

}