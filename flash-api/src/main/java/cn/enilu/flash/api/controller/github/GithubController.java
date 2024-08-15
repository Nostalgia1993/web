package cn.enilu.flash.api.controller.github;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.github.Github;
import cn.enilu.flash.service.github.GithubService;

import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;

import cn.enilu.flash.utils.factory.Page;


import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register/info")
public class GithubController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GithubService githubService;

	@GetMapping(value = "/list")
	@RequiresPermissions(value = "github")
	public Ret list(@RequestParam(required = false) Long id) {
		Page<Github> page = new PageFactory<Github>().defaultPage();
		page.addFilter("id",id);
		page = githubService.queryPage(page);
		return Rets.success(page);
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