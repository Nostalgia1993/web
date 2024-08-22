package cn.enilu.flash.api.controller.github;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.bean.entity.test.RsaInfo;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.github.GithubService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.service.github.EmailService;
import cn.enilu.flash.service.github.RsaInfoService;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/github")
public class GithubController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private GithubService githubService;
	@Resource
	private RsaInfoService rsaInfoService;


	@GetMapping(value = "/list")
//	@RequiresPermissions(value = {Permission.GITHUB})
	public Object list(@RequestParam(required = false) String emailAddress,
					   @RequestParam(required = false) String githubName,
					   @RequestParam(required = false) String startDate,
					   @RequestParam(required = false) String endDate
	) {
		Page<Github> page = new PageFactory<Github>().defaultPage();
		page.addFilter("emailAddress", SearchFilter.Operator.LIKE, emailAddress);
		page.addFilter("githubName", SearchFilter.Operator.EQ, githubName);
		page.addFilter("timeSubmit", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
		page.addFilter("timeSubmit", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
		page = githubService.queryPage(page);
		return Rets.success(page);
	}

	@PostMapping(value = "/save")
	@BussinessLog(value = "保存注册记录", key = "emailAddress")
//	@RequiresPermissions(value = {Permission.GITHUB})
	public Object save() {
		Github newGithub = getFromJson(Github.class);
		if (newGithub.getId() != null) {
			Github old = githubService.get(newGithub.getId());
			if (old == null) {
				return Rets.failure(BizExceptionEnum.REQUEST_RECORD_NOT_EXISTS);
			}
			//判断修改后的邮箱地址是否已存在
			SearchFilter emailAddressSearch = SearchFilter.build("emailAddress", SearchFilter.Operator.LIKE, newGithub.getEmailAddress());
			Github githubByAddress = githubService.get(emailAddressSearch);
			if (githubByAddress != null && !githubByAddress.getId().equals(old.getId())) {
				return Rets.failure(BizExceptionEnum.REQUEST_EMAIL_EXISTS);
			}

			old.setEmailAddress(newGithub.getEmailAddress());
			old.setGithubName(newGithub.getGithubName());
			old.setRepositoryName(newGithub.getRepositoryName());
			old.setSshUrl(newGithub.getSshUrl());
			old.setPassword(newGithub.getPassword());

			// TODO: 2024/8/16 先设置为id
			old.setUserAccount(this.getIdUserString());

			RsaInfo rsaInfo = rsaInfoService.queryByAddress(newGithub.getEmailAddress());
			if (rsaInfo == null) {
				return Rets.failure(BizExceptionEnum.REQUEST_EMAIL_EXISTS);
			}

			old.setIdRsaPublic(rsaInfo.getIdRsaPublic());
			old.setRsaId(rsaInfo.getId());
			old.setTimeSubmit(new Date());
			old.setTimeValidate(null);

			old.setModifyBy(this.getIdUser());
			githubService.update(old);
		} else {
			RsaInfo rsaInfo = rsaInfoService.queryByAddress(newGithub.getEmailAddress());
			if (rsaInfo == null) {
				return Rets.failure(BizExceptionEnum.REQUEST_RSA_NOT_EXISTS);
			}
			newGithub.setIdRsaPublic(rsaInfo.getIdRsaPublic());
			newGithub.setRsaId(rsaInfo.getId());

			// TODO: 2024/8/16 先设置为id
			newGithub.setUserAccount(this.getIdUserString());
			newGithub.setTimeSubmit(new Date());
			newGithub.setCreateTime(new Date());
			newGithub.setCreateBy(this.getIdUser());
			newGithub.setModifyBy(this.getIdUser());
			githubService.insert(newGithub);
			//设置rsa为已关联
			rsaInfo.setStatus("1");
			rsaInfoService.update(rsaInfo);
		}
		return Rets.success();
	}

	@GetMapping(value = "/generalRsa")
//    @RequiresPermissions(value = {Permission.GITHUB})
    public Object generalRsa(@Param("emailAddress") String emailAddress) {
		String publicKey = rsaInfoService.generalRas(emailAddress);
        return Rets.success(publicKey);
    }

	@GetMapping(value = "/rsaList")
//	@RequiresPermissions(value = {Permission.GITHUB})
	public Object rsaList(@RequestParam(required = false) String emailAddress,
					   @RequestParam(required = false) String startDate,
					   @RequestParam(required = false) String endDate
	) {
		Page<RsaInfo> page = new PageFactory<RsaInfo>().defaultPage();
		page.addFilter("emailAddress", SearchFilter.Operator.LIKE, emailAddress);
		page.addFilter("timeSubmit", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
		page.addFilter("timeSubmit", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
		page = rsaInfoService.queryPage(page);
		List<RsaInfo> records = page.getRecords();
		for (RsaInfo record : records) {
			record.setIdRsa("");
		}
		return Rets.success(page);
	}

	@GetMapping(value = "/get")
	@RequiresPermissions(value = {Permission.GITHUB})
	public Object get(@Param("id") Long id) {
		Github github = githubService.get(id);
		return Rets.success(github);
	}

}