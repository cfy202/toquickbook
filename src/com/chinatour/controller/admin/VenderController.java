package com.chinatour.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chinatour.Message;
import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.entity.Admin;
import com.chinatour.entity.Vender;
import com.chinatour.service.AdminService;
import com.chinatour.service.DataFactoryService;
import com.chinatour.service.DeptService;
import com.chinatour.service.StateService;
import com.chinatour.service.VenderService;
@Controller
@RequestMapping("/admin/vender")
public class VenderController  extends BaseController{
	
	@Resource(name = "venderServiceImpl")
	private VenderService venderService;
	
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@Resource(name="deptServiceImpl")
	private DeptService deptService;
	
	@Resource(name="stateServiceImpl")
	private StateService stateService;
	
	@Autowired
	public DataFactoryService dataFactoryService;
	
	/**
	 * 查询所有
	 */
	@RequestMapping(value = "/customerList" , method = RequestMethod.GET)
	public String customerList(ModelMap model,Vender vender){
		model.addAttribute("menuId", "809");
		model.addAttribute("vender",vender);
		return "/admin/vender/customer";
	}
	
	/**
	 * 异步查询所有
	 */
	@RequestMapping(value = "/customerList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> customerList(Pageable pageable, Vender vender) {
		Admin admin = adminService.getCurrent();
		vender.setDeptId(admin.getDeptId());
		vender.setIsDel(0);
		vender.setType(1);
		Page<Vender> page = venderService.findPage(vender,pageable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}
	/**
	 * 查询所有
	 */
	@RequestMapping(value = "/vendorList" , method = RequestMethod.GET)
	public String vendorList(ModelMap model,Vender vender){
		model.addAttribute("menuId", "810");
		model.addAttribute("vender",vender);
		return "/admin/vender/vendor";
	}
	
	/**
	 * 异步查询所有
	 */
	@RequestMapping(value = "/vendorList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> vendorList(Pageable pageable, Vender vender) {
		Admin admin = adminService.getCurrent();
		vender.setDeptId(admin.getDeptId());
		vender.setIsDel(0);
		vender.setType(2);
		Page<Vender> page = venderService.findPage(vender,pageable);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}
	
	/**
	 * 同步customer
	 * @param customerIds
	 * @return
	 */
	@RequestMapping(value = "/synchronousCustomer", method = RequestMethod.GET)
	public String synchronousCustomer(String customerIds,RedirectAttributes redirectAttributes){
		String message = "";
		int a=0;
		int b=0;
		String[] customerIdList = customerIds.split(",");
		Vender vender = new Vender();
		for(int i=0;i<customerIdList.length;i++){
			Vender customer = venderService.findById(customerIdList[i]);
			vender.setVenderId(customerIdList[i]);
				if(dataFactoryService.createCustomer(customer)){
					vender.setIsSuccess(1);
					venderService.update(vender);
					a++;
				}else{
					vender.setIsSuccess(2);
					venderService.update(vender);
					b++;
				}
		}
		message = "同步成功"+a+"条,"+"同步失败"+b+"条";
		addFlashMessage(redirectAttributes, new Message(Message.Type.success,message));
		return "redirect:customerList.jhtml";
	} 
	
	/**
	 * 同步customer
	 * @param customerIds
	 * @return
	 */
	@RequestMapping(value = "/synchronousVendor", method = RequestMethod.GET)
	public String synchronousVendor(String vendorIds,RedirectAttributes redirectAttributes){
		String message = "";
		int a=0;
		int b=0;
		String[] vendorIdList = vendorIds.split(",");
		Vender vender = new Vender();
		for(int i=0;i<vendorIdList.length;i++){
			Vender vendor = venderService.findById(vendorIdList[i]);
			vender.setVenderId(vendorIdList[i]);
			if(dataFactoryService.createVendor(vendor)){
				vender.setIsSuccess(1);
				venderService.update(vender);
				a++;
			}else{
				vender.setIsSuccess(2);
				venderService.update(vender);
				b++;
			}
		}
		message = "同步成功"+a+"条,"+"同步失败"+b+"条";
		addFlashMessage(redirectAttributes, new Message(Message.Type.success,message));
		return "redirect:vendorList.jhtml";
	} 
} 
