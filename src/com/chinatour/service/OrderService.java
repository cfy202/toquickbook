package com.chinatour.service;

import java.util.List;
import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.entity.Customer;
import com.chinatour.entity.Order;
import com.chinatour.vo.PayCostEditVO;
import com.chinatour.vo.ProductVO;
import com.chinatour.vo.SingleOrdersVO;
import com.chinatour.vo.TourOrderListVO;

/**
 * Service 订单信息
 * 
 * @copyright Copyright: 2014
 * @author Pis
 * @create-time 2014-9-4 上午10:32:53
 * @revision 3.0
 */
public interface OrderService extends BaseService<Order, String> {


	/**
	 * 添加产品
	 * 
	 * @param productVO
	 */
	public void saveProduct(ProductVO  productVO);
	/**
	 * 添加产品
	 * 
	 * @param productVO
	 */
	public void saveProductNew(ProductVO  productVO);
	
	/**
	 * 根据子订单ID加载产品
	 * 
	 * @param ordersId
	 * @return
	 */
	public ProductVO loadProduct(String ordersId);
	
	/**
	 * 更新团订单产品
	 * 
	 * @param productVO
	 * @param deleteItemIds
	 */
	public void updateProduct(ProductVO productVO,String[] deleteItemIds);
	/**
	 * 更新团订单产品
	 * 
	 * @param productVO
	 * @param deleteItemIds
	 */
	public void updateProductNew(ProductVO productVO,String[] deleteItemIds);
	
	
	/**
	 * 根据订单ID加载收入支出
	 * @param orderId
	 * @return
	 */
	public PayCostEditVO findPayCostByOrderId(String orderId);
	
	/**
	 * 更新收入支出
	 * @param tourOrderPayCostVO
	 */
	public void updatePayCost(PayCostEditVO payCostEditVO);
	
	/**
	 * 查找到团订单list列表
	 * 若用户已分组,查出组下的团订单
	 * 若未分组,查出该用户下的团订单
	 * 
	 * @param order
	 * @param pageable
	 * @return
	 */
	public Page<TourOrderListVO> findTourOrderListVO(Order order, Pageable pageable);
	
	/**
	 * 本小组订单分页
	 * @param order
	 * @param pageable
	 * @return
	 */
	Page<Order> findForGrouPage(Order order, Pageable pageable);
	

	/**
	 * 查询订单
	 * @return
	 */
	public List<Order> find(Order order);
	
	/**
	 * 查询订单的agent
	 * @returns
	 */
	public List<Order> findUserOfOrder(Order order);
	

	/**
	 * 根据订单ID查找客人
	 * 
	 * @param id
	 * @return
	 */
	public Page<Customer> findCustomerPagesByOrderId(Pageable pageable,String orderId);
	
	
	
	
	/**
	 * 分页 	region查看订单
	 * @param order
	 * @param pageable
	 * @return
	 */
	public Page<Order> findOrderOfRegionList(Order order, Pageable pageable);
	
	/**
	 * 查看所以客人
	 * @param tourId
	 * @return
	 */
	public List<Order> findCustomerForTourId(String tourId);
	
	/**
	 * 查询一个团下的订单
	 * 
	 */
	public List<Order> findByTourId(String tourId);
	
	/**
	 * 查询一个订单的客人
	 * @param orderId
	 * @return
	 */
	public Order findCustomerForOrderId(String orderId);
	/**
	 * 根据总订单Id查找子订单
	 * @param orderTotalId
	 * @return
	 */
	public List<Order> findChildOrderList(String orderTotalId);
	
	/**
	 * 保存非团订单
	 * 
	 * @param singleOrdersVO
	 */
	public void saveSingleProduct(SingleOrdersVO singleOrdersVO);
	
	/**
	 * 根据订单ID加载出非团订单
	 * 
	 * @param ordersId
	 * @return
	 */
	public SingleOrdersVO loadSingleProduct(String ordersId);
	
	/**
	 * 修改非团订单
	 * 
	 * @param singleOrdersVO
	 */
	public void updateSingleProduct(SingleOrdersVO singleOrdersVO);
	/**
	 * 查询一个总单下的子订单
	 */
	public List<Order> findByOrdersTotalId(String ordersTotalId);

	public Page<Order> findForGroupPage(Order order, Pageable pageable);
	/**
	 * 
	 *查询订单人数
	 * */
	public TourOrderListVO findOrderSumPepole(Order order);
	
	/**
	 * 
	 *查询订单人数
	 * */
	public Order findRegionOrderSumPepole(Order order);
	
	/**
	 * 
	 *查询订单人数
	 * */
	public Order findGroupOrderSumPepole(Order order);
	
	
}
