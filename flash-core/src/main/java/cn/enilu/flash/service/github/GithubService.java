package cn.enilu.flash.service.github;


import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.dao.github.GithubRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubService extends BaseService<Github,Long,GithubRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GithubRepository githubRepository;

}

