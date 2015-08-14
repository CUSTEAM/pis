<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/eis/inc/js/plugin/jQuery-File-Upload/css/jquery.fileupload-ui.css">
<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/vendor/jquery.ui.widget.js"></script>
<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.iframe-transport.js"></script>
<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.fileupload.js"></script>
<title>中華科技大學新生學籍簡表</title>
<script src="/eis/inc/js/plugin/bootstrap-typeahead.js"></script>
<script src="/eis/inc/js/plugin/json2.js"></script>
<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
<script src="/eis/inc/js/plugin/bootstrap-tooltip.js"></script>
<link href="/eis/inc/css/jquery-ui.css" rel="stylesheet"/>
<link href="/eis/inc/css/wizard-step.css" rel="stylesheet"/>
<script> 
$(document).ready(function() {	
	$('#q1').popover("show");	
	setTimeout(function() {
		$('#q1').popover("hide");
	}, 3000);
	$("input[id='idiot']").typeahead({
		remote:"#stdNo",
		source : [],
		items : 10,
		updateSource:function(inputVal, callback){			
			$.ajax({
				type:"POST",
				url:"/eis/autoCompleteStmd",
				dataType:"json",
				data:{length:10, nameno:inputVal},
				success:function(d){
					callback(d.list);
				}
			});
		}		
	});
	
});

function ImgError(source){  
    source.src = "/eis/img/icon/User-Black-Folder-icon.png";  
    source.onerror = "";  
    return true;  
} 
</script>
</head>
<body>    
<div id="navbarExample" class="navbar navbar-fixed-top">
              <div class="navbar-inner">
                <div class="container" style="width: auto;">
                
                  <a class="brand" href="/pis/StdProfileEdit">&nbsp;中華科技大學新生學籍簡表</a>
                 
                  
                  
                </div>
              </div>
            </div>
<div class="alert">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<strong>請依欄位名稱輸入資料</strong>
	<div id="q1" rel="popover" title="說明" data-content="驗證後請依欄位名稱建立資料,照片為日後相關證件用,請使用高中職2吋畢業照" data-placement="right" class="btn btn-warning">?</div>
</div>

<form action="StdProfileEdit" method="post">
<c:if test="${empty std}">
<div class="wizard-steps">
  	<div><a href="#"><span>1</span> 驗證</a></div>
  	<div><a href="#"><span>2</span> 上傳照片</a></div>
  	<div><a href="#"><span>3</span> 修改基本資料</a></div>
  	<div><a href="#"><span>4</span> 完成</a></div>
</div>
<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap>身分證</td>
		<td class="text-info" nowrap>		
		<input type="text" autocomplete="off" name="idno" class="span2"/>		
		</td>
		<td class="text-info" nowrap>生日</td>
		<td class="text-info" nowrap>
		<input type="password" name="birthday" id="birthday" class="span2"/>		
		</td>
		<td class="text-info" nowrap>		
		<button name="method:login" type="submit" class="btn btn-success">開始</button>
		</td>
		<td width="100%"></td>
	</tr>
	<tr>
		<td colspan="5">
		<div class="bs-docs-example">
          <div id="popalert" class="alert alert-block alert-error fade in">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4 class="alert-heading">請注意以下事項</h4>
            <p></p>            
            <p>1.生日格式為YYMMDD,如民國88年1月1日則輸入880101</p>
            <p>2.照片為日後相關證件用,請使用高中職2吋畢業照</p>
            <p>3.戶籍地址資訊請儘可能輸入完整,包含:村、里、鄰</p>
            <p>
              <button type="button" onClick="$('#popalert').css('display','none'); " class="btn btn-danger">我知道了</button> <button type="button" class="btn">?</button>
            </p>
          </div>
        </div>
		</td>
		<td></td>
	</tr>
</table>
</c:if>
<c:if test="${!empty std}">
<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap>班級: ${std.ClassName} ${std.divi}</td>
		<td class="text-info" nowrap>學號: ${std.student_no}<input type="hidden" name="myStdNo" value="${myStdNo}" /></td>
		<td class="text-info" nowrap>姓名: ${std.student_name}</td>
		<td width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap>
			<div id="files">
			<img class='img-polaroid' onerror="ImgError(this);" src='/eis/getStdimage?myStdNo=${myStdNo}&"+Math.floor(Math.random()*999)+"'>
			</div> 
		</td>		
		<td nowrap>
			<span class="btn btn-success fileinput-button">
		        <i class="icon-plus icon-white"></i>
		        <span>選擇照片</span>
		        <input id="myFile" type="file" 
		        onClick=" $('#progress .bar').css('width','0%'),$('#files').html('');" name="myFile" multiple>
		    </span>
		    <br /><span>請上傳正式2吋或3吋證件照片 (製作學生證使用)</span>
		</td>		
		<td width="100%"></td>
	</tr>
</table>
<script>
$(function(){
	//文件上傳地址
	    var url = "/eis/putStdimage";   
	    $('#myFile').fileupload({	    	
	        autoUpload: true,//自動上傳true
	        url: url,
	        dataType: 'json',	        
	        done: function (e, data) {
	            $.each(data.result.files, function (index, file) {//
	                if(file.name!=null){
	                	$("#files").html("<img class='img-polaroid' src='/eis/getStdimage?myStdNo=${myStdNo}&"+Math.floor(Math.random()*999)+"'>");
	                }else{
	                	$("#files").html("必需是.jpg檔案");
	                }	            		            
	            });
	            $.unblockUI();
	        },
	        progressall: function (e, data) {
	        	$.blockUI({ 
	                theme:     true, 
	                title:    "影像處理中", 
	                message:  "<div id='progress' class='progress progress-success progress-striped'><div class='bar'></div></div>"
	            }); 
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $('#progress .bar').css('width',progress+'%');
	        }
	    });
	});
</script>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap>基本資料</td>
		<td class="text-info" nowrap><b class="text-warning">*</b>星號必填欄位</td>
		<td class="text-info" width="100%"></td>
	</tr>
	
	
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>性別</td>
		<td class="text-info">
		<select name="sex" disabled>
			<option <c:if test="${std.sex eq '1'}">selected</c:if> value="1">男</option>
			<option <c:if test="${std.sex eq '2'}">selected</c:if> value="2">女</option>
		</select>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap><b class="text-warning"></b>出生地 (縣市)</td>
		<td class="text-info">
		<select name="birth_county">
			<option value=""></option>
			<c:forEach items="${birth_county}" var="d">
				<option <c:if test="${std.birth_county eq d.no}">selected</c:if> value="${d.no}">${d.name}</option>
			</c:forEach>
		</select>
	</tr>
	<tr>
		<td class="text-info" nowrap>國籍</td>
		<td nowrap>
		<input type="text" autocomplete="off" name="BirthCountry" value="${std.BirthCountry}" class="span2"/>		
		<select name="ident_remark">
			<option value="">非新住民身份</option>
			<option <c:if test="${fn:indexOf(std.ident_remark, '新住民')>-1}">selected</c:if> value="新住民">具新住民身份</option>
		</select>
		
		<input type="text" autocomplete="off" placeholder="英文姓名" name="student_ename" value="${std.student_ename}" class="span2"/>
		</td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap>僑生僑居地</td>
		<td><input type="text" autocomplete="off" name="ForeignPlace" value="${std.ForeignPlace}" class="span2"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap>原住民族籍</td>
		<td class="text-info">
		<select name="Aborigine">
			<option value="">無</option>
			<c:forEach items="${Aborigine}" var="d">
				<option <c:if test="${std.Aborigine eq d.Name}">selected</c:if> value="${d.Name}">${d.Name}</option>
			</c:forEach>
		</select>
		<td class="text-info" width="100%"></td>
		</td>
	</tr>
	
	<tr>
		<td class="text-info" nowrap>入學前之學歷學校名稱</td>
		<td><input type="text" autocomplete="off" name="schl_name" value="${std.schl_name}" class="span3"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap>入學前之科系</td>
		<td><input type="text" autocomplete="off" name="grad_dept" value="${std.grad_dept}" class="span3"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>戶籍地址 (包含:村、里、鄰) <!-- <button type="button" onClick="$('#perm_post').val($('#curr_post').val()),$('#perm_addr').val($('#curr_addr').val())" class="btn btn-info btn-small">同通訊地址</button> --></td>
		<td class="text-info" nowrap>郵遞區號</td>
		<td class="text-info"><input type="text" autocomplete="off" id="perm_post" name="perm_post" value="${std.perm_post}" class="span1"/></td>
		<td class="text-info" nowrap>地址</td>
		<td class="text-info"><input type="text" autocomplete="off" id="perm_addr" name="perm_addr" value="${std.perm_addr}" class="span6"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>電話</td>
		<td><input type="text" autocomplete="off" name="telephone" value="${std.telephone}" class="span4"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>行動電話</td>
		<td><input type="text" autocomplete="off" name="CellPhone" value="${std.CellPhone}" class="span4"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>電子郵件</td>
		<td><input type="text" autocomplete="off" name="Email" value="${std.Email}" class="span4"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap>家長資料</td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>家長姓名</td>
		<td><input type="text" autocomplete="off" name="parent_name" value="${std.parent_name}" class="span2"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap>家長年齡</td>
		<td><input type="text" autocomplete="off" name="ParentAge" value="${std.ParentAge}" class="span1"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap>家長職業</td>
		<td><input type="text" autocomplete="off" name="ParentCareer" value="${std.ParentCareer}" class="span2"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>家長通訊地址 <button type="button" onClick="$('#curr_post').val($('#perm_post').val()),$('#curr_addr').val($('#perm_addr').val())" class="btn btn-info btn-small">同戶籍地址</button></td>
		<td class="text-info" nowrap>郵遞區號</td>
		<td class="text-info"><input type="text" autocomplete="off" id="curr_post" name="curr_post" value="${std.curr_post}" class="span1"/></td>
		<td class="text-info" nowrap>地址</td>
		<td class="text-info"><input type="text" autocomplete="off" id="curr_addr" name="curr_addr" value="${std.curr_addr}" class="span6"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>緊急連絡電話</td>
		<td><input type="text" autocomplete="off" name="EmergentPhone" value="${std.EmergentPhone}" class="span4"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
	<tr>
		<td class="text-info" nowrap><b class="text-warning">*</b>緊急連絡行動電話</td>
		<td><input type="text" autocomplete="off" name="EmergentCell" value="${std.EmergentCell}" class="span4"/></td>
		<td class="text-info" width="100%"></td>
	</tr>
</table>

<table class="table control-group info text-info">
	<tr>
		<td>
		<button class="btn btn-danger" name="method:save" type="submit" onClick="$.blockUI({message:null});">儲存輸入資料</button>
		<a href="StdProfileEdit" class="btn">離開</a>
		</td>
	</tr>
</table>
</c:if>
</form>    
</body>
</html>