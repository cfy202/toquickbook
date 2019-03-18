package com.chinatour.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinatour.Constant;
import com.chinatour.entity.Admin;
import com.chinatour.entity.AirticketItems;
import com.chinatour.entity.City;
import com.chinatour.entity.Company;
import com.chinatour.entity.Country;
import com.chinatour.entity.Dept;
import com.chinatour.entity.InvoiceAndCredit;
import com.chinatour.entity.InvoiceAndCreditItems;
import com.chinatour.entity.Order;
import com.chinatour.entity.OrdersTotal;
import com.chinatour.entity.PayCostRecords;
import com.chinatour.entity.ReceivableInfoOfOrder;
import com.chinatour.entity.SupplierPriceForOrder;
import com.chinatour.entity.Tour;
import com.chinatour.entity.TourInfoForOrder;
import com.chinatour.entity.Vender;
import com.chinatour.persistence.AirticketItemsMapper;
import com.chinatour.persistence.CityMapper;
import com.chinatour.persistence.CompanyMapper;
import com.chinatour.persistence.CountryMapper;
import com.chinatour.persistence.DeptMapper;
import com.chinatour.persistence.InvoiceAndCreditMapper;
import com.chinatour.persistence.OrderMapper;
import com.chinatour.persistence.OrdersTotalMapper;
import com.chinatour.persistence.ReceivableInfoOfOrderMapper;
import com.chinatour.persistence.SOrderReceiveItemMapper;
import com.chinatour.persistence.TOrderReceiveItemMapper;
import com.chinatour.persistence.TourInfoForOrderMapper;
import com.chinatour.persistence.TourMapper;
import com.chinatour.persistence.VenderMapper;
import com.chinatour.service.AdminService;
import com.chinatour.service.DataFactoryService;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.data.Account;
import com.intuit.ipp.data.AccountBasedExpenseLineDetail;
import com.intuit.ipp.data.Bill;
import com.intuit.ipp.data.CreditMemo;
import com.intuit.ipp.data.CustomField;
import com.intuit.ipp.data.CustomFieldTypeEnum;
import com.intuit.ipp.data.Customer;
import com.intuit.ipp.data.IntuitAnyType;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.data.Item;
import com.intuit.ipp.data.Line;
import com.intuit.ipp.data.LineDetailTypeEnum;
import com.intuit.ipp.data.LinkedTxn;
import com.intuit.ipp.data.Payment;
import com.intuit.ipp.data.PaymentMethod;
import com.intuit.ipp.data.PhysicalAddress;
import com.intuit.ipp.data.Preferences;
import com.intuit.ipp.data.ReferenceType;
import com.intuit.ipp.data.SalesItemLineDetail;
import com.intuit.ipp.data.TelephoneNumber;
import com.intuit.ipp.data.Vendor;
import com.intuit.ipp.data.VendorCredit;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.IAuthorizer;
import com.intuit.ipp.security.OAuthAuthorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.ipp.util.Config;

@Service("DataServiceImpl")
public class DataServiceImpl implements  DataFactoryService{
	@Autowired
	private CompanyMapper companyMapper;
	@Autowired
	private AdminService adminService;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private CityMapper cityMapper;
	@Autowired
	private CountryMapper countryMapper;
	@Autowired
	private InvoiceAndCreditMapper invoiceAndCreditMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrdersTotalMapper ordersTotalMapper;
	@Autowired
	private TourMapper tourMapper;
	@Autowired
	private VenderMapper venderMapper;
	@Autowired
	private AirticketItemsMapper airticketItemsMapper;
	@Autowired
	private ReceivableInfoOfOrderMapper receivableInfoOfOrderMapper;
	@Autowired
	private TOrderReceiveItemMapper tOrderReceiveItemMapper;
	@Autowired
	private SOrderReceiveItemMapper sOrderReceiveItemMapper;
	@Autowired
	private TourInfoForOrderMapper tourInfoForOrderMapper;
	@Override
	public DataService getDataService(Company domainCompany) {
		verifyCompanyConnectedToQBO(domainCompany);
        IAuthorizer authorizer = new OAuthAuthorizer(Constant.OAUTHCONSUMERKEY,
        		Constant.OAUTHCONSUMERSECRET,
                domainCompany.getAccessToken(),
                domainCompany.getAccessTokenSecret());

        Context context;
        try {
            context = new Context(authorizer, ServiceType.QBO, domainCompany.getQboId());
        } catch (FMSException e) {
            throw new RuntimeException("Could not initialize Intuit context object", e);
        }

        return new DataService(context);
	}
	
	/**
	 * 往来账进入QB
	 */
	@Override
	public String getInvoice(InvoiceAndCredit invoiceAndCredit) {
		try{
		Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
		Company company = companyMapper.findById(Constant.OLDCOMPANYID);
		if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
	     }
		DataService dataService= this.getDataService(company);
		//Admin admin = adminService.getCurrent();
		Dept dept = deptMapper.findById(invoiceAndCredit.getDeptId());
		Vendor vendor = new Vendor();
		vendor.setDisplayName(dept.getDeptName());
			QueryResult	vendorResult = dataService.executeQuery("SELECT Id FROM Vendor WHERE DisplayName='"+dept.getDeptName().replaceAll("'", "\\\\'")+"'");
			List<Vendor> vendorList = (List<Vendor>) vendorResult.getEntities();
			if(vendorList==null||vendorList.size()==0){
				vendor.setGivenName(dept.getDeptName());
				vendor.setBalance(new BigDecimal(0.00));
				TelephoneNumber telNo = new TelephoneNumber();
				telNo.setFreeFormNumber(dept.getMobile());
				//vendor.setMobile(telNo);
				PhysicalAddress address = new PhysicalAddress();
				if(dept.getCityId()!=null&&(!dept.getDeptId().equals(""))){
					City city = cityMapper.findById(dept.getCityId());
					if(city!=null){
						address.setCity(city.getCityName());
					}
				}
				if(dept.getCountryId()!=null&&(!dept.getCountryId().equals(""))){
					Country country = countryMapper.findById(dept.getCountryId());
					address.setCountry(country.getCountryName());
				}
				address.setLine1(dept.getAddress());
				vendor.setBillAddr(address);
				dataService.add(vendor);
				vendorResult = dataService.executeQuery("SELECT Id FROM Vendor WHERE DisplayName='"+dept.getDeptName().replaceAll("'", "\\\\'")+"'");
				vendorList = (List<Vendor>) vendorResult.getEntities();
				vendor = vendorList.get(0);
			}else{
				vendor = vendorList.get(0);
			}
			ReferenceType referenceType = new ReferenceType();
			referenceType.setValue(vendor.getId());
			referenceType.setName(vendor.getDisplayName());
			QueryResult referenceResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='Bank Charges'");
			List<Account> referenceList = (List<Account>) referenceResult.getEntities();
			/*ReferenceType terms = new ReferenceType();
			terms.setValue("12");*/
			if(invoiceAndCredit.getRecordType().equals("INVOICE")){
				Bill bill = new Bill();
				bill.setVendorRef(referenceType);
				bill.setBalance(invoiceAndCredit.getDollar());
				bill.setDocNumber(Integer.toString(invoiceAndCredit.getBusinessNo()));
				bill.setShipAddr(vendor.getBillAddr());
				bill.setMemo(invoiceAndCredit.getTourCode());
				//bill.setSalesTermRef(terms);
				List<Line> lineList = new ArrayList<Line>();
				for(int i=0;i<invoiceAndCredit.getListInvoiceAndCreditItems().size();i++){
					InvoiceAndCreditItems invoiceAndCreditItems = invoiceAndCredit.getListInvoiceAndCreditItems().get(i);
					Line line = new Line();
					line.setLineNum(new BigInteger(Integer.toString(i+1)));
					line.setAmount(invoiceAndCreditItems.getDollarAmount());
					line.setDetailType(LineDetailTypeEnum.ACCOUNT_BASED_EXPENSE_LINE_DETAIL);
					line.setLineNum(new BigInteger(String.valueOf(1)));
					line.setDescription(invoiceAndCreditItems.getDescription());
					AccountBasedExpenseLineDetail accountBasedExpenseLineDetail = new AccountBasedExpenseLineDetail();
					ReferenceType account = new ReferenceType();
					account.setValue(referenceList.get(0).getId());
					account.setName(referenceList.get(0).getName());
					accountBasedExpenseLineDetail.setAccountRef(account);
					line.setAccountBasedExpenseLineDetail(accountBasedExpenseLineDetail);
					lineList.add(line);
				}
				bill.setLine(lineList);
			    dataService.add(bill);
			    return "approve successfully,transfer successfully";
			}else{
				VendorCredit vendorCredit = new VendorCredit();
				vendorCredit.setVendorRef(referenceType);
				vendorCredit.setTotalAmt(invoiceAndCredit.getDollar());
				vendorCredit.setDocNumber(invoiceAndCredit.getBusinessNo().toString());
				vendorCredit.setMemo(invoiceAndCredit.getTourCode());
				List<Line> lineList = new ArrayList<Line>();
				for(int i=0;i<invoiceAndCredit.getListInvoiceAndCreditItems().size();i++){
					InvoiceAndCreditItems invoiceAndCreditItems = invoiceAndCredit.getListInvoiceAndCreditItems().get(i);
					Line line = new Line();
					line.setLineNum(new BigInteger(Integer.toString(i+1)));
					line.setAmount(invoiceAndCreditItems.getDollarAmount());
					line.setDetailType(LineDetailTypeEnum.ACCOUNT_BASED_EXPENSE_LINE_DETAIL);
					line.setLineNum(new BigInteger(String.valueOf(1)));
					line.setDescription(invoiceAndCreditItems.getDescription());
					AccountBasedExpenseLineDetail accountBasedExpenseLineDetail = new AccountBasedExpenseLineDetail();
					ReferenceType account = new ReferenceType();
					account.setValue(referenceList.get(0).getId());
					account.setName(referenceList.get(0).getName());
					accountBasedExpenseLineDetail.setAccountRef(account);
					line.setAccountBasedExpenseLineDetail(accountBasedExpenseLineDetail);
					lineList.add(line);
				}
				vendorCredit.setLine(lineList);
				dataService.add(vendorCredit);
				return "approve successfully,transfer successfully";
			}
		}catch(Exception exception){
			InvoiceAndCredit invoiceAndCreditTemp = new InvoiceAndCredit();
			invoiceAndCreditTemp.setInvoiceAndCreditId(invoiceAndCredit.getInvoiceAndCreditId());
			invoiceAndCreditTemp.setIsSuccess(1);
			invoiceAndCreditMapper.update(invoiceAndCreditTemp);
			exception.printStackTrace();
		}
		return "approve successfully,transfer unsuccessfully";
}
	
	/**
	 * 审核往来账将分公司视为customer(分公司审核HD往来账)
	 */
	@Override
	public String createInvoice(InvoiceAndCredit invoiceAndCredit) {
		try{
			Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
			Company company = companyMapper.findById(Constant.OLDCOMPANYID);
			if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
		       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
		     }
			DataService dataService= this.getDataService(company);
			Dept dept = deptMapper.findById(invoiceAndCredit.getBillToDeptId());
			Customer customer = new Customer();
			customer.setDisplayName(dept.getDeptName());
			QueryResult	customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+dept.getDeptName().replaceAll("'", "\\\\'")+"'");
			List<Customer> customerList = (List<Customer>) customerResult.getEntities();
			if(customerList==null||customerList.size()==0){
				customer.setGivenName(dept.getDeptName());
				TelephoneNumber telNo = new TelephoneNumber();
				telNo.setFreeFormNumber(dept.getMobile());
				PhysicalAddress address = new PhysicalAddress();
				if(dept.getCityId()!=null&&(!dept.getDeptId().equals(""))){
					City city = cityMapper.findById(dept.getCityId());
					if(city!=null){
						address.setCity(city.getCityName());
					}
				}
				if(dept.getCountryId()!=null&&(!dept.getCountryId().equals(""))){
					Country country = countryMapper.findById(dept.getCountryId());
					address.setCountry(country.getCountryName());
				}
				address.setLine1(dept.getAddress());
				customer.setBillAddr(address);
				dataService.add(customer);
				customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+dept.getDeptName().replaceAll("'", "\\\\'")+"'");
				customerList = (List<Customer>) customerResult.getEntities();
				customer = customerList.get(0);
			}else{
				customer = customerList.get(0);
			}
			ReferenceType referenceType = new ReferenceType();
			referenceType.setValue(customer.getId());
			referenceType.setName(customer.getDisplayName());
			QueryResult referenceResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='Bank Charges'");
			List<Account> referenceList = (List<Account>) referenceResult.getEntities();
			ReferenceType terms = new ReferenceType();
			terms.setValue("12");
			if(invoiceAndCredit.getRecordType().equals("INVOICE")){
				Invoice invoice = new Invoice();
				invoice.setCustomerRef(referenceType);
				invoice.setTotalAmt(invoiceAndCredit.getDollar());
				invoice.setDocNumber(Integer.toString(invoiceAndCredit.getBusinessNo()));
				//invoice.setShipAddr(customer.getBillAddr());
				//invoice.setSalesTermRef(terms);
				List<Line> lineList = new ArrayList<Line>();
				for(int i=0;i<invoiceAndCredit.getListInvoiceAndCreditItems().size();i++){
					InvoiceAndCreditItems invoiceAndCreditItems = invoiceAndCredit.getListInvoiceAndCreditItems().get(i);
					Line line = new Line();
					line.setAmount(invoiceAndCreditItems.getDollarAmount());
					line.setLineNum(new BigInteger(Integer.toString(i+1)));
					line.setDetailType(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL);
					line.setDescription(invoiceAndCreditItems.getDescription());
					SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
					salesItemLineDetail.setUnitPrice(invoiceAndCreditItems.getDollarAmount());
					salesItemLineDetail.setQty(new BigDecimal(1));
					line.setSalesItemLineDetail(salesItemLineDetail);
					lineList.add(line);
				}
				invoice.setLine(lineList);
			    dataService.add(invoice);
			    return "approve successfully,transfer successfully";
			}else{
				CreditMemo creditMemo = new CreditMemo();
				creditMemo.setCustomerRef(referenceType);
				creditMemo.setTotalAmt(invoiceAndCredit.getDollar());
				creditMemo.setDocNumber(invoiceAndCredit.getBusinessNo().toString());
				//creditMemo.setMemo(invoiceAndCredit.getTourCode());
				List<Line> lineList = new ArrayList<Line>();
				for(int i=0;i<invoiceAndCredit.getListInvoiceAndCreditItems().size();i++){
					InvoiceAndCreditItems invoiceAndCreditItems = invoiceAndCredit.getListInvoiceAndCreditItems().get(i);
					Line line = new Line();
					line.setAmount(invoiceAndCreditItems.getDollarAmount());
					line.setLineNum(new BigInteger(Integer.toString(i+1)));
					line.setDetailType(LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL);
					line.setDescription(invoiceAndCreditItems.getDescription());
					SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
					salesItemLineDetail.setUnitPrice(invoiceAndCreditItems.getDollarAmount());
					salesItemLineDetail.setQty(new BigDecimal(1));
					line.setSalesItemLineDetail(salesItemLineDetail);
					lineList.add(line);
				}
				creditMemo.setLine(lineList);
				dataService.add(creditMemo);
				return "approve successfully,transfer successfully";
			}
		}catch(Exception exception){
			InvoiceAndCredit invoiceAndCreditTemp = new InvoiceAndCredit();
			invoiceAndCreditTemp.setInvoiceAndCreditId(invoiceAndCredit.getInvoiceAndCreditId());
			invoiceAndCreditTemp.setIsSuccess(1);
			invoiceAndCreditMapper.update(invoiceAndCreditTemp);
			exception.printStackTrace();
		}
		return "approve successfully,transfer unsuccessfully";
}
	
	/**
	 * 将账单数据转为accdata数据
	 * @param pay
	 * @return
	 */
	@Override
	@Transactional
	public Boolean orderToAccData(PayCostRecords pay){
		try{
			Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
			Company company = companyMapper.findById(Constant.OLDCOMPANYID);
			if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
		       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
		     }
			DataService dataService= this.getDataService(company);
			Order order = orderMapper.findById(pay.getOrderId());
			OrdersTotal ordersTotal = ordersTotalMapper.findById(order.getOrdersTotalId());
			Tour tour = tourMapper.findById(order.getTourId());
		if(pay.getPayOrCost()==1){
			QueryResult payMethodResult = dataService.executeQuery("SELECT Id FROM PaymentMethod WHERE Name='"+pay.getWay()+"'");
			List<PaymentMethod> paymentMethod = (List<PaymentMethod>) payMethodResult.getEntities();
			ReferenceType paymentType = new ReferenceType();
			if(paymentMethod!=null&&paymentMethod.size()>0){
				paymentType.setValue(paymentMethod.get(0).getId());
				paymentType.setType("PaymentMethod");
			}else{
				PaymentMethod payMethod = new PaymentMethod();
				payMethod.setName(pay.getWay());
				dataService.add(payMethod);
				payMethodResult = dataService.executeQuery("SELECT Id FROM PaymentMethod WHERE Name='"+pay.getWay()+"'");
				paymentMethod = (List<PaymentMethod>) payMethodResult.getEntities();
				paymentType.setValue(paymentMethod.get(0).getId());
				paymentType.setType("PaymentMethod");
			}
			Payment payment = new Payment();
			if(ordersTotal.getWr().equals("wholeSale")){
				Customer customer = new Customer();
				Vender vendor= venderMapper.findById(order.getPeerId());
				customer.setDisplayName(vendor.getName());
					QueryResult customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE CompanyName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
					List<Customer> customerList = (List<Customer>) customerResult.getEntities();
					if(customerList==null||customerList.size()==0){
						customer.setGivenName(vendor.getName());
						customer.setContactName(vendor.getContactor());
						customer.setBalance(new BigDecimal(0.00));
						TelephoneNumber telNo = new TelephoneNumber();
						telNo.setFreeFormNumber(vendor.getTel());
						PhysicalAddress address = new PhysicalAddress();
						address.setCity(vendor.getCityId());
						address.setCountry(vendor.getCountryName());
						address.setLine1(vendor.getAddress());
						customer.setBillAddr(address);
						dataService.add(customer);
						customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
						customerList = (List<Customer>) customerResult.getEntities();
						customer = customerList.get(0);
					}else{
						customer = customerList.get(0);
					}
				ReferenceType referenceType = new ReferenceType();
				referenceType.setValue(customer.getId());
				referenceType.setName(customer.getDisplayName());
				if(pay.getSum().compareTo(new BigDecimal(0))==1){
					payment.setCustomerRef(referenceType);
					payment.setTotalAmt(pay.getSum());
					payment.setPaymentRefNum(pay.getCode());
					paymentType.setName(pay.getWay());
					payment.setPaymentMethodRef(paymentType);
					payment.setPrivateNote(order.getOrderNo());
					payment.setTxnDate(pay.getApproveDate());
					dataService.add(payment);
					return true;
				}
			}else{
					Customer customer = new Customer();
					Admin admin = adminService.findById(order.getUserId());
					Dept dept = deptMapper.findById(admin.getDeptId());
					customer.setDisplayName(admin.getUsername());
					QueryResult customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+admin.getUsername().replaceAll("'", "\\\\'")+"'");
					List<Customer> customerList = (List<Customer>) customerResult.getEntities();
					if(customerList==null||customerList.size()==0){
						customer.setGivenName(admin.getUsername());
						customer.setContactName(admin.getUsername());
						customer.setBalance(new BigDecimal(0.00));
						PhysicalAddress address = new PhysicalAddress();
						address.setLine1(dept.getAddress());
						customer.setBillAddr(address);
						dataService.add(customer);
						customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+admin.getUsername().replaceAll("'", "\\\\'")+"'");
						customerList = (List<Customer>) customerResult.getEntities();
						customer = customerList.get(0);
					}else{
						customer = customerList.get(0);
					}
					ReferenceType referenceType = new ReferenceType();
					referenceType.setValue(customer.getId());
					referenceType.setName(customer.getDisplayName());
					if(pay.getSum().compareTo(new BigDecimal(0))==1){
						payment.setCustomerRef(referenceType);
						payment.setTotalAmt(pay.getSum());
						payment.setPaymentRefNum(pay.getCode());
						payment.setPrivateNote(order.getOrderNo());
						payment.setPaymentMethodRef(paymentType);
						payment.setTxnDate(pay.getApproveDate());
						List<Line> lineList = getLine(order.getOrderNo(),dataService,pay.getSum());
						payment.setLine(lineList);
						dataService.add(payment);
						return true;
					}
			}
		}else{
				ReferenceType classType = new ReferenceType();
				if(order.getIsSelfOrganize()==2){
					classType = getClassForSingle(order.getUserName(),order.getOrderNo(),dataService);
				}else{
					String tourCode = order.getTourCode();
					if(tourCode.length()-tourCode.indexOf(" ")==0){
						tourCode = tourCode.substring(0, tourCode.indexOf(" "));
					}
					classType = getClass(order.getUserName(),tourCode,dataService);
				}
				Vendor vendor = new Vendor();
				Vender vendorTemp= venderMapper.findById(pay.getVenderId());
				vendor.setDisplayName(vendorTemp.getName());
				ReferenceType accountType = getAccount(dataService,vendorTemp.getName());
				QueryResult	vendorResult = dataService.executeQuery("SELECT Id FROM Vendor WHERE DisplayName='"+vendorTemp.getName().replaceAll("'", "\\\\'")+"'");
				List<Vendor> vendorList = (List<Vendor>) vendorResult.getEntities();
				if(vendorList==null||vendorList.size()==0){
					vendor.setGivenName(vendorTemp.getName());
					vendor.setContactName(vendorTemp.getContactor());
					vendor.setBalance(new BigDecimal(0.00));
					TelephoneNumber telNo = new TelephoneNumber();
					telNo.setFreeFormNumber(vendorTemp.getTel());
					//vendor.setMobile(telNo);
					PhysicalAddress address = new PhysicalAddress();
					address.setCity(vendorTemp.getCityId());
					address.setCountry(vendorTemp.getCountryName());
					address.setLine1(vendorTemp.getAddress());
					vendor.setBillAddr(address);
					dataService.add(vendor);
					vendorResult = dataService.executeQuery("SELECT Id FROM Vendor WHERE DisplayName='"+vendorTemp.getName().replaceAll("'", "\\\\'")+"'");
					vendorList = (List<Vendor>) vendorResult.getEntities();
					vendor = vendorList.get(0);
				}else{
					vendor = vendorList.get(0);
				}
				ReferenceType referenceType = new ReferenceType();
				referenceType.setValue(vendor.getId());
				referenceType.setName(vendor.getDisplayName());
				if(pay.getSum().compareTo(new BigDecimal(0.00))==1){
					Bill bill = new Bill();
					bill.setVendorRef(referenceType);
					bill.setBalance(pay.getSum());
					bill.setDocNumber(pay.getCode());
					bill.setShipAddr(vendor.getBillAddr());
					bill.setTxnDate(pay.getCreateDate());
					String privateNote = order.getOrderNo();
					String tourCode = order.getTourCode();
					if(tourCode!=null&&tourCode.length()>0){
						privateNote =  privateNote+":"+tourCode;
					}
					bill.setPrivateNote(privateNote);
					List<Line> lineList = new ArrayList<Line>();
					Line line = new Line();
					line.setAmount(pay.getSum());
					line.setDetailType(LineDetailTypeEnum.ACCOUNT_BASED_EXPENSE_LINE_DETAIL);
					line.setLineNum(new BigInteger(String.valueOf(1)));
					line.setDescription(pay.getRemark());
					line.setAmount(pay.getSum());
					AccountBasedExpenseLineDetail accountBasedExpenseLineDetail = new AccountBasedExpenseLineDetail();
					accountBasedExpenseLineDetail.setAccountRef(accountType);
					accountBasedExpenseLineDetail.setClassRef(classType);
					line.setAccountBasedExpenseLineDetail(accountBasedExpenseLineDetail);
					lineList.add(line);
					bill.setLine(lineList);
				    dataService.add(bill);
				    return true;
				}else{
					VendorCredit vendorCredit = new VendorCredit();
					vendorCredit.setVendorRef(referenceType);
					vendorCredit.setTotalAmt(pay.getSum().multiply(new BigDecimal(-1)));
					vendorCredit.setDocNumber(pay.getBillNo());
					vendorCredit.setTxnDate(pay.getCreateDate());
					if(tour!=null){
						vendorCredit.setPrivateNote(tour.getTourCode());
					}
					List<Line> lineList = new ArrayList<Line>();
					Line line = new Line();
					LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.ACCOUNT_BASED_EXPENSE_LINE_DETAIL;
					line.setDetailType(lineDetailTypeEnum);
					line.setLineNum(new BigInteger(String.valueOf(1)));
					line.setDescription(pay.getRemark());
					line.setAmount(pay.getSum().multiply(new BigDecimal(-1)));
					AccountBasedExpenseLineDetail accountBasedExpenseLineDetail = new AccountBasedExpenseLineDetail();
					accountBasedExpenseLineDetail.setAccountRef(accountType);
					accountBasedExpenseLineDetail.setClassRef(classType);
					line.setAccountBasedExpenseLineDetail(accountBasedExpenseLineDetail);
					lineList.add(line);
					vendorCredit.setLine(lineList);
					dataService.add(vendorCredit);
					return true;
				}
		}
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return false;
		}
	 
	private void verifyCompanyConnectedToQBO(Company company) {
	        if (company.getConnectedToQbo()!=1) {
	            throw new RuntimeException("Company is not connected to QBO: " + company.getName());
	        }
	    }

/**
 * 组团后将tourCode接入进QB
 */
@Override
public Boolean updateClass(Order order) {
	Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
	Company company = companyMapper.findById(Constant.OLDCOMPANYID);
	if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
     }
	DataService dataService= this.getDataService(company);
	try {
		ReferenceType referenceType = new ReferenceType();
				QueryResult invoiceResult;
				invoiceResult = dataService.executeQuery("SELECT * FROM Invoice WHERE DocNumber='"+order.getOrderNo()+"'");
				List<Invoice> invoiceList = (List<Invoice>) invoiceResult.getEntities();
				Invoice invoice = invoiceList.get(0);
				invoice.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
				if(order.getOrderType()==5){
					referenceType = getClassForSingle(order.getUserName(),order.getOrderNo(),dataService);
				}else{
					String tourCode = order.getTourCode();
					if(tourCode.length()-tourCode.indexOf(" ")==0){
						tourCode = tourCode.substring(0, tourCode.indexOf(" "));
					}
					referenceType = getClass(order.getUserName(),tourCode,dataService);
				}
				SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
				salesItemLineDetail.setClassRef(referenceType);
				if(invoiceList!=null&&invoiceList.size()!=0){
					List<Line> lineList = invoiceList.get(0).getLine();
					for(Line line:lineList){
						line.setSalesItemLineDetail(salesItemLineDetail);
					}
					invoice.setLine(lineList);
					dataService.update(invoice);
				}else{
					invoiceResult = dataService.executeQuery("SELECT * FROM CreditMemo WHERE DocNumber='"+order.getOrderNo()+"'");
					List<CreditMemo> creditMemoList = (List<CreditMemo>) invoiceResult.getEntities();
					CreditMemo creditMemo = creditMemoList.get(0);
					creditMemo.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
					List<Line> lineList = creditMemo.getLine();
					for(Line line:lineList){
						line.setSalesItemLineDetail(salesItemLineDetail);
					}
					creditMemo.setLine(lineList);
					dataService.update(creditMemo);
				}
	} catch (FMSException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

/**
 * 订单下单成功
 */
@Override
@Transactional
public Boolean saveOrderToAccData(Order order) {
	try {
			Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
			Company company = companyMapper.findById(Constant.OLDCOMPANYID);
			if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
		       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
		     }
			DataService dataService= this.getDataService(company);
			ReferenceType itemRef = getItemType(dataService);
			OrdersTotal ordersTotal = ordersTotalMapper.findById(order.getOrdersTotalId());
			ReferenceType classType = new ReferenceType();
			if(order.getTourCode()!=null&&order.getTourCode().length()!=0){
				String tourCode = order.getTourCode();
				if(tourCode.length()-tourCode.indexOf(" ")==4){
					tourCode = tourCode.substring(0, tourCode.indexOf(" "));
				}
				classType = getClass(order.getUserName(),tourCode,dataService);
			}else if(order.getIsSelfOrganize()==2){
				classType = getClassForSingle(order.getUserName(),order.getOrderNo(),dataService);
			}
			String wr = ordersTotal.getWr();
			Customer customer = new Customer();
			Admin admin = adminService.findById(order.getUserId());
			ReceivableInfoOfOrder receivableInfoOdOrder = receivableInfoOfOrderMapper.findByOrderId(order.getId());
			TourInfoForOrder tourInfoForOrder = tourInfoForOrderMapper.findByOrderId(order.getId());
			Date createDate = tourInfoForOrder.getScheduleOfArriveTime();
	if(wr.equals("wholeSale")){
				Vender vendor= venderMapper.findById(ordersTotal.getCompanyId());
				customer.setDisplayName(vendor.getName());
				QueryResult customerResult;
				customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
				List<Customer> customerList = (List<Customer>) customerResult.getEntities();
			if(customerList==null||customerList.size()==0){
				customer.setGivenName(vendor.getName());
				customer.setContactName(vendor.getContactor());
				customer.setBalance(new BigDecimal(0.00));
				TelephoneNumber telNo = new TelephoneNumber();
				telNo.setFreeFormNumber(vendor.getTel());
				PhysicalAddress address = new PhysicalAddress();
				address.setCity(vendor.getCityId());
				address.setCountry(vendor.getCountryName());
				address.setLine1(vendor.getAddress());
				customer.setBillAddr(address);
				dataService.add(customer);
				customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
				customerList = (List<Customer>) customerResult.getEntities();
				customer = customerList.get(0);
			}else{
				customer = customerList.get(0);
			}
	}else{
			customer.setDisplayName(admin.getUsername());
			QueryResult customerResult;
			customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+customer.getDisplayName().replaceAll("'", "\\\\'")+"'");
			List<Customer> customerList = (List<Customer>) customerResult.getEntities();
		if(customerList==null||customerList.size()==0){
			customer.setGivenName(admin.getUsername());
			customer.setContactName(admin.getUsername());
			customer.setBalance(new BigDecimal(0.00));
			TelephoneNumber telNo = new TelephoneNumber();
			telNo.setFreeFormNumber(admin.getTel());
			PhysicalAddress address = new PhysicalAddress();
			address.setLine1(admin.getAddress());
			customer.setBillAddr(address);
			dataService.add(customer);
			customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+customer.getDisplayName().replaceAll("'", "\\\\'")+"'");
			customerList = (List<Customer>) customerResult.getEntities();
			customer = customerList.get(0);
		}else{
			customer = customerList.get(0);
		}
	}
				ReferenceType referenceType = new ReferenceType();
				referenceType.setValue(customer.getId());
				referenceType.setName(customer.getDisplayName());
				ReferenceType termsType = new ReferenceType();
			if(receivableInfoOdOrder.getSumFee().compareTo(new BigDecimal(0))==1){
					Invoice invoice = new Invoice();
					invoice.setAutoDocNumber(false);
					invoice.setCustomerRef(referenceType);
					invoice.setBalance(receivableInfoOdOrder.getSumFee());
					invoice.setDocNumber(order.getOrderNo());
					invoice.setBillAddr(customer.getBillAddr());
					invoice.setPrivateNote(order.getOrderNo());
					if(wr.equals("wholeSale")){
						List<CustomField> customFieldList = new ArrayList<CustomField>();
						CustomField customField = new CustomField();
						customField.setName("Type");
						customField.setDefinitionId("1");
						customField.setStringValue("SG");
						customField.setType(CustomFieldTypeEnum.STRING_TYPE);
						customFieldList.add(customField);
						invoice.setCustomField(customFieldList);
					}
					if(order.getTourCode()!=null&&order.getTourCode().length()>0){
						invoice.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
					}
					invoice.setClassRef(classType);
					invoice.setSalesTermRef(termsType);
					if(createDate!=null){
						invoice.setTxnDate(createDate);
					}
					List<Line> lineList = new ArrayList<Line>();
					Line line = new Line();
					LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
					line.setDetailType(lineDetailTypeEnum);
					line.setLineNum(new BigInteger(String.valueOf(1)));
					line.setDescription("SG");
					SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
					salesItemLineDetail.setUnitPrice(receivableInfoOdOrder.getSumFee());
					salesItemLineDetail.setQty(new BigDecimal(1));
					salesItemLineDetail.setClassRef(classType);
					line.setAmount(receivableInfoOdOrder.getSumFee());
					salesItemLineDetail.setItemRef(itemRef);
					salesItemLineDetail.setClassRef(classType);
					line.setSalesItemLineDetail(salesItemLineDetail);
					lineList.add(line);
					invoice.setLine(lineList);
					dataService.add(invoice);
					return true;
			}else{
					CreditMemo creditMemo = new CreditMemo();
					creditMemo.setCustomerRef(referenceType);
					creditMemo.setBalance(receivableInfoOdOrder.getSumFee().multiply(new BigDecimal(-1)));
					creditMemo.setDocNumber(order.getOrderNo());
					creditMemo.setBillAddr(customer.getBillAddr());
					creditMemo.setPaymentMethodRef(referenceType);
					creditMemo.setPrivateNote(order.getOrderNo());
					if(order.getTourCode()!=null&&order.getTourCode().length()>0){
						creditMemo.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
					}
					creditMemo.setClassRef(classType);
					if(createDate!=null){
						creditMemo.setTxnDate(createDate);
					}
					List<Line> lineListForCredit = new ArrayList<Line>();
					Line lineForCredit = new Line();
					LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
					lineForCredit.setDetailType(lineDetailTypeEnum);
					lineForCredit.setDescription("SG");
					lineForCredit.setLineNum(new BigInteger(String.valueOf(1)));
					SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
					salesItemLineDetail.setUnitPrice(receivableInfoOdOrder.getSumFee().multiply(new BigDecimal(-1)));
					salesItemLineDetail.setQty(new BigDecimal(1));
					salesItemLineDetail.setItemRef(itemRef);
					salesItemLineDetail.setClassRef(classType);
					lineForCredit.setAmount(receivableInfoOdOrder.getSumFee().multiply(new BigDecimal(-1)));
					lineForCredit.setSalesItemLineDetail(salesItemLineDetail);
					lineListForCredit.add(lineForCredit);
					creditMemo.setLine(lineListForCredit);
					dataService.add(creditMemo);
					return true;
			}
	} catch (FMSException e) {
		e.printStackTrace();
	}
	return false;
}

/**
 * 更新团订单信息时qb中订单信息同样修改
 */
	@Override
	@Transactional
	public Boolean updateTourOrderInfo(Order order) {
		Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
		Company company = companyMapper.findById(Constant.OLDCOMPANYID);
		if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
	     }
		DataService dataService= this.getDataService(company);
		QueryResult invoiceResult;
		try {
			invoiceResult = dataService.executeQuery("SELECT * FROM Invoice WHERE DocNumber='"+order.getOrderNo()+"'");
			List<Invoice> invoiceList = (List<Invoice>) invoiceResult.getEntities();
			ReferenceType termsType = new ReferenceType();
			ReferenceType itemRef = getItemType(dataService);
			ReferenceType classType = new ReferenceType();
			BigDecimal tourFee = order.getCommonTourFee();
			if(invoiceList.size()!=0){
				Invoice invoice = new Invoice();
				invoice = invoiceList.get(0);
				//当订单金额由正数更改为负数时,将原有的invoice删除，创建一个creditMemo
				if(tourFee.compareTo(new BigDecimal(0))==-1){
					String tourCode = order.getTourCode();
					if(tourCode!=null&&tourCode.length()!=0){
						if(tourCode.length()-tourCode.indexOf(" ")==4){
							tourCode = tourCode.substring(0, tourCode.indexOf(" "));
						}
						classType = getClass(order.getUserName(),order.getTourCode(),dataService);
					}else if(order.getIsSelfOrganize()==2){
						classType = getClassForSingle(order.getUserName(),order.getOrderNo(),dataService);
					}
					dataService.delete(invoice);
					CreditMemo creditMemo = new CreditMemo();
					creditMemo.setCustomerRef(invoice.getCustomerRef());
					creditMemo.setAutoDocNumber(false);
					creditMemo.setDocNumber(order.getOrderNo());
					creditMemo.setBalance(tourFee.multiply(new BigDecimal(-1)));
					creditMemo.setSalesTermRef(termsType);
					creditMemo.setCustomerRef(invoice.getCustomerRef());
					if(order.getTourCode()!=null&&order.getTourCode().length()>0){
						creditMemo.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
					}
					List<Line> lineList = new ArrayList<Line>();
					Line line = new Line();
					LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
					line.setDetailType(lineDetailTypeEnum);
					line.setDescription("SG");
					line.setLineNum(new BigInteger(String.valueOf(1)));
					SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
					salesItemLineDetail.setUnitPrice(tourFee.multiply(new BigDecimal(-1)));
					salesItemLineDetail.setQty(new BigDecimal(1));
					salesItemLineDetail.setClassRef(classType);
					line.setAmount(tourFee.multiply(new BigDecimal(-1)));
					line.setSalesItemLineDetail(salesItemLineDetail);
					lineList.add(line);
					creditMemo.setLine(lineList);
					dataService.add(creditMemo);
					return true;
				}
				else{
						invoice.setBalance(tourFee);
						if(order.getTourCode()!=null&&order.getTourCode().length()>0){
							invoice.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
						}
						List<Line> lineList = new ArrayList<Line>();
						Line line = invoice.getLine().get(0);
						LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
						line.setDetailType(lineDetailTypeEnum);
						line.setLineNum(new BigInteger(String.valueOf(1)));
						SalesItemLineDetail salesItemLineDetail = line.getSalesItemLineDetail();
						salesItemLineDetail.setUnitPrice(tourFee);
						salesItemLineDetail.setQty(new BigDecimal(1));
						salesItemLineDetail.setItemRef(itemRef);
						line.setAmount(tourFee);
						line.setSalesItemLineDetail(salesItemLineDetail);
						lineList.add(line);
						invoice.setLine(lineList);
						dataService.update(invoice);
				}
				return true;
			}else{
				invoiceResult = dataService.executeQuery("SELECT * FROM CreditMemo WHERE DocNumber='"+order.getOrderNo()+"'");
				List<CreditMemo> creditMemoList = (List<CreditMemo>) invoiceResult.getEntities();
				CreditMemo creditMemo = creditMemoList.get(0);
				if(creditMemoList!=null&&creditMemoList.size()!=0){
					if(tourFee.compareTo(new BigDecimal(0))==1){
						String tourCode = order.getTourCode();
						if(tourCode!=null&&tourCode.length()!=0){
							if(tourCode.length()-tourCode.indexOf(" ")==4){
								tourCode = tourCode.substring(0, tourCode.indexOf(" "));
							}
							classType = getClass(order.getUserName(),order.getTourCode(),dataService);
						}else if(order.getIsSelfOrganize()==2){
							classType = getClassForSingle(order.getUserName(),order.getOrderNo(),dataService);
						}
							dataService.delete(creditMemo);
							Invoice invoice = new Invoice();
							invoice.setCustomerRef(creditMemo.getCustomerRef());
							invoice.setAutoDocNumber(false);
							invoice.setBalance(tourFee);
							invoice.setSalesTermRef(termsType);
							invoice.setDocNumber(order.getOrderNo());
							invoice.setCustomerRef(creditMemo.getCustomerRef());
							invoice.setPrivateNote(order.getOrderNo());
							if(order.getTourCode()!=null&&order.getTourCode().length()>0){
								invoice.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
							}
							List<Line> lineList = new ArrayList<Line>();
							Line line = new Line();
							LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
							line.setDetailType(lineDetailTypeEnum);
							line.setDescription("SG");
							line.setLineNum(new BigInteger(String.valueOf(1)));
							SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
							salesItemLineDetail.setUnitPrice(tourFee);
							salesItemLineDetail.setQty(new BigDecimal(1));
							salesItemLineDetail.setClassRef(classType);
							line.setAmount(tourFee);
							line.setSalesItemLineDetail(salesItemLineDetail);
							lineList.add(line);
							invoice.setLine(lineList);
							dataService.add(invoice);
							return true;
					}else{
							creditMemo.setBalance(tourFee.multiply(new BigDecimal(-1)));
							if(order.getTourCode()!=null&&order.getTourCode().length()>0){
								creditMemo.setPrivateNote(order.getOrderNo()+":"+order.getTourCode());
							}
							List<Line> lineListForCredit = new ArrayList<Line>();
							Line lineForCredit = creditMemo.getLine().get(0);
							LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
							lineForCredit.setDetailType(lineDetailTypeEnum);
							lineForCredit.setLineNum(new BigInteger(String.valueOf(1)));
							SalesItemLineDetail salesItemLineDetail = lineForCredit.getSalesItemLineDetail();
							salesItemLineDetail.setUnitPrice(tourFee.multiply(new BigDecimal(-1)));
							salesItemLineDetail.setQty(new BigDecimal(1));
							salesItemLineDetail.setItemRef(itemRef);
							lineForCredit.setAmount(tourFee.multiply(new BigDecimal(-1)));
							lineForCredit.setSalesItemLineDetail(salesItemLineDetail);
							lineListForCredit.add(lineForCredit);
							creditMemo.setLine(lineListForCredit);
							dataService.update(creditMemo);
					}
					return true;
				}
			}
		} catch (FMSException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 机票部门数据进入qb
	 */
	@Override
	public Boolean airTicToQb(List<SupplierPriceForOrder> supplierPriceForOrderList){
		Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
		Company company = companyMapper.findById(Constant.OLDCOMPANYID);
		if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
	     }
		DataService dataService= this.getDataService(company);
		Admin admin = adminService.getCurrent();
		for(SupplierPriceForOrder supplierPriceForOrder:supplierPriceForOrderList){
			try{
			Customer customer = new Customer();
			ReferenceType customerType = new ReferenceType();
				//当给agent订机票时,当给Retail订机票时
			if(supplierPriceForOrder.getType()==1||supplierPriceForOrder.getType()==0){
				Admin agent = adminService.findById(supplierPriceForOrder.getAgentId());
				QueryResult customerResult;
				customer.setDisplayName(agent.getUsername());
				customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+customer.getDisplayName().replaceAll("'", "\\\\'")+"'");
				List<Customer> customerList = (List<Customer>) customerResult.getEntities();
				if(customerList==null||customerList.size()==0){
					customer.setGivenName(admin.getUsername());
					customer.setContactName(admin.getUsername());
					customer.setBalance(new BigDecimal(0.00));
					TelephoneNumber telNo = new TelephoneNumber();
					telNo.setFreeFormNumber(admin.getTel());
					PhysicalAddress address = new PhysicalAddress();
					address.setLine1(admin.getAddress());
					customer.setBillAddr(address);
					dataService.add(customer);
					customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+customer.getDisplayName().replaceAll("'", "\\\\'")+"'");
					customerList = (List<Customer>) customerResult.getEntities();
					customer = customerList.get(0);
				}else{
					customer = customerList.get(0);
				}
				customerType.setValue(customer.getId());
				customerType.setName(customer.getDisplayName());
				//判断如果为不同部门之间则通过往来账进入qb
				if(admin.getDeptId().equals(agent.getDeptId())){
					if(new BigDecimal(supplierPriceForOrder.getTempValue06()).compareTo(new BigDecimal(0.00))==1){
						Invoice invoice = getInvoiceFormFlit(supplierPriceForOrder);
						invoice.setCustomerRef(customerType);
						invoice.setBillAddr(customer.getBillAddr());
						dataService.add(invoice);
					}else{
						CreditMemo creditMemo = getCreditMemoFormFlit(supplierPriceForOrder);
						creditMemo.setCustomerRef(customerType);
						creditMemo.setBillAddr(customer.getBillAddr());
						dataService.add(creditMemo);
					}
				}
				//当给wholse订机票时type=2
			}else if(supplierPriceForOrder.getType()!=null&&supplierPriceForOrder.getType()==2){
				Vender vendor= venderMapper.findById(supplierPriceForOrder.getAgency());
				customer.setDisplayName(vendor.getName());
				QueryResult customerResult;
					customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
					List<Customer> customerList = (List<Customer>) customerResult.getEntities();
					if(customerList==null||customerList.size()==0){
						customer.setGivenName(vendor.getName());
						customer.setContactName(vendor.getContactor());
						customer.setBalance(new BigDecimal(0.00));
						TelephoneNumber telNo = new TelephoneNumber();
						telNo.setFreeFormNumber(vendor.getTel());
						PhysicalAddress address = new PhysicalAddress();
						address.setCity(vendor.getCityId());
						address.setCountry(vendor.getCountryName());
						address.setLine1(vendor.getAddress());
						customer.setBillAddr(address);
						dataService.add(customer);
						customerResult = dataService.executeQuery("SELECT Id FROM Customer WHERE DisplayName='"+vendor.getName().replaceAll("'", "\\\\'")+"'");
						customerList = (List<Customer>) customerResult.getEntities();
						customer = customerList.get(0);
					}else{
						customer = customerList.get(0);
					}
					ReferenceType referenceType = new ReferenceType();
					referenceType.setValue(customer.getId());
					referenceType.setName(customer.getDisplayName());
					if(new BigDecimal(supplierPriceForOrder.getTempValue06()).compareTo(new BigDecimal(0.00))==1){
						Invoice invoice = getInvoiceFormFlit(supplierPriceForOrder);
						invoice.setCustomerRef(referenceType);
						invoice.setBillAddr(customer.getBillAddr());
						dataService.add(invoice);
					}else{
						CreditMemo creditMemo = getCreditMemoFormFlit(supplierPriceForOrder);
						creditMemo.setCustomerRef(referenceType);
						creditMemo.setBillAddr(customer.getBillAddr());
						dataService.add(creditMemo);
					}
			}
			getBillForArc(supplierPriceForOrder,dataService,customerType);
			}catch(Exception exception){
				exception.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 由机票产生invoice
	 * @return
	 */
	private Invoice getInvoiceFormFlit(SupplierPriceForOrder supplierPriceForOrder){
		ReferenceType terms = new ReferenceType();
		terms.setValue("12");
		Invoice invoice = new Invoice();
		invoice.setAutoDocNumber(false);
		invoice.setDocNumber(supplierPriceForOrder.getInvoiceNum());
		invoice.setSalesTermRef(terms);
		invoice.setBalance(new BigDecimal(supplierPriceForOrder.getTempValue06()));
		List<Line> lineList = new ArrayList<Line>();
		List<AirticketItems> airticketItemsList = airticketItemsMapper.findByOrderId(supplierPriceForOrder.getSupplierPriceForOrderId());
		for(int i=0;i<airticketItemsList.size();i++){
			Line line = new Line();
			LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
			line.setDetailType(lineDetailTypeEnum);
			line.setLineNum(new BigInteger(String.valueOf(i+1)));
			line.setDescription(airticketItemsList.get(i).getTicketNo());
			SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
			salesItemLineDetail.setUnitPrice(airticketItemsList.get(i).getSelling());
			salesItemLineDetail.setQty(new BigDecimal(1));
			line.setAmount(airticketItemsList.get(i).getSelling());
			line.setSalesItemLineDetail(salesItemLineDetail);
			//line.set
			lineList.add(line);
		}
		invoice.setLine(lineList);
		return invoice;
	}
	
	/**
	 * 由机票产生CreditMemo
	 * @return
	 */
	private CreditMemo getCreditMemoFormFlit(SupplierPriceForOrder supplierPriceForOrder){
		CreditMemo creditMemo = new CreditMemo();
		creditMemo.setAutoDocNumber(false);
		creditMemo.setDocNumber(supplierPriceForOrder.getInvoiceNum());
		creditMemo.setBalance(new BigDecimal(supplierPriceForOrder.getTempValue06()).multiply(new BigDecimal(-1)));
		List<Line> lineList = new ArrayList<Line>();
		List<AirticketItems> airticketItemsList = airticketItemsMapper.findByOrderId(supplierPriceForOrder.getSupplierPriceForOrderId());
		for(int i=0;i<airticketItemsList.size();i++){
			Line line = new Line();
			LineDetailTypeEnum lineDetailTypeEnum = LineDetailTypeEnum.SALES_ITEM_LINE_DETAIL;
			line.setDetailType(lineDetailTypeEnum);
			line.setLineNum(new BigInteger(String.valueOf(i+1)));
			line.setDescription(airticketItemsList.get(i).getTicketNo());
			SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
			salesItemLineDetail.setUnitPrice(airticketItemsList.get(i).getSelling());
			salesItemLineDetail.setQty(new BigDecimal(1));
			line.setAmount(airticketItemsList.get(i).getSelling());
			line.setSalesItemLineDetail(salesItemLineDetail);
			lineList.add(line);
		}
		creditMemo.setLine(lineList);
		return creditMemo;
	}

	/**
	 * 机票部门操作时产生对航空公司的一笔bill
	 * @return
	 */
	private Bill getBillForArc(SupplierPriceForOrder supplierPriceForOrder,DataService dataService,ReferenceType referenceType){
		QueryResult venderResult;
		List<Vendor> vendorList = new ArrayList<Vendor>();
		ReferenceType vendor = new ReferenceType();
		Bill bill = new Bill();
		try {
			venderResult = dataService.executeQuery("SELECT Id FROM Vendor WHERE DisplayName='ARC'");
			vendorList = (List<Vendor>) venderResult.getEntities();
		
		vendor.setValue(vendorList.get(0).getId());
		vendor.setName(vendorList.get(0).getDisplayName());
		bill.setVendorRef(vendor);
		bill.setDocNumber(supplierPriceForOrder.getInvoiceNum());
		bill.setBalance(supplierPriceForOrder.getCharge());
		if(!supplierPriceForOrder.getTempValue03().equals("Search OrderNo")){
			bill.setMemo(supplierPriceForOrder.getTempValue03()+supplierPriceForOrder.getTempValue01());
		}
		List<Line> lineList = new ArrayList<Line>();
		List<AirticketItems> airticketItemsList = airticketItemsMapper.findByOrderId(supplierPriceForOrder.getSupplierPriceForOrderId());
		QueryResult referenceResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='Bank Charges'");
		List<Account> referenceList = (List<Account>) referenceResult.getEntities();
		int type  = supplierPriceForOrder.getType();
		List<CustomField> customFieldList = new ArrayList<CustomField>();
		CustomField customField = new CustomField();
		customField.setStringValue(referenceType.getValue());
		customFieldList.add(customField);
		for(int i=0;i<airticketItemsList.size();i++){
			Line line = new Line();
			line.setLineNum(new BigInteger(Integer.toString(i+1)));
			line.setAmount(airticketItemsList.get(0).getCharge());
			line.setDetailType(LineDetailTypeEnum.ACCOUNT_BASED_EXPENSE_LINE_DETAIL);
			line.setLineNum(new BigInteger(String.valueOf(1)));
			//line.setDescription("");
			AccountBasedExpenseLineDetail accountBasedExpenseLineDetail = new AccountBasedExpenseLineDetail();
			ReferenceType account = new ReferenceType();
			account.setValue(referenceList.get(0).getId());
			account.setName(referenceList.get(0).getName());
			accountBasedExpenseLineDetail.setAccountRef(account);
			line.setAccountBasedExpenseLineDetail(accountBasedExpenseLineDetail);
			line.setCustomField(customFieldList);
			lineList.add(line);
		}
		bill.setLine(lineList);
		dataService.add(bill);
		} catch (FMSException e) {
			e.printStackTrace();
		}
		return bill;
	}
	
	/**
	 * 添加customer时进行全qb插入
	 * @param vender
	 * @return
	 */
	public Boolean saveCustomer(Vender vender){
		
		return null;
	}
	
	
	/**
	 * 获取chinatour 的item
	 * @return
	 */
	private ReferenceType getItemType(DataService dataService){
		QueryResult salseItemResult;
		ReferenceType itemRef = new ReferenceType();
		try {
			salseItemResult = dataService.executeQuery("SELECT Id FROM Item WHERE Name='China Tour'");
			List<Item> itemList = (List<Item>) salseItemResult.getEntities();
			Item item = new Item();
			if(itemList==null||itemList.size()==0){
				item.setName("China Tour");
				dataService.add(item);
				salseItemResult = dataService.executeQuery("SELECT Id FROM Item WHERE Name='China Tour'");
				itemList = (List<Item>) salseItemResult.getEntities();
			}
			item =  itemList.get(0);
			itemRef.setValue(item.getId());
		} catch (FMSException e) {
			e.printStackTrace();
		}
		return itemRef;
	}
	
	/**
	 * 获取生成ap时产生对应的account
	 * @return
	 */
	/*private ReferenceType getAccount(DataService dataService){
		QueryResult salseItemResult;
		ReferenceType accountRef = new ReferenceType();
		String accountName = "Local Tour";
		if(accountName.equals(Constant.WEBSUPPLIER1)||accountName.equals(Constant.WEBSUPPLIER2)){
			accountName = "";
		}
		try {
			salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='"+accountName+"'");
			List<Account> accountList = (List<Account>) salseItemResult.getEntities();
			Account account = new Account();
			if(accountList==null||accountList.size()==0){
				account.setName("Local Tour");
				dataService.add(account);
				salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='"+accountName+"'");
				accountList = (List<Account>) salseItemResult.getEntities();
			}
			account =  accountList.get(0);
			accountRef.setValue(account.getId());
		} catch (FMSException e) {
			e.printStackTrace();
		}
		return accountRef;
	}*/
/*	*//**
	 * 获取生成ap时产生对应的account
	 * @return
	 */
	private ReferenceType getAccount(DataService dataService,String vendorName){
		QueryResult salseItemResult;
		ReferenceType accountRef = new ReferenceType();
		//String accountName = "Cost of Goods Sold:Local Tour";
		String accountName = "Local Tour";
		if(vendorName.equals(Constant.WEBSUPPLIER1)||vendorName.equals(Constant.WEBSUPPLIER2)){
			//accountName = "Sales Income:Credit Card Fee Charge";
			//accountName = "Credit Card Fee Charge";
			accountName = "Credit Card Fees";
		}else if(vendorName.equals(Constant.WEBSUPPLIER3)){
			//accountName = "Cost of Goods Sold:Visa & DHL";
			accountName = "Visa";
		}
		try {
			//salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE FullyQualifiedName='"+accountName+"'");
			salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='"+accountName+"'");
			List<Account> accountList = (List<Account>) salseItemResult.getEntities();
			Account account = new Account();
			if(accountList==null||accountList.size()==0){
				account.setFullyQualifiedName(accountName);
				dataService.add(account);
				//salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE FullyQualifiedName='"+accountName+"'");
				salseItemResult = dataService.executeQuery("SELECT Id FROM Account WHERE Name='"+accountName+"'");
				accountList = (List<Account>) salseItemResult.getEntities();
			}
			account =  accountList.get(0);
			accountRef.setValue(account.getId());
		} catch (FMSException e) {
			e.printStackTrace();
		}
		return accountRef;
	}

	@Override
	public Boolean createCustomer(Vender vendor) {
		Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
		Company company = companyMapper.findById(Constant.OLDCOMPANYID);
		if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
	    }
		DataService dataService= this.getDataService(company);
		Customer customer = new Customer();
		customer.setDisplayName(vendor.getName());
		customer.setContactName(vendor.getContactor());
		customer.setBalance(new BigDecimal(0.00));
		TelephoneNumber telNo = new TelephoneNumber();
		telNo.setFreeFormNumber(vendor.getTel());
		PhysicalAddress address = new PhysicalAddress();
		address.setCity(vendor.getCityId());
		address.setCountry(vendor.getCountryName());
		address.setLine1(vendor.getAddress());
		customer.setBillAddr(address);
		try {
			dataService.add(customer);
		} catch (FMSException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean createVendor(Vender vendorTemp) {
		Config.setProperty(Config.BASE_URL_QBO,Constant.BASE_URL_QBO);
		Company company = companyMapper.findById(Constant.OLDCOMPANYID);
		if (this.adminService.getCurrent().getDeptId().equals(Constant.WJOFFICE)) {
	       company = (Company)this.companyMapper.findById(Constant.NEWCOMPANYID);
	    }
		DataService dataService= this.getDataService(company);
		Vendor vendor = new Vendor();
		vendor.setDisplayName(vendorTemp.getName());
		vendor.setContactName(vendorTemp.getContactor());
		vendor.setBalance(new BigDecimal(0.00));
		TelephoneNumber telNo = new TelephoneNumber();
		telNo.setFreeFormNumber(vendorTemp.getTel());
		PhysicalAddress address = new PhysicalAddress();
		address.setCity(vendorTemp.getCityId());
		address.setCountry(vendorTemp.getCountryName());
		address.setLine1(vendorTemp.getAddress());
		vendor.setBillAddr(address);
		try {
			dataService.add(vendor);
		} catch (FMSException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private ReferenceType getClass(String agentName,String tourCode,DataService dataService){
		ReferenceType classType = new ReferenceType();
			QueryResult classResult;
			try {
				String cla = agentName+":"+tourCode;
				classResult = dataService.executeQuery("SELECT Id FROM Class WHERE FullyQualifiedName='"+cla+"'");
				List<com.intuit.ipp.data.Class> classList = (List<com.intuit.ipp.data.Class>) classResult.getEntities();
				com.intuit.ipp.data.Class clas = new com.intuit.ipp.data.Class();
				if(classList==null||classList.size()==0){
					clas.setName(cla);
					dataService.add(clas);
					classResult = dataService.executeQuery("SELECT Id FROM Class WHERE FullyQualifiedName='"+cla+"'");
					classList = (List<com.intuit.ipp.data.Class>) classResult.getEntities();
				}
				clas = classList.get(0);
				classType.setValue(clas.getId());
			} catch (FMSException e) {
				e.printStackTrace();
			}
		return classType;
	}
	
	private ReferenceType getClassForSingle(String agentName,String orderNo,DataService dataService){
		ReferenceType classType = new ReferenceType();
			QueryResult classResult;
			try {
				String cla = agentName+":"+orderNo;
				classResult = dataService.executeQuery("SELECT Id FROM Class WHERE Name='"+orderNo+"'");
				List<com.intuit.ipp.data.Class> classList = (List<com.intuit.ipp.data.Class>) classResult.getEntities();
				com.intuit.ipp.data.Class clas = new com.intuit.ipp.data.Class();
				if(classList==null||classList.size()==0){
					clas.setName(cla);
					dataService.add(clas);
					classResult = dataService.executeQuery("SELECT Id FROM Class WHERE Name='"+orderNo+"'");
					classList = (List<com.intuit.ipp.data.Class>) classResult.getEntities();
				}
				clas = classList.get(0);
				classType.setValue(clas.getId());
			} catch (FMSException e) {
				e.printStackTrace();
			}
		return classType;
	}
	
	private List<Line> getLine(String orderNo,DataService dataService,BigDecimal amount){
		QueryResult invoiceResult;
		List<Line> lineList = new ArrayList<Line>();
		Line line = new Line();
		line.setAmount(amount);
		LinkedTxn linkedTxn = new LinkedTxn();
		List<LinkedTxn> linkedTxnList = new ArrayList<LinkedTxn>();
		try {
			invoiceResult = dataService.executeQuery("SELECT Id FROM Invoice WHERE DocNumber='"+orderNo+"'");
			Invoice invoice = (Invoice) invoiceResult.getEntities().get(0);
			if(invoice!=null){
				linkedTxn.setTxnId(invoice.getId());
				linkedTxn.setTxnType("Invoice");
			}else{
				invoiceResult = dataService.executeQuery("SELECT Id FROM CreditMemo WHERE DocNumber='"+orderNo+"'");
				CreditMemo CreditMemo = (com.intuit.ipp.data.CreditMemo) invoiceResult.getEntities().get(0);
				linkedTxn.setTxnId(CreditMemo.getId());
				linkedTxn.setTxnType("CreditMemo");
			}
				linkedTxnList.add(linkedTxn);
				line.setLinkedTxn(linkedTxnList);
				lineList.add(line);
		} catch (FMSException e) {
			e.printStackTrace();
		}
		return lineList;
	}
}
