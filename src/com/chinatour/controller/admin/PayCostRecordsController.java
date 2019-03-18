package com.chinatour.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.chinatour.Constant;
import com.chinatour.Message;
import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.entity.Admin;
import com.chinatour.entity.PayCostRecords;
import com.chinatour.service.AdminService;
import com.chinatour.service.DataFactoryService;
import com.chinatour.service.DeptService;
import com.chinatour.service.OrderService;
import com.chinatour.service.PayCostRecordsService;
import com.chinatour.service.TourService;
import com.chinatour.service.VenderService;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * 订单收款
 * 
 * @copyright Copyright: 2014
 * @author jacky
 * @create-time 2014-10-13 下午2:43:12
 * @revision 3.0
 */

@Controller
@RequestMapping("/admin/payCostRecords")
public class PayCostRecordsController extends BaseController implements ServletContextAware {
	
	public static final TemplateHashModel CONSTANT;

	static {
		TemplateHashModel constant = null;
		try {
			constant = BeansWrapper.getDefaultInstance().getStaticModels();
			constant = (TemplateHashModel) constant.get("com.chinatour.Constant");
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
		CONSTANT = constant;
	}

	@Resource(name = "payCostRecordsServiceImpl")
	private PayCostRecordsService payCostRecordsService;

	@Autowired
	private AdminService adminService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "tourServiceImpl")
	private TourService tourService;

	@Resource(name = "deptServiceImpl")
	private DeptService deptService;

	@Autowired
	private VenderService venderService;
	
	@Autowired
	private DataFactoryService dataFactoryService;
	
	
	/** servletContext */
	private ServletContext servletContext;
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	/**
	 * 查询收入所有
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model,PayCostRecords pay) {
		//传Null值进入判断
		if(pay.getOrderNo()!=null && pay.getOrderNo().equals("null")){
			pay.setOrderNo(null);
		}
		if(pay.getTourCode()!=null && pay.getTourCode().equals("null")){
			pay.setTourCode(null);
		}
		if(pay.getSum()!=null && pay.getSum().equals("null")){
			pay.setSum(null);
		}
		if(pay.getUserName()!=null && pay.getUserName().equals("null")){
			pay.setUserName(null);
		}
		if(pay.getCode()!=null && pay.getCode().equals("null")){
			pay.setCode(null);
		}
		if(pay.getRemark()!=null && pay.getRemark().equals("null")){
			pay.setRemark(null);
		}
		/*if(pay.getOrderNo().equals("null")){
			pay.setOrderNo(null);
		}*/
		model.addAttribute("pay",pay);
		model.addAttribute("constant", CONSTANT);
		model.addAttribute("menuId", "503");
		return "/admin/finance/orderFinance/list";
	}

	/**
	 * 异步查询收入所有
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> list(Pageable pageable, PayCostRecords payCostRecords) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 插入部门id
		payCostRecords.setDeptIdString(adminService.getCurrent().getDeptId());
		payCostRecords.setPayOrCost(1);
		payCostRecords.setStatus(1);
		Page<PayCostRecords> page = payCostRecordsService.findPage(
				payCostRecords, pageable);
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}
	/**
	 * 查询支出所有
	 */
	@RequestMapping(value = "/payList", method = RequestMethod.GET)
	public String payList(ModelMap model,PayCostRecords pay) {
		if(pay.getOrderNo()!=null && pay.getOrderNo().equals("null")){
			pay.setOrderNo(null);
		}
		if(pay.getTourCode()!=null && pay.getTourCode().equals("null")){
			pay.setTourCode(null);
		}
		if(pay.getSum()!=null && pay.getSum().equals("null")){
			pay.setSum(null);
		}
		if(pay.getUserName()!=null && pay.getUserName().equals("null")){
			pay.setUserName(null);
		}
		if(pay.getCode()!=null && pay.getCode().equals("null")){
			pay.setCode(null);
		}
		if(pay.getRemark()!=null && pay.getRemark().equals("null")){
			pay.setRemark(null);
		}
		if(pay.getVenderString()!=null && pay.getVenderString().equals("null")){
			pay.setVenderString(null);
		}
		model.addAttribute("pay",pay);
		model.addAttribute("constant", CONSTANT);
		model.addAttribute("menuId", "503");
		return "/admin/finance/orderFinance/payList";
	}

	/**
	 * 异步查询支出所有
	 */
	@RequestMapping(value = "/payList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> payList(Pageable pageable, PayCostRecords payCostRecords) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 插入部门id
		payCostRecords.setDeptIdString(adminService.getCurrent().getDeptId());
		payCostRecords.setPayOrCost(2);
		payCostRecords.setStatus(1);
		Page<PayCostRecords> page = payCostRecordsService.findCostForPage(
				payCostRecords, pageable);
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}


	/**
	 * 修改
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(PayCostRecords pay, String[] recordsId,String temp,String InvoiceNos,String ways,
			String recordString, String munId,RedirectAttributes redirectAttributes) {
		PayCostRecords payTemp = new PayCostRecords();
		if (recordString != null) {
			/**
			 * 财务系统接口
			 */
		    payTemp = payCostRecordsService.findById(recordString);
			if(dataFactoryService.orderToAccData(payTemp)){
				payTemp.setIsSuccess(1);
				payTemp.setApproveDate(new Date());
				payCostRecordsService.update(payTemp);
				addFlashMessage(redirectAttributes,new Message(Message.Type.success,"同步成功"));
			}else{
				payTemp.setIsSuccess(2);
				payTemp.setApproveDate(new Date());
				payCostRecordsService.update(payTemp);
				addFlashMessage(redirectAttributes,new Message(Message.Type.danger,"同步失败"));
			}
		} else {
				int a = 0;
				int b = 0;
				String message = "";
			for (String id : recordsId) {
				/**
				 * 财务系统接口
				 */
					payTemp = payCostRecordsService.findById(id);
					if(!dataFactoryService.orderToAccData(payTemp)){
						b++;
						payTemp.setIsSuccess(2);
						payCostRecordsService.update(payTemp);
					}else{
						a++;
						payTemp.setIsSuccess(1);
						payTemp.setApproveDate(new Date());
						payCostRecordsService.update(payTemp);
					}
			}
			message = "同步成功"+a+"条,同步失败"+b+"条";
			addFlashMessage(redirectAttributes,new Message(Message.Type.success,message));
		}
			if(temp!=null&&temp.equals("pay")){//判断是收入页面还是支出
				return "redirect:payList.jhtml";
			}
			return "redirect:list.jhtml";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/editForNot", method = RequestMethod.POST)
	public String editForNot(PayCostRecords pay, String[] recordsId,String temp,String InvoiceNos,String ways,
			String recordString, String munId,RedirectAttributes redirectAttributes) {
		PayCostRecords payTemp = new PayCostRecords();
		if(recordString!=null){
			payTemp = payCostRecordsService.findById(recordString);
			payTemp.setIsSuccess(3);
			payTemp.setApproveDate(new Date());
			payCostRecordsService.update(payTemp);
		}else{
			for (String id : recordsId) {
				payTemp = payCostRecordsService.findById(id);
				payTemp.setIsSuccess(3);
				payCostRecordsService.update(payTemp);
			}
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		if(temp!=null&&temp.equals("pay")){//判断是收入页面还是支出
			return "redirect:payList.jhtml";
		}
		return "redirect:list.jhtml";
	}
	
	/**
	 * 查询
	 * 
	 * @param model(传入了tourCode)
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public @ResponseBody
	PayCostRecords findById(String id) {
		String tourCode=orderService.findById(payCostRecordsService.findById(id).getOrderId()).getTourCode();
		if(tourCode==""||tourCode==null){
			tourCode="";
		}
		PayCostRecords payCostRecords=payCostRecordsService.findById(id);
		payCostRecords.setTourCode(tourCode);
		return payCostRecords;
	}

}

