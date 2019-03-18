<!-- Fixed navbar -->
<div id="head-nav" class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="fa fa-gear"></span>
            </button>
            <a class="navbar-brand" href="#"><span>&nbsp;</span></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="active" title="Home"><a href="[@spring.url '/admin/'/]" style="padding-bottom: 11px;padding-top: 11px;"><i class="fa fa-home fa-2x"></i></a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right user-nav">
                <li class="dropdown profile_menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><img alt="Avatar" src="[@spring.url '/resources/images/userImage/'/][@shiro.principal property="userImage"/]-2.jpg" /><span> [@shiro.principal property="username" /]</span> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="[@spring.url '/admin/admin/editUser.jhtml'/]">My Account</a></li>
                        <li><a href="[@spring.url '/admin/admin/editPassword.jhtml'/]">Password</a></li>
                        <li class="divider"></li>
                        <li><a href="[@spring.url '/admin/logout.jsp'/]">Sign Out</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>