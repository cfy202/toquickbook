<div class="cl-sidebar" data-position="right" data-step="1" data-intro="<strong>Fixed Sidebar</strong> <br/> It adjust to your needs." >
    <div class="cl-toggle"><i class="fa fa-bars"></i></div>
    <div class="cl-navblock">
        <div class="menu-space">
            <div class="content">
                <div class="side-user">
                    <div class="avatar"><img src="[@spring.url '/resources/images/userImage/'/][@shiro.principal property="userImage"/]-1.jpg" alt="Avatar" /></div>
                    <div class="info">
                        <a href="#">[@shiro.principal /]</a>
                        <img src="[@spring.url '/resources/images/state_online.png'/]" alt="Status" /> <span>Online</span>
                    </div>
                </div>
                <ul class="cl-vnavigation">
					<li><a href="#"><i class="fa fa-list-alt"></i><span>Booking</span></a>
						<ul class="sub-menu">
							<li[#if menuId=="310"] class="active"[/#if]><a href="[@spring.url '/admin/orders/itemOfficeList.jhtml'/]" title="Office">Item List(new)</a></li>
							<li[#if menuId=="311"] class="active"[/#if]><a href="[@spring.url '/admin/orders/itemOfficeListForEdit.jhtml'/]" title="Office">Item List(edit)</a></li>
							<li[#if menuId=="312"] class="active"[/#if]><a href="[@spring.url '/admin/orders/itemOfficeListForTour.jhtml'/]" title="Office">Item List(tour)</a></li>
						</ul>
					</li>
					<li><a href="#"><i class="fa fa-money"></i><span>Accounting</span></a>
						<ul class="sub-menu">
							<li[#if menuId=="503"] class="active"[/#if]><a href="[@spring.url '/admin/payCostRecords/list.jhtml'/]">Order Receivable</a></li>
						</ul>
					</li>
					<li><a href="#"><i class="fa fa-list-alt"></i><span>Customer/Vendor</span></a>
						<ul class="sub-menu">
							<li[#if menuId=="809"] class="active"[/#if]><a href="[@spring.url '/admin/vender/customerList.jhtml'/]">Customer</a></li>
							<li[#if menuId=="810"] class="active"[/#if]><a href="[@spring.url '/admin/vender/vendorList.jhtml'/]">Vendor</a></li>
						</ul>
					</li>
                </ul>
            </div>
        </div>
        <div class="text-right collapse-button" style="padding:7px 9px;">
            <!-- <input type="text" class="form-control search" placeholder="Search..." /> -->
            <button id="sidebar-collapse" class="btn btn-default" style=""><i style="color:#fff;" class="fa fa-angle-left"></i></button>
        </div>
    </div>
</div>