package com.chinatour.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.entity.Customer;
import com.chinatour.entity.CustomerFlight;
import com.chinatour.entity.CustomerOrderRel;
import com.chinatour.entity.GroupLine;
import com.chinatour.entity.Order;
import com.chinatour.entity.OrderAgencyRemark;
import com.chinatour.entity.OrderFeeItems;
import com.chinatour.entity.OrderReceiveItem;
import com.chinatour.entity.OrderRemark;
import com.chinatour.entity.OrdersTotal;
import com.chinatour.entity.PayCostRecords;
import com.chinatour.entity.ReceivableInfoOfOrder;
import com.chinatour.entity.Tour;
import com.chinatour.entity.TourInfoForOrder;
import com.chinatour.entity.TourType;
import com.chinatour.persistence.CustomerFlightMapper;
import com.chinatour.persistence.CustomerMapper;
import com.chinatour.persistence.CustomerOrderRelMapper;
import com.chinatour.persistence.DeptMapper;
import com.chinatour.persistence.GroupLineMapper;
import com.chinatour.persistence.InvoiceAndCreditItemsMapper;
import com.chinatour.persistence.InvoiceAndCreditMapper;
import com.chinatour.persistence.LanguageMapper;
import com.chinatour.persistence.OrderAgencyRemarkMapper;
import com.chinatour.persistence.OrderFeeItemsMapper;
import com.chinatour.persistence.OrderMapper;
import com.chinatour.persistence.OrderRemarkMapper;
import com.chinatour.persistence.OrdersTotalMapper;
import com.chinatour.persistence.PayCostRecordsMapper;
import com.chinatour.persistence.PrePostHotelMapper;
import com.chinatour.persistence.RateOfCurrencyMapper;
import com.chinatour.persistence.ReceivableInfoOfOrderMapper;
import com.chinatour.persistence.SOrderReceiveItemMapper;
import com.chinatour.persistence.TOrderReceiveItemMapper;
import com.chinatour.persistence.TourInfoForOrderMapper;
import com.chinatour.persistence.TourMapper;
import com.chinatour.persistence.TourTypeMapper;
import com.chinatour.persistence.VenderMapper;
import com.chinatour.service.AdminService;
import com.chinatour.service.OrderService;
import com.chinatour.service.VenderService;
import com.chinatour.util.UUIDGenerator;
import com.chinatour.vo.PayCostEditVO;
import com.chinatour.vo.ProductVO;
import com.chinatour.vo.SingleOrdersVO;
import com.chinatour.vo.TourOrderListVO;

/**
 * Service 订单信息
 * 
 * @copyright Copyright: 2014
 * @author Pis
 * @create-time 2014-9-4 上午11:00:28
 * @revision 3.0
 */
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, String> implements OrderService {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private VenderService venderService;

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerOrderRelMapper customerOrderRelMapper;

	@Autowired
	private TourInfoForOrderMapper tourInfoForOrderMapper;

	@Autowired
	private ReceivableInfoOfOrderMapper receivableInfoOfOrderMapper;

	@Autowired
	private SOrderReceiveItemMapper sOrderReceiveItemMapper;

	@Autowired
	private TOrderReceiveItemMapper tOrderReceiveItemMapper;

	@Autowired
	private PayCostRecordsMapper payCostRecordsMapper;

	@Autowired
	private OrdersTotalMapper ordersTotalMapper;
	
	@Autowired
	private LanguageMapper languageMapper;
	
	@Autowired
	private TourMapper tourMapper;
	
	@Autowired
	private TourTypeMapper tourTypeMapper;
	
	@Autowired
	private VenderMapper venderMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private GroupLineMapper groupLineMapper;
	
	@Autowired
	private CustomerFlightMapper customerFlightMapper;
	
	@Autowired
	private InvoiceAndCreditMapper invoiceAndCreditMapper;
	
	@Autowired
	private RateOfCurrencyMapper rateOfCurrencyMapper;
	
	@Autowired
	private InvoiceAndCreditItemsMapper invoiceAndCreditItemsMapper;
	
	@Autowired
	private OrderRemarkMapper orderRemarkMapper;
	
	@Autowired
	private PrePostHotelMapper prePostHotelMapper;
	
	@Autowired
	private OrderAgencyRemarkMapper orderAgencyRemarkMapper;
	
	@Autowired
	private OrderFeeItemsMapper orderFeeItemsMapper;
	
	@Autowired
	public void setBaseMapper(OrderMapper orderMapper) {
		super.setBaseMapper(orderMapper);
	}

	/**
	 * 添加产品
	 * 
	 * @param productVO
	 */
	@Override
	@Transactional
	public void saveProduct(ProductVO productVO){
	}
	
	/**
	 * 添加产品
	 * 
	 * @param productVO
	 */
	@Override
	@Transactional
	public void saveProductNew(ProductVO productVO){
		
	}
	
	/**
	 * 根据子订单ID加载产品
	 */
	@Transactional
	@Override
	public ProductVO loadProduct(String ordersId){
		ProductVO product = new ProductVO();
		
		return product;
	}
	
	/**
	 * 更新团订单产品
	 */
	@Transactional
	@Override
	public void updateProduct(ProductVO productVO,String[] deleteItemIds){
		if(deleteItemIds != null && deleteItemIds.length != 0){
			tOrderReceiveItemMapper.removeByIds(deleteItemIds);
		}
		Order order = new Order();
		TourInfoForOrder tourInfoForOrder  = productVO.getTourInfoForOrder();
		ReceivableInfoOfOrder receivableInfoOfOrder = productVO.getReceivableInfoOfOrder();
		OrderRemark orderRemark = productVO.getOrderRemark();
		OrderAgencyRemark orderAgencyRemark=productVO.getOrderAgencyRemark();//同行订单修改信息
		List<OrderReceiveItem> orderReceiveItemList = new ArrayList<OrderReceiveItem>();
		TourType originalTourType = tourTypeMapper.findById(orderMapper.findById(productVO.getOrdersTotalId()).getTourTypeId());
		TourType changedTourType = tourTypeMapper.findById(productVO.getOrder().getTourTypeId());
		if(productVO.getNewState() == null){
			productVO.setNewState(0);
		}
		Order o = new Order();
		o=orderMapper.findById(productVO.getOrdersTotalId());
		if(productVO.getNewState()==1){
			Tour tour=new Tour();
			tour=tourMapper.findById(o.getTourId());
			if(tour==null){
				
			}else{
				tour.setNewState(1);//团通知OP
				tourMapper.update(tour);
				order.setNoticeState(1);//订单通知OP
			}
		}else{
			order.setNoticeState(2);//不通知
		}
		
		/*
		 * 更新子订单信息
		 */
		order.setId(productVO.getOrdersTotalId());
		order.setCommonTourFee(productVO.getReceivableInfoOfOrder().getSumFee());//保存共计应收团款
		order.setTourTypeId(productVO.getOrder().getTourTypeId());
		order.setModifyDate(new Date());
		order.setPlanticket(productVO.getOrder().getPlanticket());//agent国内机票
		order.setOtherInfo(productVO.getOrder().getOtherInfo());//备注信息
		order.setRate(productVO.getOrder().getRate());
		order.setPeerUserFee(productVO.getOrder().getPeerUserFee());
		if(o.getReviewState()==5&&orderAgencyRemark.getUpdateRemark()!=null){//如果之前通过审核，再次审核状态
			order.setReviewState(6);
		}else{
			order.setReviewState(productVO.getOrder().getReviewState());
		}
		if(productVO.getWarnState() == null){
			productVO.setWarnState(0);
		}
		if(productVO.getWarnState()==2){
			order.setWarnState(2);//通知财务
		}
		/*if(order.getReviewState()==2){
			order.setWarnState(2);//通知财务
		}*/
		if(productVO.getTourInfoForOrder().getGroupLineId() != null){
			switch(changedTourType.getType()){
				case 0: order.setOrderTourType(Order.BRAND_SELF); break;
				case 1: order.setOrderTourType(Order.BRAND_SYS); break;
				case 3: order.setOrderTourType(Order.INBOUND_SYS); break;
				case 4: order.setOrderTourType(Order.INBOUND_SELF);
			}
		}
		order.setIsEdit(1);
		orderMapper.update(order);
		
		if(productVO.getTourInfoForOrder().getGroupLineId() != null){
		//原本订单不是单订订单
			if(originalTourType.getTourTypeId().equals(productVO.getOrder().getTourTypeId())){
			/* 团队类型没有发生改变时  */	
				if(originalTourType.getType() == 0 || originalTourType.getType() == 4){
				/* 原本是自录入团，更新产品信息  */
					productVO.getGroupLine().setDepartureDate(dateFormat(productVO.getTourInfoForOrder().getDepartureDate()));
					groupLineMapper.updateIndependentGroupLine(productVO.getGroupLine());
					tourInfoForOrder.setScheduleLineCode(productVO.getGroupLine().getTourCode());
					tourInfoForOrder.setLineName(productVO.getGroupLine().getTourName());
				}else{
				/* 原本是系统录入团，更改产品 */
					GroupLine groupLine = groupLineMapper.findById(productVO.getTourInfoForOrder().getGroupLineId());
					tourInfoForOrder.setScheduleLineCode(groupLine.getTourCode());
					tourInfoForOrder.setLineName(groupLine.getTourName());
				}
			}else if(changedTourType.getType() == 0 || changedTourType.getType() == 4){
			/* 转为自录入团 */
				productVO.getGroupLine().setId(UUIDGenerator.getUUID());
				productVO.getGroupLine().setBrand(productVO.getOrder().getBrand());
				productVO.getGroupLine().setTourTypeId(productVO.getOrder().getTourTypeId());
				productVO.getGroupLine().setDepartureDate(dateFormat(productVO.getTourInfoForOrder().getDepartureDate()));
				groupLineMapper.save(productVO.getGroupLine());
				tourInfoForOrder.setGroupLineId(productVO.getGroupLine().getId());
				tourInfoForOrder.setScheduleLineCode(productVO.getGroupLine().getTourCode());
				tourInfoForOrder.setLineName(productVO.getGroupLine().getTourName());
			}else{
			/* 转为系统录入团 */	
				GroupLine groupLine = groupLineMapper.findById(productVO.getTourInfoForOrder().getGroupLineId());
				tourInfoForOrder.setScheduleLineCode(groupLine.getTourCode());
				tourInfoForOrder.setLineName(groupLine.getTourName());
			}
		}
																
		/*
		 * 更新订单信息
		 */
		tourInfoForOrderMapper.update(tourInfoForOrder);
		
		/*
		 * 更新航班信息
		 */
		if(productVO!=null&&productVO.getCustomerFlights()!=null){
			for(CustomerOrderRel customerOrderRel : productVO.getCustomerFlights()){
				customerOrderRelMapper.update(customerOrderRel);
				for(CustomerFlight customerFlight : customerOrderRel.getCustomerFlightList()){
					if(customerFlight.getId() == null || customerFlight.getId().equals("")){
						customerFlight.setId(UUIDGenerator.getUUID());
						customerFlight.setCustomerOrderRelId(customerOrderRel.getId());
						customerFlightMapper.save(customerFlight);
					}
					customerFlightMapper.updateCustomerFlight(customerFlight);
				}
			}
		}
		
		/*
		 * 保存费用信息
		 */
		receivableInfoOfOrderMapper.update(receivableInfoOfOrder);
		
		orderReceiveItemList.add(productVO.getAdultItem());
		orderReceiveItemList.add(productVO.getChildrenItem());
		addToTotalList(orderReceiveItemList,productVO.getOtherFeeList());
		addToTotalList(orderReceiveItemList,productVO.getDiscountList());
		for(OrderReceiveItem orderReceiveItem : orderReceiveItemList){
			if(orderReceiveItem.getId() == null || orderReceiveItem.getId().length() == 0){
				orderReceiveItem.setId(UUIDGenerator.getUUID());
				orderReceiveItem.setReceivableInfoOfOrderId(receivableInfoOfOrder.getId());
				tOrderReceiveItemMapper.save(orderReceiveItem);
			}else{
				tOrderReceiveItemMapper.update(orderReceiveItem);
			}
		}
		if(productVO.getWarnState() != null){
			orderRemark.setOrderRemarksId(UUIDGenerator.getUUID());
			orderRemark.setModifyDate(new Date());
			orderRemark.setOrderId(order.getId());
			orderRemark.setUserId(adminService.getCurrent().getId());
			orderRemark.setUserName(adminService.getCurrent().getUsername());
			orderRemarkMapper.save(orderRemark);
		}
		if(orderAgencyRemark.getUpdateRemark()!=null){
			orderAgencyRemark.setOrderAgencyRemarksId(UUIDGenerator.getUUID());
			orderAgencyRemark.setModifyDate(new Date());
			orderAgencyRemark.setOrderId(order.getId());
			orderAgencyRemark.setUserId(adminService.getCurrent().getId());
			orderAgencyRemark.setUserName(adminService.getCurrent().getUsername());
			orderAgencyRemarkMapper.save(orderAgencyRemark);
		}
		if(productVO.getOrderFeeItems()!=null){
			List<OrderFeeItems> orderFeeItemsList=productVO.getOrderFeeItems();
			for(OrderFeeItems orderFeeItems:orderFeeItemsList){
				if(orderFeeItems.getFeeTitle()!=null && orderFeeItems.getId()!=null){
				orderFeeItemsMapper.update(orderFeeItems);
				}else if(orderFeeItems.getFeeTitle()!=null && orderFeeItems.getId()==null){
					orderFeeItems.setId(UUIDGenerator.getUUID());
					orderFeeItems.setOrderId(order.getId());
					orderFeeItemsMapper.save(orderFeeItems);
				}else{
				}
			}
		}
	}

	
	/**
	 * 更新团订单产品
	 */
	@Transactional
	@Override
	public void updateProductNew(ProductVO productVO,String[] deleteItemIds){
		if(deleteItemIds != null && deleteItemIds.length != 0){
			tOrderReceiveItemMapper.removeByIds(deleteItemIds);
		}
		Order order = new Order();
		TourInfoForOrder tourInfoForOrder  = productVO.getTourInfoForOrder();
		ReceivableInfoOfOrder receivableInfoOfOrder = productVO.getReceivableInfoOfOrder();
		OrderRemark orderRemark = productVO.getOrderRemark();
		OrderAgencyRemark orderAgencyRemark=productVO.getOrderAgencyRemark();//同行订单修改信息
		List<OrderReceiveItem> orderReceiveItemList = new ArrayList<OrderReceiveItem>();
		TourType changedTourType =new TourType();
		TourType originalTourType = tourTypeMapper.findById(orderMapper.findById(productVO.getOrdersTotalId()).getTourTypeId());
		if(productVO.getOrder().getTourTypeId()!=null){
			changedTourType = tourTypeMapper.findById(productVO.getOrder().getTourTypeId());
		}
		if(productVO.getNewState() == null){
			productVO.setNewState(0);
		}
		Order o = new Order();
		o=orderMapper.findById(productVO.getOrdersTotalId());
		if(productVO.getNewState()==1){
			Tour tour=new Tour();
			tour=tourMapper.findById(o.getTourId());
			if(tour==null){
				
			}else{
				tour.setNewState(1);//团通知OP
				tourMapper.update(tour);
				order.setNoticeState(1);//订单通知OP
			}
		}else{
			order.setNoticeState(2);//不通知
		}
		
		/*
		 * 更新子订单信息
		 */
		order.setId(productVO.getOrdersTotalId());
		order.setCommonTourFee(productVO.getReceivableInfoOfOrder().getSumFee());//保存共计应收团款
		order.setTourTypeId(productVO.getOrder().getTourTypeId());
		order.setModifyDate(new Date());
		order.setPlanticket(productVO.getOrder().getPlanticket());//agent国内机票
		order.setOtherInfo(productVO.getOrder().getOtherInfo());//备注信息
		order.setRate(productVO.getOrder().getRate());
		order.setPeerUserFee(productVO.getOrder().getPeerUserFee());
		if(o.getReviewState()==5&&orderAgencyRemark.getUpdateRemark()!=null){//如果之前通过审核，再次审核状态
			order.setReviewState(6);
		}else{
			order.setReviewState(productVO.getOrder().getReviewState());
		}
		if(productVO.getWarnState() == null){
			productVO.setWarnState(0);
		}
		if(productVO.getWarnState()==2){
			order.setWarnState(2);//通知财务
		}
		/*if(order.getReviewState()==2){
			order.setWarnState(2);//通知财务
		}*/
		if(productVO.getTourInfoForOrder().getGroupLineId() != null){
			if(changedTourType!=null&&changedTourType.getType()!=null){
				switch(changedTourType.getType()){
					case 0: order.setOrderTourType(Order.BRAND_SELF); break;
					case 1: order.setOrderTourType(Order.BRAND_SYS); break;
					case 3: order.setOrderTourType(Order.INBOUND_SYS); break;
					case 4: order.setOrderTourType(Order.INBOUND_SELF);
				}
			}
		}
		order.setIsEdit(1);
		orderMapper.update(order);
		
		if(productVO.getTourInfoForOrder().getGroupLineId() != null){
		//原本订单不是单订订单
			if(productVO.getOrder().getTourTypeId()!=null){
				if(originalTourType.getTourTypeId().equals(productVO.getOrder().getTourTypeId())){
				/* 团队类型没有发生改变时  */	
					if(originalTourType.getType() == 0 || originalTourType.getType() == 4){
					/* 原本是自录入团，更新产品信息  */
						productVO.getGroupLine().setDepartureDate(dateFormat(productVO.getTourInfoForOrder().getDepartureDate()));
						groupLineMapper.updateIndependentGroupLine(productVO.getGroupLine());
						tourInfoForOrder.setScheduleLineCode(productVO.getGroupLine().getTourCode());
						tourInfoForOrder.setLineName(productVO.getGroupLine().getTourName());
					}else{
					/* 原本是系统录入团，更改产品 */
						GroupLine groupLine = groupLineMapper.findById(productVO.getTourInfoForOrder().getGroupLineId());
						tourInfoForOrder.setScheduleLineCode(groupLine.getTourCode());
						tourInfoForOrder.setLineName(groupLine.getTourName());
					}
				}else if(changedTourType.getType() == 0 || changedTourType.getType() == 4){
				/* 转为自录入团 */
					productVO.getGroupLine().setId(UUIDGenerator.getUUID());
					productVO.getGroupLine().setBrand(productVO.getOrder().getBrand());
					productVO.getGroupLine().setTourTypeId(productVO.getOrder().getTourTypeId());
					productVO.getGroupLine().setDepartureDate(dateFormat(productVO.getTourInfoForOrder().getDepartureDate()));
					groupLineMapper.save(productVO.getGroupLine());
					tourInfoForOrder.setGroupLineId(productVO.getGroupLine().getId());
					tourInfoForOrder.setScheduleLineCode(productVO.getGroupLine().getTourCode());
					tourInfoForOrder.setLineName(productVO.getGroupLine().getTourName());
				}else{
				/* 转为系统录入团 */	
					GroupLine groupLine = groupLineMapper.findById(productVO.getTourInfoForOrder().getGroupLineId());
					tourInfoForOrder.setScheduleLineCode(groupLine.getTourCode());
					tourInfoForOrder.setLineName(groupLine.getTourName());
				}
			}
		}
																
		/*
		 * 更新订单信息
		 */
		tourInfoForOrderMapper.update(tourInfoForOrder);
		
		/*
		 * 更新航班信息
		 */
		if(productVO!=null&&productVO.getCustomerFlights()!=null){
			for(CustomerOrderRel customerOrderRel : productVO.getCustomerFlights()){
				customerOrderRelMapper.update(customerOrderRel);
				for(CustomerFlight customerFlight : customerOrderRel.getCustomerFlightList()){
					if(customerFlight.getId() == null || customerFlight.getId().equals("")){
						customerFlight.setId(UUIDGenerator.getUUID());
						customerFlight.setCustomerOrderRelId(customerOrderRel.getId());
						customerFlightMapper.save(customerFlight);
					}
					customerFlightMapper.updateCustomerFlight(customerFlight);
				}
			}
		}
		
		/*
		 * 保存费用信息
		 */
		receivableInfoOfOrderMapper.update(receivableInfoOfOrder);
		
		orderReceiveItemList.add(productVO.getAdultItem());
		orderReceiveItemList.add(productVO.getChildrenItem());
		addToTotalList(orderReceiveItemList,productVO.getOtherFeeList());
		addToTotalList(orderReceiveItemList,productVO.getDiscountList());
		for(OrderReceiveItem orderReceiveItem : orderReceiveItemList){
			if(orderReceiveItem.getId() == null || orderReceiveItem.getId().length() == 0){
				orderReceiveItem.setId(UUIDGenerator.getUUID());
				orderReceiveItem.setReceivableInfoOfOrderId(receivableInfoOfOrder.getId());
				tOrderReceiveItemMapper.save(orderReceiveItem);
			}else{
				tOrderReceiveItemMapper.update(orderReceiveItem);
			}
		}
		if(productVO.getWarnState() != null){
			orderRemark.setOrderRemarksId(UUIDGenerator.getUUID());
			orderRemark.setModifyDate(new Date());
			orderRemark.setOrderId(order.getId());
			orderRemark.setUserId(adminService.getCurrent().getId());
			orderRemark.setUserName(adminService.getCurrent().getUsername());
			orderRemarkMapper.save(orderRemark);
		}
		if(orderAgencyRemark.getUpdateRemark()!=null){
			orderAgencyRemark.setOrderAgencyRemarksId(UUIDGenerator.getUUID());
			orderAgencyRemark.setModifyDate(new Date());
			orderAgencyRemark.setOrderId(order.getId());
			orderAgencyRemark.setUserId(adminService.getCurrent().getId());
			orderAgencyRemark.setUserName(adminService.getCurrent().getUsername());
			orderAgencyRemarkMapper.save(orderAgencyRemark);
		}
		if(productVO.getOrderFeeItems()!=null){
			List<OrderFeeItems> orderFeeItemsList=productVO.getOrderFeeItems();
			for(OrderFeeItems orderFeeItems:orderFeeItemsList){
				if(orderFeeItems.getFeeTitle()!=null && orderFeeItems.getId()!=null){
				orderFeeItemsMapper.update(orderFeeItems);
				}else if(orderFeeItems.getFeeTitle()!=null && orderFeeItems.getId()==null){
					orderFeeItems.setId(UUIDGenerator.getUUID());
					orderFeeItems.setOrderId(order.getId());
					orderFeeItemsMapper.save(orderFeeItems);
				}else{
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinatour.service.OrderService#findTourOrderListVO(com.chinatour.
	 * entity.Order, com.chinatour.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<TourOrderListVO> findTourOrderListVO(Order or, Pageable pageable) {
		List<TourOrderListVO> tourOrderListVOList = new ArrayList<TourOrderListVO>();
		if (pageable == null) {
			pageable = new Pageable();
		}
		int pageCount = orderMapper.findTourOrderListVOForPageCount(or, pageable);
		tourOrderListVOList = orderMapper.findTourOrderListVOForPage(or, pageable);
		return new Page<TourOrderListVO>(tourOrderListVOList, pageCount, pageable);
	}	
	/**
	 * 保存非团订单
	 */
	@Override
	@Transactional
	public void saveSingleProduct(SingleOrdersVO singleOrdersVO){
		
		OrdersTotal ordersTotal = ordersTotalMapper.findById(singleOrdersVO.getOrdersTotalId());
		Order order = singleOrdersVO.getOrder();
		TourInfoForOrder tourInfoForOrder = singleOrdersVO.getTourInfoForOrder();
		ReceivableInfoOfOrder receivableInfoOfOrder = singleOrdersVO.getReceivableInfoOfOrder();
		List<OrderReceiveItem> orderReceiveItemList = new ArrayList<OrderReceiveItem>();
		
		/* 根据总订单ID查找出同产品的一组CustomerOrderRel */
		List<CustomerOrderRel> customerOrderRelList = customerOrderRelMapper.findBySameProductOrdersTotalId(singleOrdersVO.getOrdersTotalId());
		
		/*
		 * 保存一条子订单
		 */
		if(order == null){
			order = new Order();
		}
		order.setId(UUIDGenerator.getUUID());
		order.setOrdersTotalId(ordersTotal.getOrdersTotalId());
		order.setTotalPeople(ordersTotal.getTotalPeople());
		order.setCommonTourFee(singleOrdersVO.getReceivableInfoOfOrder().getSumFee());//保存共计应收团款
		order.setPeerId(ordersTotal.getCompanyId());
		order.setContact(ordersTotal.getContactName());
		order.setIsSelfOrganize(1);
		order.setOrderType(5);
		order.setModifyDate(new Date());
		order.setUserId(adminService.getCurrent().getId());
		order.setUserName(adminService.getCurrent().getUsername());
		order.setDeptId(adminService.getCurrent().getDeptId());
		order.setOrderNo(ordersTotal.getOrderNumber());
		orderMapper.save(order);
		
		/*
		 * 保存订单信息
		 */
		if(tourInfoForOrder == null){
			tourInfoForOrder = new TourInfoForOrder();
		}
	    tourInfoForOrder.setId(UUIDGenerator.getUUID());
	    tourInfoForOrder.setOrderId(order.getId());
		tourInfoForOrderMapper.save(tourInfoForOrder);
		
		/*
		 * 保存费用信息
		 */
		receivableInfoOfOrder.setId(UUIDGenerator.getUUID());
		receivableInfoOfOrder.setTotalPeople(ordersTotal.getTotalPeople());
		receivableInfoOfOrder.setOrderId(order.getId());
		receivableInfoOfOrderMapper.save(receivableInfoOfOrder);
		
		addToTotalList(orderReceiveItemList,singleOrdersVO.getVisaFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getFlightTicketFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getHotelFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getTicketFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getInsuranceFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getOtherFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getBusTourFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getCruiseFeeList());
		
		for(OrderReceiveItem orderReceiveItem : orderReceiveItemList){
			orderReceiveItem.setId(UUIDGenerator.getUUID());
			orderReceiveItem.setReceivableInfoOfOrderId(receivableInfoOfOrder.getId());
			sOrderReceiveItemMapper.save(orderReceiveItem);
		}
		
		/*
		 * 将产品添加至总订单
		 */
		if(customerOrderRelList.get(0).getOrderId().length() == 0){
			/* 如果是给总订单第一次添加产品 ,直接更新orderId字段   */
			String[] customerOrderRelIds = new String[customerOrderRelList.size()];
			for(int i=0; i<customerOrderRelList.size(); i++){
				customerOrderRelIds[i] = customerOrderRelList.get(i).getId();
			}
			customerOrderRelMapper.setOrderIdByIds(order.getId(), customerOrderRelIds);
		}else{
			// order的state为0，2，3，7
			int existOrderNumber = orderMapper.countExistOrdersInOrdersTotal(singleOrdersVO.getOrdersTotalId());
			//如果订单已全部取消时,新增订单则恢复总订单客人人数，更改总订单客人状态
			if(existOrderNumber == 1){
				int totalPeople = customerOrderRelMapper.countCustomersInOrdersTotal(singleOrdersVO.getOrdersTotalId());
				ordersTotalMapper.setTotalPeopleByOrdersTotalId(totalPeople, singleOrdersVO.getOrdersTotalId());
				orderMapper.setTotalPeopleByOrderId(totalPeople, order.getId());
				//将已存在的订单中客人的总订单状态置为正常
				customerOrderRelMapper.setContactFlagByCusIdAndOrToId(0, null, singleOrdersVO.getOrdersTotalId());
			}
			
			/* 如果是给总订单再次添加产品 ,设入一组ID和OrderId保存  */
			for(CustomerOrderRel customerOrderRel : customerOrderRelList){
				customerOrderRel.setId(UUIDGenerator.getUUID());
				customerOrderRel.setCustomerTourNo(0);  //取消原子订单中客人团编号
				customerOrderRel.setOrderId(order.getId());
				//如果订单已全部取消时，设置客人总订单子订单皆为正常
				if(existOrderNumber == 1){
					customerOrderRel.setIsDel(0); 
					customerOrderRel.setContactFlag(0);
				}else{
					customerOrderRel.setIsDel(customerOrderRel.getContactFlag()); //客人在本订单中状态与客人总订单状态保持一致
				}
			}
			customerOrderRelMapper.saveCustomerOrderRels(customerOrderRelList);
		}
	}			
	
	/**
	 * 加载非团订单
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public SingleOrdersVO loadSingleProduct(String ordersId){
		SingleOrdersVO singleOrdersVO = new SingleOrdersVO();
		Order order = orderMapper.findById(ordersId);
		singleOrdersVO.setOrder(order);
		singleOrdersVO.setOrdersTotalId(ordersTotalMapper.findById(order.getOrdersTotalId()).getOrderNumber());
		singleOrdersVO.setTourInfoForOrder(tourInfoForOrderMapper.findByOrderId(ordersId));
		singleOrdersVO.setReceivableInfoOfOrder(receivableInfoOfOrderMapper.findByOrderId(ordersId));
		
		List<OrderReceiveItem> orderReceiveItemList = sOrderReceiveItemMapper.findByReceivableInfoOfOrderId(singleOrdersVO.getReceivableInfoOfOrder().getId());
		
		List<OrderReceiveItem> visaFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> flightTicketFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> hotelFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> ticketFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> insuranceFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> otherFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> busTourFeeList = new ArrayList<OrderReceiveItem>();
		List<OrderReceiveItem> cruiseFeeList = new ArrayList<OrderReceiveItem>();
		List<PayCostRecords> payRecords = new ArrayList<PayCostRecords>();
		List<PayCostRecords> costRecords = new ArrayList<PayCostRecords>();
		
		Set<String> nonGroupTypeSet = new HashSet<String>();
		
		for(OrderReceiveItem orderReceiveItem : orderReceiveItemList){
			if(orderReceiveItem.getNum() >= 100 && orderReceiveItem.getNum() < 200){
				visaFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("1");
			}else if(orderReceiveItem.getNum() >= 200 && orderReceiveItem.getNum() < 300){
				flightTicketFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("2");
			}else if(orderReceiveItem.getNum() >= 300 && orderReceiveItem.getNum() < 400){
				hotelFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("3");
			}else if(orderReceiveItem.getNum() >= 400 && orderReceiveItem.getNum() < 500){
				ticketFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("4");
			}else if(orderReceiveItem.getNum() >= 500 && orderReceiveItem.getNum() < 600){
				insuranceFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("5");
			}else if(orderReceiveItem.getNum() >= 600 && orderReceiveItem.getNum() < 700){
				busTourFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("6");
			}else if(orderReceiveItem.getNum() >= 700 && orderReceiveItem.getNum() < 800){
				cruiseFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("7");	
			}else{
				otherFeeList.add(orderReceiveItem);
				nonGroupTypeSet.add("8");	
			}
		}
		List<PayCostRecords> payCostRecordsList = payCostRecordsMapper.findByOrderId(ordersId);
		for(PayCostRecords payCostRecords : payCostRecordsList){
			if(payCostRecords.getPayOrCost() == 1){
				payRecords.add(payCostRecords);
			}else{
				costRecords.add(payCostRecords);
			}
		}
		
		singleOrdersVO.setVisaFeeList(visaFeeList);
		singleOrdersVO.setFlightTicketFeeList(flightTicketFeeList);
		singleOrdersVO.setHotelFeeList(hotelFeeList);
		singleOrdersVO.setTicketFeeList(ticketFeeList);
		singleOrdersVO.setInsuranceFeeList(insuranceFeeList);
		singleOrdersVO.setOtherFeeList(otherFeeList);
		singleOrdersVO.setBusTourFeeList(busTourFeeList);
		singleOrdersVO.setCruiseFeeList(cruiseFeeList);
		singleOrdersVO.setPayRecordsList(payRecords);
		singleOrdersVO.setCostRecordsList(costRecords);
		singleOrdersVO.setNonGroupTypeSet(nonGroupTypeSet);
		return singleOrdersVO;
	}
	
	/**
	 * 修改非团订单
	 */
	@Override
	@Transactional
	public void updateSingleProduct(SingleOrdersVO singleOrdersVO){
		singleOrdersVO.getOrder().setCommonTourFee(singleOrdersVO.getReceivableInfoOfOrder().getSumFee());//保存共计应收团款
		Order order = singleOrdersVO.getOrder();
		order.setIsEdit(1);
		orderMapper.updateFlightPnrAndArriveDate(order);
		tourInfoForOrderMapper.updateDepartureDateAndDayNum(singleOrdersVO.getTourInfoForOrder());
		
		receivableInfoOfOrderMapper.update(singleOrdersVO.getReceivableInfoOfOrder());
		sOrderReceiveItemMapper.removeByReceivableInfoOfOrderId(singleOrdersVO.getReceivableInfoOfOrder().getId());
		List<OrderReceiveItem> orderReceiveItemList = new ArrayList<OrderReceiveItem>();
		
		addToTotalList(orderReceiveItemList,singleOrdersVO.getVisaFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getFlightTicketFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getHotelFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getTicketFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getInsuranceFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getOtherFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getBusTourFeeList());
		addToTotalList(orderReceiveItemList,singleOrdersVO.getCruiseFeeList());
		for(OrderReceiveItem orderReceiveItem : orderReceiveItemList){
			orderReceiveItem.setId(UUIDGenerator.getUUID());
			orderReceiveItem.setReceivableInfoOfOrderId(singleOrdersVO.getReceivableInfoOfOrder().getId());
			sOrderReceiveItemMapper.save(orderReceiveItem);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinatour.service.OrderService#findForGrouPage(com.chinatour.entity
	 * .Order, com.chinatour.Pageable)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<Order> findForGrouPage(Order order, Pageable pageable) {
		if (pageable == null) {
			pageable = new Pageable();
		}
		List<Order> orderList = orderMapper.findForGroupPage(order, pageable);
		int pageCount = orderMapper.findForGroupPageCount(order, pageable);
		return new Page<Order>(orderList, pageCount, pageable);
	}

	/*
	 * (non-Javadoc)
	 * @see com.chinatour.service.OrderService#findPayCostByOrderId(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public PayCostEditVO findPayCostByOrderId(String orderId) {
		PayCostEditVO payCostEditVO = new PayCostEditVO();
		BigDecimal sumPay = new BigDecimal(0.0);
		BigDecimal sumCost = new BigDecimal(0.0);
		List<PayCostRecords> payRecordsList = new ArrayList<PayCostRecords>();
		List<PayCostRecords> costRecordsList = new ArrayList<PayCostRecords>();
		for(PayCostRecords payCostRecords : payCostRecordsMapper.findByOrderId(orderId)){
			if(payCostRecords.getPayOrCost() == 1){
				payRecordsList.add(payCostRecords);
				sumPay = sumPay.add(payCostRecords.getSum());
			} else {
				costRecordsList.add(payCostRecords);
				sumCost = sumCost.add(payCostRecords.getSum());
			}
		}
		payCostEditVO.setOrderId(orderId);
		payCostEditVO.setSumPay(sumPay);
		payCostEditVO.setSumCost(sumCost);
		payCostEditVO.setPayRecordsList(payRecordsList);
		payCostEditVO.setCostRecordsList(costRecordsList);
		return payCostEditVO;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.chinatour.service.OrderService#updateTourOrderPayCost(com.chinatour.vo.TourOrderPayCostVO)
	 */
	@Override
	@Transactional
	public void updatePayCost(PayCostEditVO payCostEditVO) {
		List<PayCostRecords> payCostRecordsList = new ArrayList<PayCostRecords>();
		if(payCostEditVO.getPayRecordsList()!=null){
			payCostRecordsList.addAll(payCostEditVO.getPayRecordsList());
		}
		if(payCostEditVO.getCostRecordsList()!=null){
			payCostRecordsList.addAll(payCostEditVO.getCostRecordsList());
		}
		for (PayCostRecords payCostRecords : payCostRecordsList) {
			if(payCostRecords.getId() == null || payCostRecords.getId().length() == 0){
				payCostRecords.setId(UUIDGenerator.getUUID());
				payCostRecords.setOrderId(payCostEditVO.getOrderId());
				payCostRecordsMapper.save(payCostRecords);
			}
			payCostRecordsMapper.update(payCostRecords);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.chinatour.service.OrderService#findCustomerByOrderId(java.lang.String)
	 */
	@Override
	@Transactional
	public Page<Customer> findCustomerPagesByOrderId(Pageable pageable,String orderId) {
		List<Customer> customerList = new ArrayList<Customer>();
		CustomerOrderRel cusOrderRel = new CustomerOrderRel();
		Customer customer = null;
		cusOrderRel.setOrderId(orderId);
        if (pageable == null) {
            pageable = new Pageable();
        }
        
		int total = customerOrderRelMapper.findForPageCount(cusOrderRel);
		for (CustomerOrderRel customerOrderRel : customerOrderRelMapper.findForPage(cusOrderRel, pageable)) {
			customer = customerMapper.findById(customerOrderRel.getCustomerId());
			customer.setLanguage(languageMapper.findById(customer.getLanguageId()));
			customer.setCustomerTourNo(customerOrderRel.getCustomerTourNo());
			customer.setIsDel(customerOrderRel.getIsDel());
			customerList.add(customer);
		}
		return new Page<Customer>(customerList,total,pageable);
	}
	

	/*
	 * 已组团订单
	 * 订单取消中 设置客人取消中
	 */
	public void setCancelingCustomer(Order order){
		List<CustomerOrderRel> customerOrderRelList =customerOrderRelMapper.findByOrderId(order.getId());
		for(int i=0;i<customerOrderRelList.size();i++){
			CustomerOrderRel customerOrderRel=customerOrderRelList.get(i);
			//将该客人置为取消中
			customerOrderRelMapper.changeCustomerState(customerOrderRel.getId(), 5); 
		}
		
	}
	

	/*
	 * 
	 * 订单恢复中 设置客人恢复中
	 */
	public void setRecoveringCustomer(Order order){
		//修改客人在总账单下的状态为可用
		customerOrderRelMapper.setContactFlagByCusIdAndOrToId(0, null, order.getOrdersTotalId());
		List<CustomerOrderRel> customerOrderRelList =customerOrderRelMapper.findByOrderId(order.getId());
		for(int i=0;i<customerOrderRelList.size();i++){
			CustomerOrderRel customerOrderRel=customerOrderRelList.get(i);
			//将该客人置为恢复中
			customerOrderRelMapper.changeCustomerState(customerOrderRel.getId(), 3); 
		}
		
	}
	
	
	
	@Override
	public List<Order> find(Order order) {
		return orderMapper.find(order);
	}

	@Override
	public List<Order> findUserOfOrder(Order order) {
		return orderMapper.findUserOfOrder(order);
	}
	
	/*
	 * 给matrix里添加add
	 */
	private void addItem(List matrix,List add){
		if(add != null){
			matrix.addAll(add);
		}
	}

			/*supplierPriceRemark.setSprCheck(5);   //设置为已结算状态
			supplierPriceRemarkMapper.update(supplierPriceRemark);
			Admin agent = adminService.getCurrent();
			Dept dept = deptMapper.findById(agent.getDeptId());
			
			Tour tour = tourMapper.findById(supplierPriceRemark.getTourId());
			Admin op = adminService.findById(tour.getUserId());
			Dept toDept = deptMapper.findById(op.getDeptId());
			//获取汇率
			RateOfCurrency rateOfCurrency = new RateOfCurrency();
			rateOfCurrency.setCurrencyId(deptMapper.findById(agent.getDeptId()).getCurrencyTypeId());
			rateOfCurrency.setToCurrencyId(deptMapper.findById(op.getDeptId()).getCurrencyTypeId());
			rateOfCurrency.setIsAvailable(0);
			rateOfCurrency = rateOfCurrencyMapper.getRate(rateOfCurrency);
		    	if(dept.getDeptId()!=toDept.getDeptId()){
					InvoiceAndCredit invoiceAndCreditTemp = new InvoiceAndCredit();
					invoiceAndCreditTemp.setDeptId(agent.getDeptId());
					Integer businessNo =invoiceAndCreditMapper.getBusinessNo(agent.getDeptId());
					
					InvoiceAndCredit invoiceAndCredit = new InvoiceAndCredit();
					invoiceAndCredit.setDeptId(agent.getDeptId());
					invoiceAndCredit.setPrefix(dept.getDeptName());
					invoiceAndCredit.setBusinessNo(businessNo);
					invoiceAndCredit.setInvoiceAndCreditId(UUIDGenerator.getUUID());
					invoiceAndCredit.setCreateDate(new Date());
					SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
					invoiceAndCredit.setMonth(supplierPriceRemark.getInsertTime());
					invoiceAndCredit.setIfBeginningValue(1);
					invoiceAndCredit.setBillToDeptId(toDept.getDeptId());
					invoiceAndCredit.setBillToReceiver(toDept.getDeptName());
					invoiceAndCredit.setConfirmStatus(Constant.CONFIRMSTATUS[4]);
					invoiceAndCredit.setConfirmRemarks(Constant.AUTOCONFIRMREMARKS);
					invoiceAndCredit.setTourCode(tour.getTourCode());
					invoiceAndCredit.setTourId(tour.getTourId());
					invoiceAndCredit.setRateOfCurrencyId(rateOfCurrency.getId());
					if(supplierPriceRemark.getDifferenceSum().compareTo(new BigDecimal(0))==1){
						invoiceAndCredit.setRecordType(Constant.CREDIT);
					}else {
						invoiceAndCredit.setRecordType(Constant.INVOICE);
					}
					
					BigDecimal currency = supplierPriceRemark.getDifferenceSum().divide(rateOfCurrency.getRateDown()).multiply(rateOfCurrency.getRateUp());
					invoiceAndCredit.setEnterCurrency(currency.multiply(Constant.OP_PROFIT).setScale(2, BigDecimal.ROUND_HALF_UP));//本invoice或credit的输入金额（本位币）
					invoiceAndCredit.setDollar(invoiceAndCredit.getEnterCurrency().divide(rateOfCurrency.getUsRate()));//本invoice或credit的输入金额（美元）
					invoiceAndCredit.setRemarks(simpleDateFormat.format(tour.getArriveDateTime()).substring(0, 7)+Constant.EXCHANGEDPROFITFORMONTH+":"+currency.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"*"+Constant.OP_PROFIT.setScale(2, BigDecimal.ROUND_HALF_UP));
					
					
					InvoiceAndCreditItems invoiceAndCreditItems = new InvoiceAndCreditItems();
					invoiceAndCreditItems.setInvoiceAndCreditId(invoiceAndCredit.getInvoiceAndCreditId());
					invoiceAndCreditItems.setItemsId(UUIDGenerator.getUUID());
					invoiceAndCreditItems.setBillToDeptId(invoiceAndCredit.getBillToDeptId());
					invoiceAndCreditItems.setBusinessNo(invoiceAndCredit.getBusinessNo());
					invoiceAndCreditItems.setDeptId(invoiceAndCredit.getDeptId());
					//invoiceAndCreditItems.setIfBeginningValue(1);
					invoiceAndCreditItems.setIfVerified(2);
					invoiceAndCreditItems.setAmount(supplierPriceRemark.getDifferenceSum().multiply(rateOfCurrency.getRateUp()).multiply(Constant.OP_PROFIT).divide(rateOfCurrency.getRateDown(), 2, BigDecimal.ROUND_HALF_UP)); 
					invoiceAndCreditItems.setDollarAmount(supplierPriceRemark.getDifferenceSum().multiply(rateOfCurrency.getRateUp()).multiply(Constant.OP_PROFIT).divide(rateOfCurrency.getRateDown()).divide(rateOfCurrency.getUsRate(), 2, BigDecimal.ROUND_HALF_UP));
					invoiceAndCreditItems.setRemarks(Constant.EXCHANGEDPROFIT+":"+invoiceAndCreditItems.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"*"+Constant.OP_PROFIT);
					invoiceAndCreditItems.setDescription(agent.getUsername());
							
					invoiceAndCreditMapper.save(invoiceAndCredit);
					invoiceAndCreditItemsMapper.save(invoiceAndCreditItems);
					
					AccountRecord account1=new AccountRecord();			//本部门账
					AccountRecord account2=new AccountRecord();			//对方部门账
					//本部门 部门 记录
					account1.setAccountRecordId(UUIDGenerator.getUUID());
					account1.setInvoiceAndCreditId(invoiceAndCredit.getInvoiceAndCreditId());
					account1.setBusinessNo(invoiceAndCredit.getBusinessNo());
					account1.setDeptId(invoiceAndCredit.getDeptId());
					account1.setBillToDeptId(invoiceAndCredit.getBillToDeptId());
					account1.setBillToReceiver(invoiceAndCredit.getPrefix());
					account1.setCreateDate(invoiceAndCredit.getCreateDate());
					String month=simpleDateFormat.format(invoiceAndCredit.getMonth()).substring(0, 7);
					account1.setMonth(month);
					account1.setRemarks(invoiceAndCredit.getRemarks());
					account1.setTourCode(invoiceAndCredit.getTourCode());
					account1.setIfBeginningValue(invoiceAndCredit.getIfBeginningValue());
					//对方部门 记录
					account2.setAccountRecordId(UUIDGenerator.getUUID());
					account2.setInvoiceAndCreditId(invoiceAndCredit.getInvoiceAndCreditId());
					account2.setBusinessNo(invoiceAndCredit.getBusinessNo());
					account2.setDeptId(invoiceAndCredit.getBillToDeptId());
					account2.setBillToReceiver(invoiceAndCredit.getPrefix());
					account2.setCreateDate(invoiceAndCredit.getCreateDate());
					account2.setMonth(month);
					account2.setRemarks(invoiceAndCredit.getRemarks());
					account2.setTourCode(invoiceAndCredit.getTourCode());
					account2.setIfBeginningValue(invoiceAndCredit.getIfBeginningValue());
					account2.setBillToDeptId(invoiceAndCredit.getDeptId());
					
					BigDecimal exDollar = invoiceAndCredit.getEnterCurrency()
							.multiply(rateOfCurrency.getRateUp())
							.divide(rateOfCurrency.getRateDown(), 2,BigDecimal.ROUND_HALF_UP);
					
					if(invoiceAndCredit.getRecordType().equals(Constant.CREDIT)){ //如果为invoice说明该金额为正数
						account1.setReceivableCurrency(invoiceAndCredit.getEnterCurrency());
						account1.setReceivableAmount(invoiceAndCredit.getDollar());		//应收
						account2.setReceivableCurrency(new BigDecimal(0).subtract(exDollar));
						account2.setReceivableAmount(new BigDecimal(0).subtract(invoiceAndCredit.getDollar()));
					}
					if(invoiceAndCredit.getRecordType().equals(Constant.INVOICE)){//如果为credit说明该金额为负数
						account1.setReceivableCurrency(new BigDecimal(0).subtract(invoiceAndCredit.getEnterCurrency()));
						account1.setReceivableAmount(new BigDecimal(0).subtract(invoiceAndCredit.getDollar()));
						account2.setReceivableCurrency(exDollar);
						account2.setReceivableAmount(invoiceAndCredit.getDollar());		//应收
					}
					accountRecordMapper.save(account1);
					accountRecordMapper.save(account2);
			}*/

	@Override
	public Page<Order> findOrderOfRegionList(Order order, Pageable pageable) {
		if (pageable == null) {
            pageable = new Pageable();
        }
        List<Order> page = orderMapper.findOrderOfRegionPage(order, pageable);
        int pageCount = orderMapper.findOrderOfRegionPageCount(order, pageable);
        return new Page<Order>(page, pageCount, pageable);
	}
	
	
	/**
	 * 
	 * @param totalList
	 * @param addList
	 */
	private void addToTotalList(List<OrderReceiveItem> totalList,List<OrderReceiveItem> addList){
		if(totalList != null && addList != null && addList.size() != 0){
			totalList.addAll(addList);
		}
	}
	
	/**
	 * 查看所以客人
	 */
	@Override
	public List<Order> findCustomerForTourId(String tourId) {
		return orderMapper.findCustomerForTourId(tourId);
	}

	@Override
	public List<Order> findByTourId(String tourId) {
		return orderMapper.findByTourId(tourId);
	}

	@Override
	public Order findCustomerForOrderId(String orderId) {
		return orderMapper.findCustomerForOrderId(orderId);
	}
	
	/**
	 * 根据总订单Id查找子订单
	 * @param orderTotalId
	 * @return
	 */
	@Override
	public List<Order> findChildOrderList(String ordersTotalId) {
		return orderMapper.findChildOrderList(ordersTotalId);
	}
	
	/**
	 * 查询一个总单下的子订单
	 */
	@Override
	public List<Order> findByOrdersTotalId(String ordersTotalId) {
		return orderMapper.findByOrdersTotalId(ordersTotalId);
	}

	@Override
	public Page<Order> findForGroupPage(Order order, Pageable pageable) {
		if (pageable == null) {
            pageable = new Pageable();
        }
        List<Order> page = orderMapper.findForGroupPage(order, pageable);
        int pageCount = orderMapper.findForGroupPageCount(order, pageable);
        return new Page<Order>(page, pageCount, pageable);
	}

	@Override
	public TourOrderListVO findOrderSumPepole(Order order) {
		return orderMapper.findOrderSumPepole(order);
	}

	@Override
	public Order findRegionOrderSumPepole(Order order) {
		return orderMapper.findRegionOrderSumPepole(order);
	}

	@Override
	public Order findGroupOrderSumPepole(Order order) {
		return orderMapper.findGroupOrderSumPepole(order);
	}

    /** 
     * 比较两个日期之间的大小 
     *  
     * @param d1 
     * @param d2 
     * @return 前者大于后者返回true 反之false 
     */  
    public boolean compareDate(Date d2) {  
        Calendar c1 = Calendar.getInstance();  
        Calendar c2 = Calendar.getInstance();  
        c1.set(2015,11,31);// 月份 0 开始计算
        c2.setTime(d2);  
      
        int result = c1.compareTo(c2);  
        if (result > 0)  
            return true;  
        else  
            return false;  
    }  
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	// 日期格式化方法
	private static String dateFormat(Date date){
		if(date != null){
			return dateFormat.format(date);
		}else{
			return "";
		}
	}
}
