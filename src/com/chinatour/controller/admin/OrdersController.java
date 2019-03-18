package com.chinatour.controller.admin;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.chinatour.Constant;
import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.Message;
import com.chinatour.entity.Order;
import com.chinatour.service.DataFactoryService;
import com.chinatour.service.AdminService;
import com.chinatour.service.OrderService;
import com.chinatour.service.TOrderReceiveItemService;
import com.chinatour.service.TourInfoForOrderService;
import com.chinatour.vo.TourOrderListVO;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * @copyright Copyright: 2014
 * @author Pis
 * @create-time 下午2:38:35
 * @reversion 3.0
 */
@Controller
@RequestMapping("/admin/orders")
public class OrdersController extends BaseController {

	public static final TemplateHashModel CONSTANT;
	
	@Autowired
	public DataFactoryService dataFactoryService;
	
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

	/**
	 * Item
	 * Office查看本部门订单列表
	 */
	@RequestMapping(value = "/itemOfficeList", method = RequestMethod.GET)
	public String itemOfficeList(ModelMap model) {
		model.addAttribute("menuId", "310");
		return "/admin/order/itemOfficeList"; 
	}

	/**
	 * Item
	 * Office查看本部门订单列表
	 * 
	 * @param pageable
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/itemOfficeList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> itemOfficeList(Pageable pageable, Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		//order.setDeptId(Constant.HDOFFICE);
		order.setDeptId(this.adminService.getCurrent().getDeptId());
		Page<TourOrderListVO> page = orderService.findTourOrderListVO(order, pageable);
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}

	/**
	 * Item
	 * Office查看本部门订单列表
	 */
	@RequestMapping(value = "/itemOfficeListForEdit", method = RequestMethod.GET)
	public String itemOfficeListForEdit(ModelMap model) {
		model.addAttribute("menuId", "311");
		return "/admin/order/itemOfficeListForEdit"; 
	}

	/**
	 * Item
	 * Office查看本部门订单列表
	 * 
	 * @param pageable
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/itemOfficeListForEdit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> itemOfficeListForEdit(Pageable pageable, Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		//order.setDeptId(Constant.HDOFFICE);
		order.setDeptId(this.adminService.getCurrent().getDeptId());
		order.setIsSuccess(1);
		order.setIsEdit(1);
		Page<TourOrderListVO> page = orderService.findTourOrderListVO(order, pageable);
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}
	
	
	/**
	 * 同步团订单进入qb
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value = "/synchronousForTourOrder", method = RequestMethod.POST)
	public 
	String synchronousForTourOrder(String orderIds,Integer type,RedirectAttributes redirectAttributes){
		String message = "";
		int a=0;
		int b=0;
		String[] orderIdList = orderIds.split(",");
		Order ord = new Order();
		if(type==0){
			for(int i=0;i<orderIdList.length;i++){
				Order order = orderService.findById(orderIdList[i]);
				ord.setId(orderIdList[i]);
					if(dataFactoryService.saveOrderToAccData(order)){
						  	ord.setIsSuccess(1);
						if(order.getTourCode()!=null&&order.getTourCode().length()!=0){
							ord.setIfTourSuc(1);
						}
							orderService.update(ord);
							a++;
					}else{
							ord.setIsSuccess(2);
							orderService.update(ord);
							b++;
					}
			}
		}else{
			for(int i=0;i<orderIdList.length;i++){
				ord.setIsSuccess(3);
				ord.setId(orderIdList[i]);
				orderService.update(ord);
				a++;
			}
		}
		message = "同步成功"+a+"条,"+"同步失败"+b+"条";
		addFlashMessage(redirectAttributes, new Message(Message.Type.success,message));
		return "redirect:itemOfficeList.jhtml";
	}
	
	
	/**
	 * 修改订单进入qb
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value = "/synchronousForEditOrder", method = RequestMethod.POST)
	public String synchronousForEditOrder(String orderIds,Integer type,RedirectAttributes redirectAttributes){
		String message = "";
		int a=0;
		int b=0;
		String[] orderIdList = orderIds.split(",");
		Order ord = new Order();
		if(type==0){
			for(int i=0;i<orderIdList.length;i++){
				Order order = orderService.findById(orderIdList[i]);
				ord.setId(orderIdList[i]);
			
					if(dataFactoryService.updateTourOrderInfo(order)){
						ord.setIfUpdateSuc(1);
						ord.setIsEdit(0);
						orderService.update(ord);
						a++;
					}else{
						ord.setIfUpdateSuc(2);
						orderService.update(ord);
						b++;
					}
			}
		}else{
			for(int i=0;i<orderIdList.length;i++){
				ord.setIfUpdateSuc(3);
				ord.setId(orderIdList[i]);
				orderService.update(ord);
				a++;
			}
		}
		message = "同步成功"+a+"条,"+"同步失败"+b+"条";
		addFlashMessage(redirectAttributes, new Message(Message.Type.success,message));
		return "redirect:itemOfficeListForEdit.jhtml";
	}
	
	/**
	 * Item
	 * Office查看本部门订单列表
	 */
	@RequestMapping(value = "/itemOfficeListForTour", method = RequestMethod.GET)
	public String itemOfficeListForTour(ModelMap model) {
		model.addAttribute("menuId", "312");
		return "/admin/order/itemOfficeListForTour"; 
	}

	/**
	 * Item
	 * Office查看本部门订单列表
	 * @param pageable
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/itemOfficeListForTour", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> itemOfficeListForTour(Pageable pageable, Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		//order.setDeptId(Constant.HDOFFICE);
		order.setDeptId(this.adminService.getCurrent().getDeptId());
		order.setIsSuccess(1);
		order.setOrderType(5);
		Page<TourOrderListVO> page = orderService.findTourOrderListVO(order, pageable);
		map.put("recordsTotal", page.getTotal());
		map.put("recordsFiltered", page.getTotal());
		map.put("data", page.getContent());
		return map;
	}
	
	
	/**
	 * 订单组团后将class同步进qb
	 * @param orderIds
	 * @return
	 */
	@RequestMapping(value = "/synchronousForTour", method = RequestMethod.POST)
	public String synchronousForTour(String orderIds,Integer type,RedirectAttributes redirectAttributes){
		String message = "";
		int a=0;
		int b=0;
		String[] orderIdList = orderIds.split(",");
		Order ord = new Order();
		if(type==0){
			for(int i=0;i<orderIdList.length;i++){
				ord = orderService.findById(orderIdList[i]);
				if(dataFactoryService.updateClass(ord)){
					ord.setIfTourSuc(1);
					a++;
				}else{
					ord.setIfTourSuc(2);
					b++;
				}
				orderService.update(ord);
			}
		}else{
			for(int i=0;i<orderIdList.length;i++){
				ord.setIfTourSuc(3);
				ord.setId(orderIdList[i]);
				orderService.update(ord);
				a++;
			}
		}
		message = "同步成功"+a+"条,"+"同步失败"+b+"条";
		addFlashMessage(redirectAttributes, new Message(Message.Type.success,message));
		return "redirect:itemOfficeListForTour.jhtml";
	}


    
    
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private TourInfoForOrderService tourInfoForOrderService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private TOrderReceiveItemService tOrderReceiveItemService;
	
	@Autowired
	private OrderService orderService;
}
