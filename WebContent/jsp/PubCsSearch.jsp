<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<title>課程查詢</title>
<script src="/eis/inc/js/plugin/bootstrap-typeahead.js"></script>
<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
<script src="/eis/inc/js/plugin/bootstrap-tooltip.js"></script>
<script src="/eis/inc/js/plugin/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="/eis/inc/js/plugin/birthdayPicker/birthdayPicker.js" type="text/javascript"></script>
<link href="/eis/inc/js/plugin/birthdayPicker/birthdayPicker.css" rel="stylesheet"/>
<link href="/eis/inc/css/jquery-ui.css" rel="stylesheet"/>
<script src="/eis/inc/js/autoComplete.js"></script>
<script src="/eis/inc/js/develop/timeInfo.js"></script>

<script>
$(document).ready(function() {	
	$(".help").popover("show");
	setTimeout(function() {
		$('.help').popover("hide");
	}, 5000);
});
</script>
</head>
<body>
    
    
<div class="bs-callout bs-callout-info">
<h4>課程查詢</h4> 輸入教師名稱查詢留校時間, 非本學期資料依各單位公佈或留存為準
</div>

<form action="PubCsSearch" method="post"  class="form-inline">


<div class="panel panel-primary">
	<div class="panel-heading">查詢條件</div>
<table class="table">
	
	<tr>
		<td nowrap>
		<div class="input-group">
				<input type="text" style="width:70px;" placeholder="學年度" class="form-control" name="year" id="year" onClick="this.value='';"
				<c:choose>
    			<c:when test="${year==school_year||empty year}">value="${school_year}"</c:when>
    			<c:when test="${year!=school_year}">value="${year}"</c:when>
    			</c:choose>
    			autocomplete="off" />
				<span class="input-group-addon">學年</span>
		</div>
		<select name="term" id="term" class="form-control">
			<option <c:if test="${term eq'1'}">selected</c:if> value="1">上學期</option>
			<option <c:if test="${term eq'2'}">selected</c:if> value="2">下學期</option>
		</select>	
		
		<%@ include file="/inc/jsp-kit/classSelector.jsp"%></td>
	</tr>
	<tr>
		<td>		
			<div class="input-group">
				<span class="input-group-addon">授課教師</span>
				<input type="text" placeholder="授課教師姓名" class="form-control techid" name="techid" id="techid" value="${techid}" onClick="this.value='';" autocomplete="off" data-provide="typeahead"/>
			</div>	
			<div class="input-group">
				<span class="input-group-addon">科目名稱</span>
				<input type="text" placeholder="課程名稱片段或課程代碼" class="form-control cscode" name="cscode" id="cscode" value="${cscode}" onClick="this.value='';" autocomplete="off" data-provide="typeahead"/>
			</div>
			<span class="tgroup">
			<div class="input-group">
				<span class="input-group-addon">教室名稱</span>
				<input type="text" placeholder="教室名稱或教室代碼" class="form-control place" name="place" id="place" value="${place}" onClick="this.value='';" autocomplete="off" data-provide="typeahead"/>
			</div>
			</span>
		</td>
	</tr>
	<tr>
		<td>
		<div class="input-group">
			<span class="input-group-addon">選別</span>
            <select name="opt" class="form-control">
				<option value=""></option>
				<option <c:if test="${opt eq'1'}">selected</c:if> value="1">必修</option>
				<option <c:if test="${opt eq'2'}">selected</c:if> value="2">選修</option>
				<option <c:if test="${opt eq'3'}">selected</c:if> value="3">通識</option>
			</select>
        </div>
		
		<div class="input-group">
			<span class="input-group-addon">授課型態</span>
            <select name="ele" class="form-control">
				<option value=""></option>
				<option <c:if test="${ele eq'0'}">selected</c:if> value="0">一般</option>
				<option <c:if test="${ele eq'1'}">selected</c:if> value="1">遠距</option>
				<option <c:if test="${ele eq'2'}">selected</c:if> value="2">輔助</option>
				<option <c:if test="${ele eq'3'}">selected</c:if> value="3">多媒體</option>
			</select>
        </div>
        
        <span class="tgroup">
        <div class="input-group">
			<span class="input-group-addon">實習費</span>
            <select name="pay" class="form-control">
				<option value=""></option>
				<option <c:if test="${pay eq'1'}">selected</c:if> value="1">有</option>
				<option <c:if test="${pay eq'0'}">selected</c:if> value="0">無</option>				
			</select>
        </div>
        <div class="input-group">
			<span class="input-group-addon">星期</span>
            <select name="week" class="form-control" id="week">
				<option value=""></option>
				<option <c:if test="${week eq'1'}">selected</c:if> value="1">一</option>
				<option <c:if test="${week eq'2'}">selected</c:if> value="2">二</option>
				<option <c:if test="${week eq'3'}">selected</c:if> value="3">三</option>
				<option <c:if test="${week eq'4'}">selected</c:if> value="4">四</option>
				<option <c:if test="${week eq'5'}">selected</c:if> value="5">五</option>
				<option <c:if test="${week eq'6'}">selected</c:if> value="6">六</option>
				<option <c:if test="${week eq'7'}">selected</c:if> value="7">日</option>
			</select>
        </div>
        
        <div class="input-group">
				<span class="input-group-addon">節次自</span>
            <select name="begin" id="begin" class="form-control" onChange="$('#end').val(this.value)">
				<option value=""></option>
				<c:forEach begin="1" end="16" varStatus="d">
				<option <c:if test="${begin == d.index}">selected</c:if> value="${d.index}">第${d.index}節</option>
				</c:forEach>
			</select>
        </div>
		
		<div class="input-group">
			<span class="input-group-addon">至</span>
            <select name="end" id="end" class="form-control" data-style="btn-danger">
				<option value=""></option>
				<c:forEach begin="1" end="16" varStatus="d">
				<option <c:if test="${end == d.index}">selected</c:if> value="${d.index}">第${d.index}節</option>
				</c:forEach>
			</select>
        </div>
        </span>
		<div class="btn-group" >
		<button class="btn btn-danger" name="method:search">查詢</button>
		<a class="btn btn-default" href="PubCsSearch">重新設定條件</a>
		</div>
		</td>
	</tr>
</table>

</div>

</form>    
<c:if test="${!empty dtimes}">
<div class="panel panel-primary">
	<div class="panel-heading">查詢結果</div>
<display:table name="${dtimes}" requestURI="PubCsSearch" export="false" id="row" sort="list" excludedParams="*" class="table table-hover" >
	<display:column title="學年" class="left">${row.school_year}-${row.school_term}</display:column>
  	<display:column title="課程編號" property="Oid" sortable="true" class="left" />
  	<display:column title="課程名稱" style="{whitespace: nowrap; }"  property="chi_name" sortable="true" class="left"/>
  	<display:column title="開課班級" sortable="true" class="left">${row.ClassName}
  	<c:if test="${!empty row.Oid}">
  	<a data-toggle="modal" data-target=".bs-example-modal-lg" onClick="getClassTime('${row.ClassNo}', '${row.ClassName}')">課表</a>
  	</c:if>
  	</display:column>
  	<display:column title="授課教師" sortable="true" class="left">${row.cname}<a href="">
  	<c:if test="${!empty row.Oid}">
  	<c:if test="${!empty row.cname}">  	
  	<a data-toggle="modal" data-target=".bs-example-modal-lg" onClick="getTeacherTime('${row.emplOid}', '${row.cname}')">課表</a>
  	</c:if>
  	</c:if>
  	</display:column>
  	<display:column title="選別" property="optName" sortable="true" class="left" />
  	<display:column title="學分" property="credit" sortable="true" class="left" />
  	<display:column title="時數" property="thour" sortable="true" class="left" />
  	<display:column title="男/女" class="right" sortable="true">${row.bsed}/${row.gsed}</display:column>  	
  	<display:column title="" class="center">
  	<c:if test="${!empty row.Oid}">
	  	<c:choose>
		<c:when test="${year==school_year}"><a href="/pis/SylDoc?Dtime_oid=${row.Oid}">大綱</a>|<a href="/csis/IntorDoc?Dtime_oid=${row.Oid}">簡介</a></c:when>
		<c:when test="${year!=school_year}"><a href="/pis/SylDoc?Savedtime_oid=${row.Oid}">大綱</a>|<a href="/csis/IntorDoc?Savedtime_oid=${row.Oid}">簡介</a></c:when>
		</c:choose>  	
	</c:if>
  	</display:column>
  	<display:column title="時間地點" class="left">
  	<c:if test="${!empty row.Oid}">
  	<c:forEach items="${row.time}" var="t">
  	<c:choose>
    <c:when test="${t.week==7}">週日</c:when>
    <c:when test="${t.week==1}">週一</c:when>
    <c:when test="${t.week==2}">週二</c:when>
    <c:when test="${t.week==3}">週三</c:when>
    <c:when test="${t.week==4}">週四</c:when>
    <c:when test="${t.week==5}">週五</c:when>
    <c:when test="${t.week==6}">週六</c:when>
    <c:otherwise>週${t.week},</c:otherwise>
	</c:choose>
  	${t.begin}~${t.end}節${t.place}<a data-toggle="modal" data-target=".bs-example-modal-lg" onClick="getRoomTime('${row.time[0].place}', '${row.time[0].place}教室')">課表</a><br>
  	</c:forEach>
  	</c:if>
  	<c:if test="${empty row.Oid}">
  	<c:choose>
    <c:when test="${row.week==7}">週日</c:when>
    <c:when test="${row.week==1}">週一</c:when>
    <c:when test="${row.week==2}">週二</c:when>
    <c:when test="${row.week==3}">週三</c:when>
    <c:when test="${row.week==4}">週四</c:when>
    <c:when test="${row.week==5}">週五</c:when>
    <c:when test="${row.week==6}">週六</c:when>
    <c:otherwise>週${row.week},</c:otherwise>
	</c:choose>第 ${row.period}節, ${row.place}
  	</c:if>
  	
  	</display:column>
</display:table>
</div>
</c:if>
<script>  
$("#year").keyup(function(){
	weekShow();
}); 

$( document ).ready( function() {
	weekShow();

});

function weekShow(){
	if($("#year").val()!=${school_year}){
		$(".tgroup").hide("slow");
	}else{
		$(".tgroup").show("slow");
	}
}

	
	
	
	




</script>





<!-- Modal -->
<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h3 id="title"></h3>
      </div>
      <div class="modal-body" id="info"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>


</body>
</html>