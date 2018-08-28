package ${projectInfo.packageName}.${projectInfo.modelName}.controller;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ${projectInfo.packageName}.${projectInfo.modelName}.annotation.PermissionsAnnotation;
import ${projectInfo.packageName}.${projectInfo.modelName}.dto.${po.type.typeName}DTO;
import ${projectInfo.packageName}.${projectInfo.modelName}.entity.${po.type.typeName};
import ${projectInfo.packageName}.${projectInfo.modelName}.service.${po.type.typeName}Service;
import ${projectInfo.packageName}.${projectInfo.modelName}.utils.Servlets;
import ${projectInfo.packageName}.${projectInfo.modelName}.vo.AjaxMessage;
import ${projectInfo.packageName}.${projectInfo.modelName}.vo.LayTableRequestVO;
import ${projectInfo.packageName}.${projectInfo.modelName}.vo.LayTableResponseVO;

/**
 * ${po.remark}控制器
 * @author ${projectInfo.author}
 *
 */
@Controller
@RequestMapping(value = "/${projectInfo.modelName}/${po.type.typeName?lower_case}")
public class ${po.type.typeName}Controller {
	
	@Autowired
	private ${po.type.typeName}Service ${po.type.typeName?lower_case}Service;
	
	/**
	 * 跳转到列表页
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Search",permissionRemark="${po.remark}查询权限",parentPermission="", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/list",resourceRemark="跳转到${po.remark}列表")
	@RequestMapping(value = "list")
	public String list(Model model) {
		return "${projectInfo.modelName}/${po.type.typeName?lower_case}/list";
	}
	
	/**
	 * 跳转选择页
	 * 
	 * @return
	 */
	@RequestMapping(value = "select")
	public String select() {
		return "${projectInfo.modelName}/${po.type.typeName?lower_case}/select";
	}


	/**
	 * 列表页查询
	 * @param pages
	 * @param model
	 * @param request
	 * @param result
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Search",permissionRemark="${po.remark}查询权限",parentPermission="", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/queryList",resourceRemark="${po.remark}列表搜索")
	@RequestMapping(value = "queryList")
	public @ResponseBody LayTableResponseVO<${po.type.typeName}DTO> queryList(LayTableRequestVO pages, Model model,
			ServletRequest request, BindingResult result) {
		LayTableResponseVO<${po.type.typeName}DTO> jqGridResponseVO = new LayTableResponseVO<${po.type.typeName}DTO>();
		if(result.hasErrors()){
			jqGridResponseVO.setCode(500);
			jqGridResponseVO.setMsg("参数验证失败");
			return jqGridResponseVO;
		}
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<${po.type.typeName}> ${po.type.typeName?lower_case}Page = ${po.type.typeName?lower_case}Service.searchListPage(searchParams, pages.toPageRequest());
		jqGridResponseVO.setCount(${po.type.typeName?lower_case}Page.getTotalElements());
		jqGridResponseVO.setData(${po.type.typeName}DTO.transformAllTo${po.type.typeName}DTO(${po.type.typeName?lower_case}Page.getContent()));
		return jqGridResponseVO;
	}

	/**
	 * 跳转到增加页
	 * 
	 * @param model
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Save",permissionRemark="${po.remark}创建与修改权限",parentPermission="${projectInfo.modelName}${po.type.typeName}Search", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/add",resourceRemark="跳转到增加${po.remark}页")
	@RequestMapping("/add")
	public String add(Model model) {
		return "${projectInfo.modelName}/${po.type.typeName?lower_case}/add";
	}

	/**
	 * 保存
	 * 
	 * @param ${po.type.typeName?lower_case}
	 * @param result
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Save",permissionRemark="${po.remark}创建与修改权限",parentPermission="${projectInfo.modelName}${po.type.typeName}Search", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/save",resourceRemark="保存${po.remark}")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage save(@Valid ${po.type.typeName} ${po.type.typeName?lower_case}, BindingResult result) {
		if (result.hasErrors()) {
			return AjaxMessage.createFailureMsg();
		}
		${po.type.typeName?lower_case}Service.save(${po.type.typeName?lower_case});
		return AjaxMessage.createSuccessMsg();
	}
	
	/**
	 * 跳转修改页
	 * @param id
	 * @param model
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Save",permissionRemark="${po.remark}创建与修改权限",parentPermission="${projectInfo.modelName}${po.type.typeName}Search", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/edit",resourceRemark="跳转到修改${po.remark}页")
	@RequestMapping(value = "edit/{id}")
	public String edit(@PathVariable("id") String id,Model model) {
		model.addAttribute("${po.type.typeName?lower_case}", ${po.type.typeName?lower_case}Service.findOne(id));
		return "${projectInfo.modelName}/${po.type.typeName?lower_case}/edit";

	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Delete",permissionRemark="${po.remark}删除权限",parentPermission="${projectInfo.modelName}${po.type.typeName}Search", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/delete",resourceRemark="删除${po.remark}")
	@RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage delete(@PathVariable("id") String id) {
		${po.type.typeName?lower_case}Service.deleteByLogic(id);
		return AjaxMessage.createSuccessMsg();

	}
	

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@PermissionsAnnotation(permission = "${projectInfo.modelName}${po.type.typeName}Delete",permissionRemark="${po.remark}删除权限",parentPermission="${projectInfo.modelName}${po.type.typeName}Search", url = "/${projectInfo.modelName}/${po.type.typeName?lower_case}/deleteAll",resourceRemark="批量删除${po.remark}")
	@RequestMapping(value = "deleteAll", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMessage deleteAll(@RequestParam("ids[]") String... ids) {
		${po.type.typeName?lower_case}Service.deleteAllByLogic(ids);
		return AjaxMessage.createSuccessMsg();

	}
	
}
