package cn.enilu.flash.api.controller.test;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.entity.test.RsaInfo;
import cn.enilu.flash.service.github.RsaInfoService;

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
@RequestMapping("/rsa/info")
public class RsaInfoController extends BaseController {
	private  Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RsaInfoService rsaInfoService;

	@GetMapping(value = "/list")
	@RequiresPermissions(value = "rsaInfo")
	public Ret list(@RequestParam(required = false) Long id) {
		Page<RsaInfo> page = new PageFactory<RsaInfo>().defaultPage();
		page.addFilter("id",id);
		page = rsaInfoService.queryPage(page);
		return Rets.success(page);
	}
	@PostMapping
	@BussinessLog(value = "新增rsa信息", key = "name")
	@RequiresPermissions(value = "rsaInfoAdd")
	public Ret add(@RequestBody RsaInfo rsaInfo){
		rsaInfoService.insert(rsaInfo);
		return Rets.success();
	}
	@PutMapping
	@BussinessLog(value = "更新rsa信息", key = "name")
	@RequiresPermissions(value = "rsaInfoUpdate")
	public Ret update(@RequestBody RsaInfo rsaInfo){
		rsaInfoService.update(rsaInfo);
		return Rets.success();
	}
	@DeleteMapping
	@BussinessLog(value = "删除rsa信息", key = "id")
	@RequiresPermissions(value = "rsaInfoDelete")
	public Ret remove(Long id){
		if (id == null) {
			throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
		}
		rsaInfoService.delete(id);
		return Rets.success();
	}
}