package cn.enilu.flash.api.controller.github;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.cms.Article;
import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.github.GithubService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/github")
public class GithubController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GithubService githubService;
//	@Autowired
//	private EmailService emailService;

	@GetMapping(value = "/list")
	@RequiresPermissions(value = {Permission.GITHUB_LIST})
	public Object list(@RequestParam(required = false) String email,
					   @RequestParam(required = false) String accountName,
					   @RequestParam(required = false) String startDate,
					   @RequestParam(required = false) String endDate
	) {
		Page<Github> page = new PageFactory<Github>().defaultPage();
		page.addFilter("email", SearchFilter.Operator.LIKE, email);
		page.addFilter("accountName", SearchFilter.Operator.EQ, accountName);
		page.addFilter("timeSubmit", SearchFilter.Operator.GTE, DateUtil.parse(startDate, "yyyyMMddHHmmss"));
		page.addFilter("timeSubmit", SearchFilter.Operator.LTE, DateUtil.parse(endDate, "yyyyMMddHHmmss"));
		page = githubService.queryPage(page);
		return Rets.success(page);
	}

	@PostMapping
	@BussinessLog(value = "编辑注册记录", key = "emailAddress")
	@RequiresPermissions(value = {Permission.GITHUB_SAVE})
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
			old.setUserAccount(newGithub.getUserAccount());
//			old.setRsaId(newGithub.getRsaId());
			old.setIdRsaPublic(newGithub.getIdRsaPublic());
			old.setTimeSubmit(new Date());
			old.setTimeValidate(null);
			SecurityUtils.getSubject().getPrincipal();
			old.setModifyBy("" + getLoginUser().getId());
			githubService.update(old);
		} else {
			githubService.insert(newGithub);
		}
		return Rets.success();
	}

	@DeleteMapping
	@BussinessLog(value = "删除文章", key = "id")
	@RequiresPermissions(value = {Permission.ARTICLE_DEL})
	public Object remove(Long id) {
		articleService.delete(id);
		return Rets.success();
	}

	@GetMapping
	@RequiresPermissions(value = {Permission.ARTICLE})
	public Object get(@Param("id") Long id) {
		Article article = articleService.get(id);
		return Rets.success(article);
	}


	@PostMapping
	@BussinessLog(value = "新增github注册", key = "name")
	@RequiresPermissions(value = "githubAdd")
	public Ret add(@RequestBody Github github){
		githubService.insert(github);
		return Rets.success();
	}
	@PutMapping
	@BussinessLog(value = "更新github注册", key = "name")
	@RequiresPermissions(value = "githubUpdate")
	public Ret update(@RequestBody Github github){
		githubService.update(github);
		return Rets.success();
	}
	@DeleteMapping
	@BussinessLog(value = "删除github注册", key = "id")
	@RequiresPermissions(value = "githubDelete")
	public Ret remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		githubService.delete(id);
		return Rets.success();
	}
}