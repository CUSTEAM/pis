<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>家長資訊</title>
<script src="/eis/inc/js/plugin/bootstrap-scrollspy.js"></script>
<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
<script src="/eis/inc/js/plugin/bootstrap-tooltip.js"></script>
<link href="/eis/inc/css/jquery-ui.css" rel="stylesheet" />
<script>
$.ajaxSetup ({cache: false});
$(document).ready(function() {
	$("#dilgTag").val("");
	$("#scoreTag").val("");
	$("#careTag").val("");
});
</script>
</head>
<body data-spy="scroll" data-target="#navbarExample" data-offset="0">
<div id="navbarExample" class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container" style="width: auto;">
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                  </a>
			<a class="brand" href="http://john.cust.edu.tw/pis/Parent#fat">&nbsp;&nbsp;中華科技大學</a>
			<c:if test="${!empty std}">
			<div class="nav-collapse collapse navbar-responsive-collapse">
				<ul class="nav">
					<li><a href="#csTable">本學期課表</a></li>
					<li><a href="#dilg">缺曠記錄</a></li>
					<li><a href="#score">歷年成績</a></li>
					<li><a href="#desd">獎懲記錄</a></li>
					<!-- li><a href="#care">輔導記錄</a></li-->
					<li><a href="#contact">與導師連繫</a></li>
				</ul>
			</div>
			</c:if>
		</div>
	</div>
</div>

<c:if test="${empty std}">
<%@ include file="Parent/login.jsp"%>
</c:if>

<c:if test="${!empty std}">
<div class="container">
<div id="csTable" style="height:40px;"></div>
<h4>本學期課表</h4>
<table class="table table-bordered table table-striped">
		<tr>
			<td></td>
			<td width="14%" nowrap>${viewday[1]} 星期一</td>
			<td width="14%" nowrap>${viewday[2]} 星期二</td>
			<td width="14%" nowrap>${viewday[3]} 星期三</td>
			<td width="14%" nowrap>${viewday[4]} 星期四</td>
			<td width="14%" nowrap>${viewday[5]} 星期五</td>
			<td width="14%" nowrap>${viewday[6]} 星期六</td>
			<td width="14%" nowrap>${viewday[7]} 星期日</td>
		</tr>
		<c:set var="beginClass" value="1"/>
		<c:if test="${allClass[0].begin>=10}"><c:set var="beginClass" value="11"/></c:if>
		<c:forEach begin="${beginClass}" end="16" var="c">
		<tr height="75">
		<td class="hairLineTdF">${c}</td>		
		<c:forEach begin="1" end="7" var="w">		
		<td class="hairLineTdF" style="font-size:14px;" valign="middle">		
				
		<c:forEach items="${allClass}" var="ac">				
		<c:if test="${ac.week==w && (c>=ac.begin && c<=ac.end)}">			
		<div id="a${w}${c}" style="cursor:pointer; width:100%;">
		${ac.chi_name}			
		<br>
		<a href="/CIS/Print/teacher/SylDoc.do?Oid=${ac.dOid}">大綱</a>-<a href="/CIS/Print/teacher/IntorDoc.do?Oid=${ac.dOid}">簡介</a>
		<br>${ac.cname}老師<br>${ac.place}教室<br>					
		</div>			
		</c:if>			
		</c:forEach>		
		</td>		
		</c:forEach>
	</tr>
	</c:forEach>
</table>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<input id="stdNo" type="hidden" value="${stdNo}"/>
<div id="dilg" style="height:50px;"></div>
<h4>本學期缺曠記錄</h4>
<input type="hidden" id="dilgTag" value="" />

<div class="row">
	<div class="span6"><div id="dilgChart" style="width:auto; height:300px"></div></div>
	<div class="span6"><div id="dilgChart1" style="width:100%; height:300px"></div></div>
</div>
<table class="table" id="dilgTable"></table> 
              
<div id="score" style="height:50px;"></div>
<h4>歷年成績</h4>
<input type="hidden" id="scoreTag" value="" />
<div class="row">
	<div class="span6"><div id="scoreChart" style="width:auto; height:300px"></div></div>
	<div class="span6"><div id="scoreChart1" style="width:100%; height:300px"></div></div>
</div>
<table id="scoreTable" class="table"></table>

<div id="desd" style="height:50px;"></div>
<h4>獎懲記錄</h4>
<input type="hidden" id="desdTag" value="" />
<table id="desdTable" class="table"></table>
<!-- 
<div id="care" style="height:50px;"></div>
<h4>輔導記錄</h4>
<input type="hidden" id="careTag" value="" />
<table id="careTable" class="table"></table>
-->
<div id="contact" style="height:50px;"></div>
<h4>與導師連繫</h4>
<strong>${tutor.cname}</strong>老師<br>
<address>
	<abbr title="Phone">電話:</abbr> ${tutor.phone}<br>
	<abbr title="Phone">行動電話:</abbr> ${tutor.CellPhone}<br>
 	電子郵件: <a href="mailto:#">${tutor.Email}</a><br>
	<strong>中華科技大學${tutor.schoolName}, ${tutor.deptName}</strong><br>
	${tutor.address}${tutor.location}
</address>
<div style="height:200px;"></div>  
<hr/>
        <p class="pull-right"><a href="#">回頂端</a></p>
        <p>&copy; 中華科技大學 &middot; <a href="#">電子計算機中心</a> &middot;</p>
      
</div>   

<script type="text/javascript">
  google.load("visualization", "1", {packages:["corechart"]});
  var option;
  var data;
</script>

<script>
$(window).scroll(function () {
	//缺曠
	if($("#dilgTag").val()==""){
		if ($(window).scrollTop()+$(window).height()>=$("#dilg").offset().top && $(window).scrollTop()<$("#score").offset().top) {	        
	        
			$("#dilgTag").val("*");
	        			
			$.ajax({
	            url: "/eis/getStdDilgChart?stdNo="+$("#stdNo").val(),
	            dataType: "JSON",			
	            success: function (d) {
	            	options = {title: '各項缺席比例'};
	            	data = google.visualization.arrayToDataTable(d.cData);
	            	chart = new google.visualization.PieChart(document.getElementById('dilgChart'));
	    			chart.draw(data, options);	    			
	    			//長條
	    			data = google.visualization.arrayToDataTable(d.dData);
                    options = {title: '各課程缺席狀況'};
                    chart = new google.visualization.ColumnChart(document.getElementById('dilgChart1'));
                    chart.draw(data, options);	    			
	            }
	        });
			
			//建立表格
			$.ajax({
				  url: "/eis/getStdDilgJson?stdNo="+$("#stdNo").val(),
				  type: "GET",
				  dataType: "json",
				  success: function(data) {
					
					  if(data.list.length>0){
						  
						  var i = 0;
						  $.each(data.list, function() {
						    //呼叫方式也稍微改變了一下，意思等同於上述的的JData[i]["idx_Key"]
						    $("#dilgTable").append("<tr>" +
	                        "<td nowrap>" + data.list[i].date   + "</td>" +
	                        "<td nowrap>第 " + data.list[i].cls    + "節</td>" +
	                        "<td nowrap>" + data.list[i].chi_name    + "</td>" +
	                        "<td nowrap>" + data.list[i].name    + "</td>" +
	                        "<td width='100%'></td>" +
	                        "</tr>");
						    i++;
						  });
						  
					  }else{
						  $("#dilgChart").hide(1000);
						  $("#dilgChart1").hide(1000);
						  $("#dilgTable").append("<tr><td>無缺曠記錄</td></tr>");
						  
					  }
					  					  
						  						  
					  
					  
				    
				  },
				  
				  error: function() {
				    alert("ERROR!!!");
				  }
			});
	    }
	}
	
	
	//成績
	if($("#scoreTag").val()==""){
		if ($(window).scrollTop()+$(window).height()>=$("#scoreTag").offset().top && $(window).scrollTop()<$("#desd").offset().top) {	
			
	        $("#scoreTag").val("*");
	        
	        //繪製圖表
	        $.ajax({
	            url: "/eis/getStdScoreChart?stdNo="+$("#stdNo").val(),
	            dataType: "JSON",			
	            success: function (d) {	            	
	            	options = {title:'成績分佈'};
	            	data = google.visualization.arrayToDataTable(d.cData);
	            	chart = new google.visualization.PieChart(document.getElementById('scoreChart'));
	    			chart.draw(data, options);	    			
	    			//長條
	    			var data = google.visualization.arrayToDataTable(d.bData);
                	options = {title: '平均分數與名次'};
                   	chart = new google.visualization.LineChart(document.getElementById('scoreChart1'));
                   	chart.draw(data, options);
	            }
	        });			        
			        
			//建立表格
			$.ajax({
				  url: "/eis/getStdScore?stdNo="+$("#stdNo").val(),
				  type: "GET",
				  dataType: "json",
				  success: function(data) {
					if(data.list.length>0){		
						for(i=0; i<data.list.length; i++){
							
							$("#scoreTable").append("<tr>" +
					                  "<td colspan='5'><h5>" + data.list[i].school_year+"學年, 第"+data.list[i].school_term+"學期"/*, 平均成績: "+data.list[i].avg+ ", 全班排名: " +data.list[i].rank*/+"</h5></td>" +
					                  "</tr>");
									  
									  $("#scoreTable").append("<tr>" +
			                          "<td nowrap>科目名稱</td>" +
			                          "<td nowrap>選別</td>" +
			                          "<td nowrap>學分數</td>" +
			                          "<td nowrap>成績</td>" +
			                          "<td width='100%'></td>" +
			                          "</tr>"+"");
									  for(j=0; j<data.list[i].score.length; j++){
										  $("#scoreTable").append("<tr>" +
						                            "<td nowrap>" + data.list[i].score[j].chi_name   + "</td>" +
						                            "<td nowrap> " + data.list[i].score[j].opt    + "</td>" +
						                            "<td nowrap>" + data.list[i].score[j].credit    + "</td>" +
						                            "<td nowrap>" + data.list[i].score[j].score    + "</td>" +
						                            "<td width='100%'></td>" +
						                            "</tr>"+"");
									  }
							
						}
							  						  
						  }else{
							  $("#scoreChart").hide(1000);
							  $("#scoreChart1").hide(1000);
							  $("#scoreTable").append("<tr><td>無歷年成績記錄</td></tr>");
						  }
		           
					  
				  },
				  
				  error: function() {
				    alert("ERROR!!!");
				  }
			});	        
	    }
	}	
	
	//獎懲
	if($("#desdTag").val()==""){
		if ($(window).scrollTop()+$(window).height()>=$("#desd").offset().top && $(window).scrollTop()<$("#care").offset().top) {
	        $("#desdTag").val("*");
	      //建立表格
			$.ajax({
				  url: "/eis/getStDesd?stdNo="+$("#stdNo").val(),
				  type: "GET",
				  dataType: "json",
				  success: function(data) {
					  if(data.list.length>0){
						  
						  $("#desdTable").append("<tr>" +
			                         "<td nowrap>學年</td>" +
			                         "<td nowrap>學期</td>" +
			                         "<td nowrap>類型</td>" +
			                         "<td nowrap>次數</td>" +
			                         "<td nowrap>類型</td>" +
			                         "<td nowrap>次數</td>" +
			                         "<td width='100%'></td>" +
			                         "</tr>"+"");
								  for(i=0; i<data.list.length; i++){
									  
									  $("#desdTable").append("<tr>" +
				                         "<td nowrap>" + data.list[i].school_year   + "</td>" +
				                         "<td nowrap> " + data.list[i].school_term    + "</td>" +
				                         "<td nowrap>" + data.list[i].name1    + "</td>" +
				                         "<td nowrap>" + data.list[i].cnt1    + "</td>" +
				                         "<td nowrap>" + data.list[i].name2    + "</td>" +
				                         "<td nowrap>" + data.list[i].cnt2    + "</td>" +
				                         "<td nowrap>" + data.list[i].name    + "</td>" +
				                         "</tr>"+"");
								  }
						  
					  }else{
						  $("#desdTable").append("<tr><td>無獎懲記錄</td></tr>");
					  }					  
				  },
				  
				  error: function() {
				    alert("ERROR!!!");
				  }
			});
		}
	}
	/*
	//輔導記錄
	if($("#careTag").val()==""){
		if ($(window).scrollTop()+$(window).height()>=$("#care").offset().top && $(window).scrollTop()<$("#contact").offset().top) {	        
	        $("#careTag").val("*");
	      
	      	//建立表格
			$.ajax({
				  url: "/eis/getStudCounseling?stdNo="+$("#stdNo").val(),
				  type: "GET",
				  dataType: "json",
				  success: function(data) {
					  if(data.list.length>0){
						  
						  $("#careTable").append("<tr>" +
			                         "<td nowrap>學年</td>" +
			                         "<td nowrap>學期</td>" +
			                         "<td nowrap>教師</td>" +
			                         "<td nowrap>類型</td>" +
			                         "<td nowrap>課程</td>" +			                         
			                         "<td width='100%'>內容</td>" +
			                         "</tr>"+"");
								  for(i=0; i<data.list.length; i++){
									  $("#careTable").append("<tr>" +
				                         "<td nowrap>" + data.list[i].schoolYear   + "</td>" +
				                         "<td nowrap> " + data.list[i].schoolTerm    + "</td>" +
				                         "<td nowrap>" + data.list[i].cname    + "</td>" +
				                         "<td nowrap>" + data.list[i].itemName    + "</td>" +
				                         "<td nowrap>" + data.list[i].chi_name    + "</td>" +
				                         "<td nowrap>" + data.list[i].content    + "</td>" +
				                         "</tr>"+"");
								  }
					  }else{
						  $("#careTable").append("<tr><td>無輔導記錄</td></tr>");
						  
					  }
					  
				  },
				  
				  error: function() {
				    alert("ERROR!!!");
				  }
			});
	        
		}
	}	
    */
});

</script>


</c:if>






</body>
</html>