package cn.enilu.flash.api.controller.test;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.test.Email;
import cn.enilu.flash.service.test.EmailService;

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
@RequestMapping("/email")
public class EmailController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EmailService emailService;

	@GetMapping(value = "/list")
	@RequiresPermissions(value = "email")
	public Ret list(@RequestParam(required = false) Long id) {
		Page<Email> page = new PageFactory<Email>().defaultPage();
		page.addFilter("id",id);
		page = emailService.queryPage(page);
		return Rets.success(page);
	}
	@PostMapping
	@BussinessLog(value = "新增邮箱", key = "name")
	@RequiresPermissions(value = "emailAdd")
	public Ret add(@RequestBody Email email){
		emailService.insert(email);
		return Rets.success();
	}
	@PutMapping
	@BussinessLog(value = "更新邮箱", key = "name")
	@RequiresPermissions(value = "emailUpdate")
	public Ret update(@RequestBody Email email){
		emailService.update(email);
		return Rets.success();
	}
	@DeleteMapping
	@BussinessLog(value = "删除邮箱", key = "id")
	@RequiresPermissions(value = "emailDelete")
	public Ret remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		emailService.delete(id);
		return Rets.success();
	}
}