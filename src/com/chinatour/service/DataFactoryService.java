package com.chinatour.service;

import java.util.List;

import com.chinatour.entity.Company;
import com.chinatour.entity.InvoiceAndCredit;
import com.chinatour.entity.Order;
import com.chinatour.entity.PayCostRecords;
import com.chinatour.entity.SupplierPriceForOrder;
import com.chinatour.entity.Vender;
import com.intuit.ipp.services.DataService;


public interface DataFactoryService {
	public DataService getDataService(Company domainCompany);
	
	/**
	 * 向财务系统生成invoice
	 */
	
	String getInvoice(InvoiceAndCredit invoiceAndCredit);
	
	/**
	 * 将分公司视为customer
	 * @param invoiceAndCredit
	 * @return
	 */
	String createInvoice(InvoiceAndCredit invoiceAndCredit);
	
	/**
	 * 收入支出审核进入财务系统
	 * @param pay
	 * @return
	 */
	Boolean orderToAccData(PayCostRecords pay);
	
	/**
	 * 下单成功之后向qb中插入一条invoice
	 * @param 团order
	 * @return
	 */
	Boolean saveOrderToAccData(Order order);
	
	/**
	 * op组团之后对invoice的class进行修改
	 * @param orderIds
	 * @return
	 */
	Boolean updateClass(Order order);
	
	
	/**
	 * 非团订单下单之后进入财务系统
	 * @param singleOrdersVO
	 * @return
	 */
	//public Boolean saveSingleOrderToAccData(Order order);
	
	public Boolean updateTourOrderInfo(Order order);
	
	//public Boolean updateSingleOrderInfo(Order order);
	
	/**
	 * 机票部门数据进入qb
	 * @return
	 */
	public Boolean airTicToQb(List<SupplierPriceForOrder> supplierPriceForOrderList);
	
	/**
	 * 同步vendor向qb
	 * @param customerId
	 * @return
	 */
	public Boolean createCustomer(Vender vender);
	public Boolean createVendor(Vender vender);
	
}
