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
<div class="container-fluid" id="pcont">
<div class="cl-mcont">
<div class="stats_bar">
	<input type="hidden" value="[@shiro.principal property="type" /]" id="userRole">
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Booking</h2><span id="month_clientes">0</span></div>
        <div class="stat">
        	<div><span class="spk1">
        		<canvas style="display: inline-block; width: 74px; height: 16px; vertical-align: top;" width="74" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Monthly Subtotal</span>
        </div>
    </div>
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Booking</h2><span id="total_clientes">0</span></div>
        <div class="stat">
        	<div><span class="spk2">
        		<canvas style="display: inline-block; width: 74px; height: 16px; vertical-align: center;" width="74" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Yearly Total</span></div>
    </div>
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Arrival</h2><span id="month_arrival">0</span></div>
        <div class="stat">
        	<div><span class="spk3">
        		<canvas style="display: inline-block; width: 80px; height: 16px; vertical-align: center;" width="80" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Monthly Subtotal</span></div>
    </div>
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Arrival</h2><span id="total_arrival">0</span></div>
        <div class="stat">
        	<div><span class="spk4">
        		<canvas style="display: inline-block; width: 80px; height: 16px; vertical-align: center;" width="80" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Yearly Total</span></div>
    </div>
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Sales</h2><span id="month_sumFee">0</span></div>
        <div class="stat">
        	<div><span class="spk5">
        		<canvas style="display: inline-block; width: 80px; height: 16px; vertical-align: center;" width="80" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Monthly Subtotal</span></div>
    </div>
    <div class="butpro butstyle flat">
        <div class="sub"><h2>Sales</h2><span id="total_sumFee">0</span></div>
        <div class="stat">
        	<div><span class="spk6">
        		<canvas style="display: inline-block; width: 80px; height: 16px; vertical-align: center;" width="80" height="16"></canvas>
        	</span></div>
        	<span style="font-size:12px;"> Yearly Total</span></div>
    </div>

</div>

 <!-- Nifty Modal -->
    <div class="md-modal md-effect-1 colored-header custom-width" id="md-scale" style="width: 60%; perspective: none;height: 50%;">
    	[#list newsList as news]
        <div class="md-content" style="height: 100%;display:none;"  id="${news.newsId}">
          <div class="modal-header">
          	<h3>${news.title}</h3>
            <button type="button" class="close md-close" data-dismiss="modal" aria-hidden="true">&times;</button>
          </div>
          <div class="modal-body">
            <div style="text-alignleft;padding:0 30px;">
              <p>${news.content}</p>
            </div>
          </div>
        </div>
        [/#list]
    </div>
 <!--end Nifty Modal -->
<button class="btn btn-primary btn-flat md-trigger"  style="display:none" id="buttonId" data-modal="md-scale"> Fade in & Scale</button>
<button class="btn btn-primary btn-flat md-trigger"  style="display:none" id="roomId" data-modal="md-room"></button>
<div class="row dash-cols">

    <div class="col-sm-6 col-md-6">
    
        <ul class="timeline">
        <li>
                <i class="fa fa-comment green"></i>
                <span class="date">05-21</span>
                <div class="content">
                	<i class="fa fa-paperclip pull-right"></i>
                    <p class="news-p" title="关于系统汇率变更通知  ">
                    	<a style="color:red;">关于系统汇率变更通知,6月1日开始执行 </a>
                    </p>
                	<table>
                		<tr>
		                    	<td>
									<div>
										<p style="margin-left:60px;margin-top:6px;">USD : CNY&nbsp;&nbsp;&nbsp;&nbsp;  1 : 6.55</p>
										<p style="margin-left:60px;margin-top:6px;">CAD : CNY&nbsp;&nbsp;&nbsp;&nbsp;  1 : 5.00</p>
										<p style="margin-left:60px;margin-top:6px;">AUD : CNY&nbsp;&nbsp;&nbsp;&nbsp;  1 : 4.70</p>
										<p style="margin-left:60px;margin-top:6px;">EUR : CNY&nbsp;&nbsp;&nbsp;&nbsp;  1 : 7.30</p>
										<p style="margin-left:60px;margin-top:6px;">GBP : CNY&nbsp;&nbsp;&nbsp;&nbsp;  1 : 9.40</p>
										<p style="margin-left:60px;margin-top:6px;">USD : AUD&nbsp;&nbsp;&nbsp;&nbsp;  1 : 1.40</p>
										<p style="margin-left:60px;margin-top:6px;">USD : CAD&nbsp;&nbsp;&nbsp;&nbsp;  1 : 1.30</p>
										<p style="margin-left:60px;margin-top:6px;">USD : EUR&nbsp;&nbsp;&nbsp;&nbsp;  1 : 0.90</p>
										<p style="margin-left:60px;margin-top:6px;">USD : GBP&nbsp;&nbsp;&nbsp;&nbsp;  1 : 0.70</p>
									</div>
								</td>
							</tr>
                	</table>
                </div>
            </li>
	  [#list newsList as news]
	   	 <li>
	            <i class="fa fa-comment"></i>
	            <span class="date">${news.createDateStr}</span>
	            <div class="content">
	            	<i class="fa fa-paperclip pull-right"></i>
	                <p class="news-p" title="${news.title}">
	                	<a href="javascript:show('${news.newsId}')">${news.title}</a>
	                </p>
	                [#if news.image!=""]
	                <a class="image-zoom" href="[@spring.url '${news.image}'/]"><img src="[@spring.url '${news.image}'/]" class="img-thumbnail" width="730" height="414"/></a>
	                <blockquote>
	                    <p>
	                    	<a href="javascript:show('${news.newsId}')">Details</a>
	                    </p>
	                </blockquote>
	                [#else]
	                <div style="text-align: left;height:42px;overflow: hidden;-webkit-line-clamp: 2;margin-left:30px;">${news.content}</div></br></br>
	        		<blockquote>
	                    <p>
	                    	<a href="javascript:show('${news.newsId}')">Details</a>
	                    </p>
	                </blockquote>
					[/#if]
	            </div>
	        </li>
       [/#list]
   	  <!-- <li>
                <i class="fa fa-comment green"></i>
                <span class="date">05 Nov</span>
                <div class="content">
                	<i class="fa fa-paperclip pull-right"></i>
                    <p class="news-p" title="关于系统汇率变更通知  ">
                    	<a target="blank" style="color:red;">关于系统汇率变更通知,11月5日开始执行</a>
                    </p>
                	<table>
                		<tr>
		                    	<td>
									<div>
										<p style="margin-left:30px;margin-top:10px;font-weight:bold;">变更前汇率:</p>
										<p style="margin-left:60px;margin-top:6px;">EUR : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:7.2</p>
									</div>
								</td>
								<td>	
									<div>
										<p style="margin-left:30px;margin-top:10px;font-weight:bold;">变更后汇率:</p>
										<p style="margin-left:60px;margin-top:6px;">EUR : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:6.8</p>
											
									</div>
								</td>
							</tr>
                	</table>
                </div>
            </li>
        	<li>
                <i class="fa fa-comment"></i>
                <span class="date">26 Aug</span>
                <div class="content">
                	<i class="fa fa-paperclip pull-right"></i>
                    <p class="news-p" title="关于系统汇率变更通知  ">
                    	<a target="blank" style="color:red;">关于系统汇率变更通知,9月1日开始执行 </a>
                    </p>
                    	<table>
                    		<tr style="background-color:#ccc;">
                    			<td>
                    				<div>
	                    				<p style="margin-left:60px;margin-top:6px;">1USD : 6.3CNY</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1USD : 0.87 EUR</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1USD : 1.33 CAD</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1USD : 1.40AUD</p>
                    				</div>
                    			</td>
                    			<td>
                    				<div>
	                    				<p style="margin-left:60px;margin-top:6px;">1EUR : 7.2CNY</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1EUR : 1.14 USD</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1EUR : 1.53CAD</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1EUR : 1.59AUS</p>
                    				</div>
                    			</td>
                    		</tr>
                    		<tr style="background-color:#ccc;">
                    			<td>
                    				<div>
	                    				<p style="margin-left:60px;margin-top:6px;">1CAD : 4.7CNY</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1CAD : 0.75USD</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1CAD : 0.66EUR</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1CAD : 1.05 AUS</p>
                    				</div>
                    			</td>
                    			<td>
                    				<div>
	                    				<p style="margin-left:60px;margin-top:6px;">1AUD : 4.6CNY</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1AUD : 0.96CAD</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1AUD : 0.63EUR</p>
	                    				<p style="margin-left:60px;margin-top:6px;">1AUD : 0.72USD</p>
                    				</div>
                    			</td>
                    		</tr>
	                    	
						</table>
                </div>
            </li>
        	<li>
                <i class="fa fa-comment green"></i>
                <span class="date">20 Jun</span>
                <div class="content">
                	<i class="fa fa-paperclip pull-right"></i>
                    <p class="news-p" title="关于系统汇率变更通知  ">
                    	<a target="blank" style="color:red;">关于系统汇率变更通知</a>
                    </p>
                	<table>
                		<tr style="background-color:#ccc;">
		                    	<td>
									<div>
										<p style="margin-left:30px;margin-top:10px;font-weight:bold;">变更前汇率:</p>
										<p style="margin-left:60px;margin-top:6px;">USD : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:6.15</p>
										<p style="margin-left:60px;margin-top:6px;">USD : CAD&nbsp;&nbsp;&nbsp;&nbsp;  1:1.05</p>
										<p style="margin-left:60px;margin-top:6px;">CAD : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:5.2</p>	
									</div>
								</td>
								<td>	
									<div>
										<p style="margin-left:30px;margin-top:10px;font-weight:bold;">变更后汇率:</p>
										<p style="margin-left:60px;margin-top:6px;">USD : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:6.15</p>
										<p style="margin-left:60px;margin-top:6px;">USD : CAD&nbsp;&nbsp;&nbsp;&nbsp;  1:1.2</p>
										<p style="margin-left:60px;margin-top:6px;">CAD : RMB&nbsp;&nbsp;&nbsp;&nbsp;  1:5.05</p>	
									</div>
								</td>
							</tr>
                	</table>
                </div>
            </li>
        	<li>
                <i class="fa fa-comment"></i>
                <span class="date">03 Feb</span>
                <div class="content">
                	<i class="fa fa-paperclip pull-right"></i>
                    <p class="news-p" title="文景假期2015洛杉磯同行答謝晚宴成功舉辦">
                    	<a target="blank" href="http://mp.weixin.qq.com/s?__biz=MzA5NzA2NjUwNA==&mid=203621962&idx=1&sn=decb2b59b7189c20562b225e0100f72b&scene=2&from=timeline&isappinstalled=0&key=e50c042086dc971709ce6b6d19da82a7650c90389797a2c20910c8d766c1ca1b1da31257afe01d8abc15151afc967f56&ascene=1&uin=ODI1MDUwMjgy&devicetype=Windows+7&version=61000707&pass_ticket=u%2F8NrrPTWASihDWR0DXtMII8npwQJccAG1Fmsaio8oMweoOCtrDSfmGTdA8eWbYB">文景假期2015洛杉磯同行答謝晚宴成功舉辦 </a>
                    </p>
                    <a class="image-zoom" href="[@spring.url '/resources/images/news/3_1.jpg'/]"><img src="[@spring.url '/resources/images/news/3_1.jpg'/]" class="img-thumbnail" /></a>

                   	<blockquote>
	                    <p>
	                    	<small>由美國文景假期主辦的“2015洛杉磯同行答謝晚宴”于2月1日晚，在LaPuente, Pacific Palms Resort舉行。文景假期的管理層，各大航空公司領導，晚宴贊助商，以及百餘位洛杉磯旅遊業界同行共聚一堂，慶祝中國農曆新年的到來...</br></br></small>
	                    	<a target="blank" href="http://mp.weixin.qq.com/s?__biz=MzA5NzA2NjUwNA==&mid=203621962&idx=1&sn=decb2b59b7189c20562b225e0100f72b&scene=2&from=timeline&isappinstalled=0&key=e50c042086dc971709ce6b6d19da82a7650c90389797a2c20910c8d766c1ca1b1da31257afe01d8abc15151afc967f56&ascene=1&uin=ODI1MDUwMjgy&devicetype=Windows+7&version=61000707&pass_ticket=u%2F8NrrPTWASihDWR0DXtMII8npwQJccAG1Fmsaio8oMweoOCtrDSfmGTdA8eWbYB">Details</a>
	                    </p>
                    </blockquote>
                </div>
            </li>-->
        </ul>

    </div>

    <div class="col-sm-6 col-md-6">

        <div class="block-flat">
            <div class="header">
                <h3>Company location</h3>
            </div>
            <div class="content" id="world-map" style="height: 300px;">
            </div>
        </div>
        <!--
		<div class="block-flat pie-widget">
			<div class="header">
                <h3>Media & Videos</h3>
            </div>
            
            <div class="video content">
				<div class="video-left">
			    	<a href="http://v.youku.com/v_show/id_XNDI3MDA0NTU2.html" target="blank">
			            <img src="[@spring.url '/resources/images/gallery/video1.jpg'/]" />
			            <div class="video-btn">
			                <img src="[@spring.url '/resources/images/gallery/video-btn.png'/]"/>
			            </div>
			        </a>
			    </div>
			    <div class="video-right-top">
			        <a href="http://v.youku.com/v_show/id_XODc3Nzg1Mzc2.html" target="blank">
			            <img src="[@spring.url '/resources/images/gallery/video2.jpg'/]" />
			            <div class="video-btn">
			                <img src="[@spring.url '/resources/images/gallery/video-btn.png'/]"/>
			            </div>
			        </a>
			    </div>
			    <div class="video-right-down">
			        <a href="http://v.youku.com/v_show/id_XODc3NzgzNTQ4.html" target="blank">
			            <img src="[@spring.url '/resources/images/gallery/video3.jpg'/]" />
			            <div class="video-btn">
			                <img src="[@spring.url '/resources/images/gallery/video-btn.png'/]"/>
			            </div>
			        </a>
			    </div>
			</div>
        </div>
		-->
    </div>

</div>


</div>
</div>

</div>

[#include "/admin/include/foot.ftl"]
<!-- <script src="http://html5media.googlecode.com/svn/trunk/src/html5media.min.js"></script>-->

<!-- Bootstrap core JavaScript
  ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->

<script type="text/javascript">
    /*Video Controls
    var video  = $('#video1').get(0);
     video.currentTime=0.03; 
    function formatTime(seconds) {
      seconds = Math.round(seconds);
      minutes = Math.floor(seconds / 60);
      minutes = (minutes >= 10) ? minutes : "0" + minutes;
      seconds = Math.floor(seconds % 60);
      seconds = (seconds >= 10) ? seconds : "0" + seconds;
      return minutes + ":" + seconds;
    }
    
    $("#play_video").click(function(){
      video.play();
    });
    
    $("#pause_video").click(function(){
      video.pause();
    });
    
    $('#video1').on('timeupdate', function() {
      var time = video.currentTime;
       $('#time_video').html(formatTime(time));
    });*/
    //获取两位小数点
	function changeTwoDecimal(x){
			var f_x = parseFloat(x);
			if (isNaN(f_x))
			{
				//alert('function:changeTwoDecimal->parameter error');
				return false;
			}
			var f_x = Math.round(x*100)/100;
		
			return f_x;
		}
	//显示新闻
	function show(id){
		$(".md-content").css("display","none")
		$("#"+id).show();
		$("#buttonId").trigger("click");
	}
  </script>
<script type="text/javascript">
    $(document).ready(function(){
        //initialize the javascript
        App.init();
        $("#roomId").trigger("click");
         //收客人数统计，发客人数统计
        $.ajax({
				type:"POST",
				url:"${base}/admin/statistical/getBooking.jhtml",
				dataType:"json",
				success:function(map) {
					$("#month_clientes").text(changeTwoDecimal(map.bookSum));
					$("#month_sumFee").text(changeTwoDecimal(map.sumMonth));
					$("#total_clientes").text(changeTwoDecimal(map.bookYearSum));
					$("#total_sumFee").text(changeTwoDecimal(map.bookSumFee));
					$("#month_arrival").text(changeTwoDecimal(map.arrivalP));
					$("#total_arrival").text(changeTwoDecimal(map.sumArrival));
				}
			});
        //App.dashBoard();        
         /*Sparklines*/
        $(".spk1").sparkline([2,4,3,6,7,5,8,9,4,2,6,8,8,9,10], { type: 'bar', width: '80px', barColor: '#60c060'});
        $(".spk2").sparkline([2,4,3,6,7,5,8,9,4,2,6,8,8,9,10], { type: 'bar', width: '80px', barColor: '#60c060'});
        $(".spk3").sparkline([4,6,7,7,4,3,2,1,4,4 ,5,6,5], { type: 'discrete', width: '80', lineColor: '#4A8CF7',thresholdValue: 4,thresholdColor: '#ff0000'});
        $(".spk4").sparkline([4,6,7,7,4,3,2,1,4,4 ,5,6,5], { type: 'discrete', width: '80', lineColor: '#4A8CF7',thresholdValue: 4,thresholdColor: '#ff0000'});
        $(".spk5").sparkline([5,6,7,9,9,5,3,2,2,4,6,7], {type: 'line', lineColor: '#258FEC',fillColor: '#4A8CF7',spotColor: false,width: '80px',minSpotColor: false, maxSpotColor: false,  highlightSpotColor: '#1e7ac6',highlightLineColor: '#1e7ac6'});
        $(".spk6").sparkline([5,6,7,9,9,5,3,2,2,4,6,7], {type: 'line', lineColor: '#258FEC',fillColor: '#4A8CF7',spotColor: false,width: '80px',minSpotColor: false, maxSpotColor: false,  highlightSpotColor: '#1e7ac6',highlightLineColor: '#1e7ac6'});

        //Maps 
        $('#world-map').vectorMap({
            map: 'world_mill_en',
            backgroundColor: 'transparent',
            regionStyle: {
                initial: {
                    fill: '#38c3c1',
                },
                hover: {
                    "fill-opacity": 0.8
                }
            },
            markerStyle:{
                initial:{
                    r: 4
                },
                hover: {
                    r: 6,
                    stroke: 'rgba(255,255,255,0.8)',
                    "stroke-width": 4
                }
            },
            markers: [
            	/*上海，无锡，南非 */
                {latLng: [49.282729, -123.120738], name: 'Vancouver  (+1 604-800-6411)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [43.653226, -79.383184], name: 'Toronto', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [39.442555, 115.845336], name: 'BeiJing  (+86 10-6520-6136)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [31.461467, 117.476806], name: 'SuZhou  (+86 512-6760-0096)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [34.610604, 109.116210], name: 'XiAn  (+86 29-8222-4852)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [51.507351, -0.127758], name: 'London', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [-33.867486, 151.206990], name: 'Sydney', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [40.416775, -3.703790], name: 'Madrid  (+34-9130-8869-5)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [34.052234, -118.243685], name: 'Los Angeles (+1 626-377-9888)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [37.774929, -122.419416], name: 'San Francisco  (+1 415-876-7888)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [40.712784, -74.0059413], name: 'New York  (+1 718-412-0111)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                {latLng: [41.878114, -87.629798], name: 'Chicago  (+1 312-854-2388)', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [31.230416, 121.473701], name: 'Shang Hai', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [31.49117, 120.31191], name: 'Wu Xi', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [-23.87741, 23.67105], name: 'Tshwane', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}},
                //{latLng: [38.90719, -77.03687], name: 'Washington', style: {fill: '#E44C34',stroke:'rgba(255,255,255,0.7)',"stroke-width": 3}}
            ]
        });

        /*Pie Chart*/
        var data = [
            { label: "Google", data: 50},
            { label: "Dribbble", data: 15},
            { label: "Twitter", data: 12},
            { label: "Youtube", data: 14},
            { label: "Microsoft", data: 14}
        ];

        $("table td .legend").each(function(){
            var el = $(this);
            var color = el.data("color");
            el.css("background",color);
        });

    });
</script>




</body>
</html>
