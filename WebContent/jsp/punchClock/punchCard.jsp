D<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%String path = request.getContextPath();String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";%>
<!DOCTYPE html>
<html lang="zh-tw">
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="inc/icon.ico" />
<link rel="mask-icon" type="" href="inc/svg.svg" color="#111" />
<title>Punch Clock</title>
<link rel="stylesheet" href="jsp/punchClock/css/normalize.css">
<link rel='stylesheet' href='jsp/punchClock/css/css.css'>
<link rel='stylesheet' href='jsp/punchClock/css/bootstrap.min.css'>
<style>
html, body {
	height: 100%;
}

body {
	background: #0f3854;
	background: radial-gradient(ellipse at center, #0a2e38 0%, #000000 70%);
	background-size: 100%;
}

p {
	margin: 0;
	padding: 0;
}

#clock {
	font-family: 'Share Tech Mono', monospace;
	color: #ffffff;
	text-align: center;
	position: absolute;
	left: 50%;
	top: 50%;
	-webkit-transform: translate(-50%, -50%);
	transform: translate(-50%, -50%);
	color: #daf6ff;
	text-shadow: 0 0 20px #0aafe6, 0 0 20px rgba(10, 175, 230, 0);
}

#clock .time {
	letter-spacing: 0.05em;
	font-size: 80px;
	padding: 5px 0;
}

#clock .date {
	letter-spacing: 0.1em;
	font-size: 24px;
	font-family: 'Microsoft JhengHei';
}

#clock .text {
	letter-spacing: 0.1em;
	font-size: 20px;
	padding: 20px 0 0;
	font-family: 'Microsoft JhengHei';
}












.alert-minimalist {
	background-color: rgb(241, 242, 240);
	border-color: rgba(149, 149, 149, 0.3);
	border-radius: 3px;
	color: rgb(149, 149, 149);
	padding: 10px;
}
.alert-minimalist > [data-notify="icon"] {
	height: 50px;
	margin-right: 12px;
}
.alert-minimalist > [data-notify="title"] {
	color: rgb(51, 51, 51);
	display: block;
	font-weight: bold;
	margin-bottom: 5px;
}
.alert-minimalist > [data-notify="message"] {
	font-size: 80%;
}


</style>
<script>
window.console = window.console || function(t) {
	};

if (document.location.search.match(/type=embed/gi)) {
	window.parent.postMessage("resize", "*");
}
</script>
</head>
<body translate="no">
	<div id="clock">
		<p class="date">{{ date }}</p>
		<p class="time">{{ time }}</p>
		<p class="text">中華科技大學</p>
	</div>
	
	
	
	
	
	
	<script src='jsp/punchClock/js/vue.js'></script>
	<script id="rendered-js">
		var clock = new Vue({
			el : '#clock',
			data : {
				time : '',
				date : ''
			}
		});

		var week = [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ];
		var timerID = setInterval(updateTime, 1000);
		updateTime();
		function updateTime() {
			var cd = new Date();
			clock.time = zeroPadding(cd.getHours(), 2) + ':'
					+ zeroPadding(cd.getMinutes(), 2) + ':'
					+ zeroPadding(cd.getSeconds(), 2);
			clock.date = zeroPadding(cd.getFullYear(), 4) + '-'
					+ zeroPadding(cd.getMonth() + 1, 2) + '-'
					+ zeroPadding(cd.getDate(), 2) + ' ' + week[cd.getDay()];
		};

		function zeroPadding(num, digit) {
			var zero = '';
			for (var i = 0; i < digit; i++) {
				zero += '0';
			}
			return (zero + num).slice(-digit);
		}
	</script>

	<script src="jsp/punchClock/js/jquery-3.4.1.min.js"></script>
	<script src="jsp/punchClock/js/bootstrap.min.js"></script>
	<script src="jsp/punchClock/js/bootstrap-notify.min.js"></script>
	
	
	
	<input type="text" id="cardNo" style="width:1px;"/>
	
<script>

var url="http://ap.cust.edu.tw/CIS/CardReaderSimple";

var cname,day,info, idno, html, time;

$("#cardNo").keydown(function (event) {
    if (event.which == 13) {
    	
    	if($("#cardNo").val().length>=10)
    	 $.ajax({
    		 	
    			
    		    url:"http://ap.cust.edu.tw/CIS/CardReaderSimple",
    		    type:"GET",
    		    dataType: "xml",//資料型態可以不設定，且此型態不可是text或html
    		    data: { 
    		    	CardNo:$("#cardNo").val(), 
    		    	day:clock.date, 
    		    	time:clock.time
    		    },
    		    timeout: 1000,
    		    error: function(xml){
    		    	$.notify({		
    		    		//icon: "http://ap.cust.edu.tw/CIS/Personnel/getFTPhoto4Empl?idno=1234567",
    		    		title: "訊息",
    		    		message: "遠端服務發生問題",
    		    		
    		    	},{
    		    		allow_dismiss: false,
    		    		type: 'success',
    		    		progress:25,
    		    		showProgressbar: true,
    		    	type: 'minimalist',
    		    	delay: 100,
    		    	icon_type: 'image',
    		    	
    		    	
    		    });
    		    },
    		    success: function(xml){
    		      $(xml).find("pront").each(function(i){  

    		        idno=$(this).children("idno").text(); //取得子節點中的src資料
    		        cname=$(this).children("cname").text(); //取得子節點中的src資料
    		        day=$(this).children("day").text(); //取得子節點中的url資料
    		        info=$(this).children("info").text(); //取得子節點中的url資料
    		        times=$(this).children("time"); //取得子節點中的url資料
    		       
    		        
    		        
    		        html="<div data-notify='container' class='col-xs-11 col-sm-4 alert alert-warning' role='alert'>";
    		        html+="<table><tr><td><img src='http://ap.cust.edu.tw/CIS/Personnel/getFTPhoto4Empl?idno="+idno+"'/></td>";
    		        html+="<td valign='top'>";
    		        html+=cname+info+times[0];
    		        html+="</td>";
    		        html+="</tr>";
    		        /*for(i=0; i<times.length; i++){
    		        	html+=times[i].text();
    		        }*/
    		        
    		        
    		        $.notify({    		    		    		    		
    		    		//icon: "http://ap.cust.edu.tw/CIS/Personnel/getFTPhoto4Empl?idno="+idno,
    		    		//title: cname,
    		    		//message: cname+"",
    		    		
    	    		},{
    	    			
    	    			allow_dismiss: true,
    	    			//type: 'success',
    	    			progress:25,
    	    			showProgressbar: true,
	    	    		type: 'minimalist',
	    	    		delay: 100,
	    	    		icon_type: 'image',
    	    			
	    	    		template:html   	    			
    	    		
    	    	});
    		    ///

    		   })
    		 }
    	 });
    	 
    
    	
    	
    $("#cardNo").val("");
    }
}); 





$(function(){
	$.notify({		
		//icon: "http://ap.cust.edu.tw/CIS/Personnel/getFTPhoto4Empl?idno=1234567",
		title: "訊息",
		message: "程式啟動",
		
	},{
		allow_dismiss: false,
		type: 'success',
		progress:25,
		showProgressbar: true,
	type: 'minimalist',
	delay: 100,
	icon_type: 'image',
	
	
});
	
	function show(){
		$('#cardNo').focus();
	}
	
	setInterval(show,100);// 注意函式名沒有引號和括弧！
	
});


</script>
</body>
</html>
