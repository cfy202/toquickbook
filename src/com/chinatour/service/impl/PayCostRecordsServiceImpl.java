package com.chinatour.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinatour.Page;
import com.chinatour.Pageable;
import com.chinatour.entity.PayCostRecords;
import com.chinatour.persistence.DeptMapper;
import com.chinatour.persistence.OrderMapper;
import com.chinatour.persistence.PayCostRecordsMapper;
import com.chinatour.persistence.TourMapper;
import com.chinatour.persistence.TourTypeMapper;
import com.chinatour.service.AdminService;
import com.chinatour.service.PayCostRecordsService;

/**
 * @copyright   Copyright: 2014 
 * @author Jared
 * @create-time Sep 17, 2014 3:36:37 PM
 * @revision  3.0
 */

@Service("payCostRecordsServiceImpl")
public class PayCostRecordsServiceImpl extends BaseServiceImpl<PayCostRecords, String> implements PayCostRecordsService {
    
	
	@Autowired
	private PayCostRecordsMapper payCostRecordsMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private TourMapper tourMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private TourTypeMapper tourTypeMapper;
	 
	
	@Autowired
	public void setPayCostRecordsMapper(PayCostRecordsMapper payCostRecordsMapper){
		this.setBaseMapper(payCostRecordsMapper);
	}

	@Override
	public List<PayCostRecords> find(PayCostRecords payCostRecords) {
		return payCostRecordsMapper.find(payCostRecords);
	}
	

	@Override
	public Page<PayCostRecords> findForGroupPage(PayCostRecords payCostRecords,
			Pageable pageable) {
		if (pageable == null) {
            pageable = new Pageable();
        }
        List<PayCostRecords> page = payCostRecordsMapper.findForGroupPage(payCostRecords, pageable);
        int pageCount = payCostRecordsMapper.findForGroupPageCount(payCostRecords, pageable);
        return new Page<PayCostRecords>(page, pageCount, pageable);
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
	        c2.set(2015,11,31);// 月份 0 开始计算
	        c1.setTime(d2);  
	      
	        int result = c1.compareTo(c2);  
	        if (result > 0)  
	            return true;  
	        else  
	            return false;  
	    }

		@Override
		public Page<PayCostRecords> findCostForPage(
				PayCostRecords payCostRecords, Pageable pageable) {
			if (pageable == null) {
	            pageable = new Pageable();
	        }
	        List<PayCostRecords> page = payCostRecordsMapper.findCostForPage(payCostRecords, pageable);
	        int pageCount = payCostRecordsMapper.findCostForPageCount(payCostRecords, pageable);
	        return new Page<PayCostRecords>(page, pageCount, pageable);
		}

		@Override
		public List<PayCostRecords> findPayOrCostByOrders(
				PayCostRecords payCostRecords) {
			return payCostRecordsMapper.findPayOrCostByOrders(payCostRecords);
		}


}
