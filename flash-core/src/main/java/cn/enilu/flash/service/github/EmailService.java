package cn.enilu.flash.service.github;


import cn.enilu.flash.bean.entity.test.Email;
import cn.enilu.flash.dao.test.EmailRepository;

import cn.enilu.flash.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends BaseService<Email,Long,EmailRepository>  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EmailRepository emailRepository;

}

