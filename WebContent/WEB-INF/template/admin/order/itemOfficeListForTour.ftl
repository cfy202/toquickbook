[#assign shiro=JspTaglibs["/WEB-INF/tld/shiro.tld"] /]
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="[@spring.url '/resources/images/favicon.png'/]">
    <title>${message("admin.main.title")}</title>
[#include "/admin/include/head.ftl"]
</head>
<body>

<!-- Fixed navbar -->
[#include "/admin/include/navbar.ftl"]
<div id="cl-wrapper" class="fixed-menu">
[#include "/admin/include/sidebar.ftl"]
<!-- Modal -->
<div class="modal fade" id="confirm-delete" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
            	 <div class="text-center">
                    <h4><i class="fa fa-warning"></i> Warning !</h4>
                    <p>Booking will be Cancel ?</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <a style="cursor:pointer;" href="#" class="btn btn-danger">Yes</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="confirm-recover" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="text-center">
                    <h4><i class="fa fa-warning"></i>Warning !</h4>
                    <p>Booking will be Recover ?</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <a style="cursor:pointer;" href="#" class="btn btn-danger">Yes</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- Modal -->
<div class="modal fade" id="confirm-order" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="text-center">
                    <h4><i class="fa fa-warning"></i>Cancell Booking!</h4>
					<div class="col-sm-6">
	                  <label class="radio-inline"> <input type="radio" checked="" name="tax" class="icheck" value="1">Unsettled</label> 
	                  <label class="radio-inline"> <input type="radio" name="tax" class="icheck" value="2">Settled</label> 
	                </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <a style="cursor:pointer;" href="#" class="btn btn-danger" >Yes</a>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<div class="md-overlay"></div>
    <div class="container-fluid" id="pcont">
        <div class="page-head">
            <h3>Item List(tour)</h3>
            <div class="new"><a style="cursor:pointer;" href="javascript:void(0);"  class="md-trigger" data-modal="form-primary"></a><button class="btn btn-success" type="button" id="synForNotButton">&nbsp;&nbsp;Not to synchronous &nbsp;&nbsp;</button></div>
            <div class="new"><a style="cursor:pointer;" href="javascript:void(0);"  class="md-trigger" data-modal="form-primary"></a><button class="btn btn-success" type="button" id="synchronousButton">&nbsp;&nbsp;Synchronous &nbsp;&nbsp;</button></div>
            <ol class="breadcrumb">
                <li><a style="cursor:pointer;" href="../../">Home</a></li>
                <li><a style="cursor:pointer;" href="">Booking</a></li>
            </ol>
        </div>
        <div class="cl-mcont">
            <div class="row">
                <div class="col-md-12">
                    <div class="block-flat">
                        <div class="header">
							<h4 class="filter-bar"><i class="fa fa-filter"></i> Filter</h4>
							<div class="pull-right pull-cursor" title="Filter">
								<i id="filter" class="fa fa-angle-down"></i>&nbsp;&nbsp; 
							</div>
							<div class="options" style="margin:10px; padding:5px 0;">
									<div  class="nav-panel">
										<div  class="nav-block">
											<div  class="block-head">
												<span  class="nav-title">Booking Status</span>:
											</div>
											<div  class="block-body default-4-line">
												<div  class="params-cont"> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item" > 
														<span  class="unchecked"  name="stateSpan" checked="false" onclick="change(this,0);">NEW</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,2);" >Composed</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,3);" >Update</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,4);" >Cancelling</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,5);" >Cancelled</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,6);" >Cacelling by Agent</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="stateSpan" checked="false" onclick="change(this,7);" >Recovering</span>
													</a> 
													<input type="hidden" id="stateSpan"/>
												</div>
											</div>
										</div>
										<div  class="nav-block">
											<div  class="block-head">
												<span  class="nav-title">Settlement Status</span>:
											</div>
											<div  class="block-body default-4-line">
												<div  class="params-cont"> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item" > 
														<span  class="unchecked"  name="taxSpan" checked="false" onclick="change(this,0);">Unsettled</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="taxSpan" checked="false" onclick="change(this,3);" >Settling</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="taxSpan" checked="false" onclick="change(this,4);" >Settled</span> 
													</a> 
													<input type="hidden" id="taxSpan"/>
												</div>
											</div>
										</div>
										<div  class="nav-block">
											<div  class="block-head">
												<span  class="nav-title">Accountant Auditing</span>:
											</div>
											<div  class="block-body default-4-line">
												<div  class="params-cont"> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item" > 
														<span  class="unchecked"  name="payStateSpan" checked="false" onclick="change(this,0);">New</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="payStateSpan" checked="false" onclick="change(this,1);" >Approved</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="payStateSpan" checked="false" onclick="change(this,2);" >DisApproved</span> 
													</a> 
													<input type="hidden" id="payStateSpan"/>
												</div>
											</div>
										</div>
										<div  class="nav-block">
											<div  class="block-head">
												<span  class="nav-title">Accountant View Changes</span>:
											</div>
											<div  class="block-body default-4-line">
												<div  class="params-cont"> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item" > 
														<span  class="unchecked"  name="warnStateSpan" checked="false" onclick="change(this,0);">Unrevised</span> 
													</a>
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="warnStateSpan" checked="false" onclick="change(this,1);" >Checked</span> 
													</a> 
													<a style="cursor:pointer;"  target="_self"  class=" param-item " > 
														<span  class="unchecked" name="warnStateSpan" checked="false" onclick="change(this,2);" >Unchecked</span> 
													</a> 
													<input type="hidden" id="warnStateSpan"/>
												</div>
											</div>
										</div>
										<div class="nav-block">
											<div class="block-head">
												<span class="nav-title">Arrival Date</span>:
											</div>
											<div class="block-body default-4-line pull-cursor">
												<div class="params-cont">
													<input type="text" id="search_beginningDate" size="14"  placeholder="Beginning Date." />
													<input type="text" id="search_endingDate" size="14"  placeholder="Ending Date." />
													&nbsp;<i id="clearDate" class="fa fa-rotate-left" title="Clear Date"></i>
												</div>
											</div>
										</div>
										<div class="nav-block">
											<div class="block-head">
												<span class="nav-title">Booking Date</span>:
											</div>
											<div class="block-body default-4-line pull-cursor">
												<div class="params-cont">
													<input type="text" id="booking_beginningDate" size="14"  placeholder="Beginning Date." />
													<input type="text" id="booking_endingDate" size="14"  placeholder="Ending Date." />
													&nbsp;<i id="clearDate2" class="fa fa-rotate-left" title="Clear Date"></i>
												</div>
											</div>
										</div>
										<div  class="nav-block">
											<div  class="block-head">
												<span  class="nav-title">Others</span>:
											</div>
											<div  class="block-body default-2-line">
												<div  class="params-cont"> 
													<input type="text" size="14" id="search_orderNo" placeholder="orderNo..." />
													<input type="text" size="14" id="search_tourCode" placeholder="tourCode..." />
													<input type="text" size="14" id="search_agent" placeholder="agent..." />
													<input type="text" size="14" id="search_peer" placeholder="Peer..." />
												</div>
											</div>
										</div>
									</div>
									<div  class="nav-panel">
										<div class="btn-cont">
											<input class="submit-btn"  type="submit" id="subId" value="Search">
										</div>
									</div>
							</div>
						</div>
                        <div class="content">
                            <div class="table-responsive">
                                <table class="table table-bordered" id="datatable2">
                                    <thead>
	                                    <tr>
											<th><img class="toggle-details" src="[@spring.url '/resources/images/plus.png'/]" /></th>
											<th><input id="check-all" type="checkbox"></th>
											<th>Booking No. </th>
											<th>Tour Code</th>
											<th>Arrival Date</th>
											<th>Booking Date </th>
											<th>Total Amount</th>
											<th>Agent</th>
											<th>Status</th>
											<th>Action</th>
	                                    </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="md-modal colored-header custom-width md-effect-9" id="form-primary">
	<div class="md-content">
		<div class="modal-header">
			<h3>Invoice</h3>
			<button type="button" id="closeButton" class="close md-close" data-dismiss="modal" aria-hidden="true">&times;</button>
		</div>
		<form id="sysForm" method="post" action="[@spring.url '/admin/orders/synchronousForTour.jhtml'/]">
			<div class="modal-body form">
				<div class="text-center">
					<h4>Are you sure to synchronous?</h4>
					<div class="text-center"id="centerId">
						<input name="orderIds" type="hidden" value="" id="orderIdForSy">
						<input name="type" type="hidden" value="0" id="typeForSy">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default btn-flat md-close" data-dismiss="modal">Cancel</button>
				<button type="button" id="synButton" class="btn btn-primary">Synchronous</button>
				<div style="display:none;"><button type="button" id="btn" class="btn btn-primary btn-flat md-trigger" data-toggle="modal" data-modal="form-primary"></button></div>
			</div>
		</form>
	</div>
</div>
<!-- /.modal -->
<!-- Modal -->
<div class="md-modal colored-header custom-width md-effect-9" id="form-primary1">
	<div class="md-content">
		<div class="modal-header">
			<h3>Invoice</h3>
			<button type="button" id="closeForOneButton" class="close md-close" data-dismiss="modal" aria-hidden="true">&times;</button>
		</div>
		<form id="sysForOneForm" method="post" action="[@spring.url '/admin/orders/synchronousForTour.jhtml'/]">
			<div class="modal-body form">
				<div class="text-center">
					<h4>Are you sure to synchronous?</h4>
					<div class="text-center"id="centerIdForOne">
						<input name="orderIds" type="hidden" value="" id="orderIdForOne">
						<input name="type" type="hidden" value="0" id="typeForOne">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default btn-flat md-close" data-dismiss="modal">Cancel</button>
				<button type="button" id="synForOneButton" class="btn btn-primary">Synchronous</button>
				<button type="button" id="synOneForNotButton" class="btn btn-primary">Not to synchronous</button>
				<div style="display:none;"><button type="button" id="btnForOne" class="btn btn-primary btn-flat md-trigger" data-toggle="modal" data-modal="form-primary1"></button></div>
			</div>
		</form>
	</div>
</div>
<!-- /.modal -->
<div class="md-overlay"></div>
[#include "/admin/include/foot.ftl"]
<script type="text/javascript">
    $(document).ready(function () {
        //initialize the javascript
        App.init();
        [@flash_message /]
        
         $("#datatable2").attr("width","100%");
        
        $("div.options").hide();//默认隐藏div，或者在样式表中添加.text{display:none}，推荐使用后者
		$("#filter").click(function(){
			$("div.options").slideToggle("slow");
			var _slide=$("#filter");
			if(_slide.attr('class')=="fa fa-angle-up"){
				_slide.removeClass("fa fa-angle-up").addClass("fa fa-angle-down");
			}else{
				_slide.removeClass("fa fa-angle-down").addClass("fa fa-angle-up");
			}
		});
		
        /*
         * Initialse DataTables, with no sorting on the 'details' column
         */
         
        /* Formating function for row details */
        function fnFormatDetails ( oTable, nTr )
        {	
            var aData = oTable.fnGetData(nTr);
            var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
            sOut += '<tr><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Peer: '+aData.peerName+'</td><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Total Amount : '+aData.sumFee+'</td></tr>';
            sOut += '<tr><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Status : ';
	        	if(aData.state==0){
					sOut += '<span class="color-danger">NEW</span>';
				}else if(aData.state==2){
					sOut += '<span class="color-success">COMPOSED</span>';
				}else if(aData.state==3){
					sOut += '<span class="color-primary">UPDATE</span>';
				}else if(aData.state==4){
					sOut += '<span class="color-danger">CACELLING</span>';
				}else if(aData.state==5){
					sOut += '<span class="color-warning">CANCELLED</span>';
				}else if(aData.state==6){
					sOut += '<span class="color-warning">CANCELLED</span>';
				}else if(aData.state==7){
					sOut += '<span class="color-danger">RECOVERING</span>';
				}
			sOut += '</td><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Settlement Status : ';
				if(aData.tax==0){
					sOut +=  '<i title="Unsettled" class="fa fa-times"></i>';
				}else if(aData.tax==2){
					sOut +=  '<i title="Settled Not Sent 0.05Invoice" class="fa fa-check"></i>';
				}else if(aData.tax==3){
					sOut +=  '<i title="Settling" class="fa fa-clock-o"></i>';
				}else if(aData.tax==4){
					sOut +=  '<i title="Settled And Sent 0.05Invoice" class="fa fa-check"></i>';
				}
			sOut += '</td></tr>';
			sOut += '<tr><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Acc. Auditing : ';
				if(aData.payState==0){
					sOut +=  'New';
				}else if(aData.payState==1){
					sOut +=  'Approved';
				}else if(aData.payState==2){
					sOut +=  'Disapproved';
				}
			sOut += '</td><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Acc. View Changes : ';
				if(aData.warnState==0){
					sOut +=  'Unrevised';
				}else if(aData.warnState==1){
					sOut +=  'Checked';
				}else if(aData.warnState==2){
					sOut +=  'Unchecked';
				}
			sOut += '</td></tr>';
			sOut += '<tr><td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Product Name : '+aData.lineName+'</td>';
            sOut += '<td style="border-left: 1px solid #DADADA;border-bottom: 1px solid #dadada;">Product Code : '+aData.scheduleLineCode+'</td></tr>';
            sOut += '</table>';
            return sOut;
        }
        
        var oTable = $('#datatable2').dataTable({
            "processing": true,
            "serverSide": true,
            "bFilter":false,
            "bSort":false,
            "aLengthMenu":[20,40,80,100],
            "ajax": {
                url: "[@spring.url '/admin/orders/itemOfficeListForTour.jhtml'/]",
                type: "POST",
                "data": function ( data ) {
					data.tourCode = $("#search_tourCode").val();
					data.orderNo = $("#search_orderNo").val();
					data.state = $("#stateSpan").val();
					data.tax = $("#taxSpan").val();
					data.payState = $("#payStateSpan").val();
					data.warnState = $("#warnStateSpan").val();
					data.beginningDate = $("#search_beginningDate").val();
					data.endingDate = $("#search_endingDate").val();
					data.bookingBeginningDate = $("#booking_beginningDate").val();
					data.bookingEndingDate = $("#booking_endingDate").val();
					data.userName = $("#search_agent").val();
					data.peerId = $("#search_peer").val();
				}
            },
            "columns": [
                 { "data": "id" },
                { "data": "id",
                  "render": function (data, type, row) {
                  		if(row.ifTourSuc==0&&row.tourCode!=''){	
	                     	return '<div class="radio"><input type="checkbox"  name="orderIds" value="'+data+'" class="icheck"/></div>';
	                     }else{
	                     	return '<div class="radio"><input type="checkbox"  name="orderIds" value="'+data+'" class="icheck" disabled="disabled"/></div>';
	                     }
                   } },
                { "data": "orderNo" },
                { "data": "tourCode" },
                { "data": "scheduleOfArriveTime" },
                { "data": "createDate" },
                { "data": "sumFee" },
                { "data": "userName"},
                { "data": "ifTourSuc",
                	"render" : function(data, type, row) {
					  	switch(data){
					  		case 0 : return '<i title="New" class="fa fa-clock-o"></i>';
					  		break;
					  		case 1 : return '<i title="Approved" class="fa fa-check"></i>'; 
					  		break;
					  		case 2 : return '<i title="DisApproved" class="fa fa-times"></i>';
					  		break;
					  		default: return '';
					  	}
					}},
                { "data": "id" }
            ],
            "columnDefs" : [ {
				"render" : function(data, type, row) {
						if(row.ifTourSuc==0&&row.tourCode!=''){
							return '<a style="cursor:pointer;" href="javascript:confirmUp(\''+data+'\');">Synchronous</a>';
						}else{
							return 'Synchronous';
						}
                 },
				"targets" : 9
		   }],
		    "fnCreatedRow": function( nRow, aData, iDataIndex ) { 
         		$('td:eq(0)', nRow).html( '<img class="toggle-details" src="[@spring.url '/resources/images/plus.png'/]" />' );
           },
		   "order": [[ 1, 'asc' ]]
        });
        
        $('#datatable2').delegate('tbody td img','click', function () {
            var nTr = $(this).parents('tr')[0];
            
            if ( oTable.fnIsOpen(nTr) )
            {
                /* This row is already open - close it */
                this.src = "[@spring.url '/resources/images/plus.png'/]";
                oTable.fnClose( nTr );
            }
            else
            {
                /* Open this row */
                this.src = "[@spring.url '/resources/images/minus.png'/]";
                oTable.fnOpen( nTr, fnFormatDetails(oTable, nTr), 'details' );
            }
        });
        
        $('#confirm-delete').on('show.bs.modal', function (e) {
            $(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
        });
        
        $('#confirm-recover').on('show.bs.modal', function (e) {
            $(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href'));
        });
        
        $('#confirm-order').on('show.bs.modal', function (e) {
        	var tax=$("input[name='tax']:checked").val();
            $(this).find('.btn-danger').attr('href', $(e.relatedTarget).data('href')+"?tex="+tax);
        });
    });
    
		$("#subId").on( 'click', function () {
			$('#datatable2').DataTable().draw();
		} );
		
		var obj={
		 colorSpan:"",
		};
		function change(span,value)
		{
			//给所以span的属性初始化
		    $('span[name="'+$(span).attr('name')+'"]').each(function(){
		             if(this.checked&&this!=span)
		          {
		                this.className="unchecked";
		                this.checked=false;
		          }               
		    });
		    //判断是否有选中   是 初始化取消加粗   否加粗
		  	if(span.checked&&span.className=="checked"){
			    span.className="unchecked";
			    span.checked=false;
			    $("#"+$(span).attr('name')+"").val("");
		  	}else{
		  		obj[$(span).attr('name')]=span.innerHTML;
			    span.className="checked";
			    span.checked=true;
			    $("#"+$(span).attr('name')+"").val(value);
		  	}
		}
		$("#clearDate").click(function(){
			$("#search_beginningDate").val('');
 			$("#search_endingDate").val('');
		});
		
		$("#search_beginningDate").datepicker({dateFormat: 'yy-mm-dd',changeYear: true,changeMonth: true });
		$("#search_endingDate").datepicker({dateFormat: 'yy-mm-dd',changeYear: true,changeMonth: true });
		
		$("#clearDate2").click(function(){
			$("#booking_beginningDate").val('');
 			$("#booking_endingDate").val('');
		});
		
		$("#booking_beginningDate").datepicker({dateFormat: 'yy-mm-dd',changeYear: true,changeMonth: true });
		$("#booking_endingDate").datepicker({dateFormat: 'yy-mm-dd',changeYear: true,changeMonth: true });
		
		 //全选或不全选
     $("#check-all").on('change',function(){
        var checkboxes = $(".radio").find(':checkbox:enabled');
        
        if($(this).is(':checked')) {
            checkboxes.iCheck('check');
        } else {
            checkboxes.iCheck('uncheck');
        }
  	 });
  	 
  	  $("#synchronousButton").click(function(){
  	 	var n = $("input:checkbox:checked").length;
  	 	var orderIds = "";
  	 	if(n==0){
  	 		alert("please select booking");
  	 	}else{
  	 		$("input[name='orderIds']:checkbox:checked").each(function(){
  	 			orderIds += $(this).val()+",";
  	 		});
  	 		orderIds = orderIds.substring(orderIds,orderIds.length-1);
  	 		$("#orderIdForSy").val(orderIds);
  	 		$("#btn").trigger("click");
  	 	}
  	 });
  	 
  	 $("#synForNotButton").click(function(){
  	 	var n = $("input:checkbox:checked").length;
  	 	var orderIds = "";
  	 	if(n==0){
  	 		alert("please select booking");
  	 	}else{
  	 		$("input[name='orderIds']:checkbox:checked").each(function(){
  	 			orderIds += $(this).val()+",";
  	 		});
  	 		orderIds = orderIds.substring(orderIds,orderIds.length-1);
  	 		$("#orderIdForSy").val(orderIds);
  	 		$("#typeForSy").val(1);
  	 		$("#btn").trigger("click");
  	 	}
  	 });
  	 
  	 $("#synButton").click(function(){
		$("#sysForm").submit();
		$(this).hide();
		$("#closeButton").click();
	});
	
	 function confirmUp(data){
  	 	$("#orderIdForOne").val(data);
  	 	$("#btnForOne").trigger("click");
	}
	
	$("#synForOneButton").click(function(){
		$("#sysForOneForm").submit();
		$(this).hide();
		$("#closeForOneButton").click();
	});
	$("#synOneForNotButton").click(function(){
		$("#typeForOne").val(1);
		$("#sysForOneForm").submit();
		$(this).hide();
		$("#closeForOneButton").click();
	});
</script>
</body>
</html>
