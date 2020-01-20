<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <title>中華科技大學</title>
    <meta charset="UTF-8">
    <meta name="JOHN HSIAO" content="http://blog.xuite.net/hsiao/blog" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
	<meta HTTP-EQUIV="expires" CONTENT="-1">
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<meta name="ROBOTS" content="none">
	<!-- link rel="stylesheet" href="jsp/Enroll/css/jquery-confirm.css" /-->
    <link rel="stylesheet" href="jsp/Enroll/css/adv.css" />
	<link rel="stylesheet" href="/eis/inc/bootstrap/css/bootstrap.css" />
	<!-- link rel="stylesheet" href="/eis/inc/bootstrap/css/bootstrap-theme.css" /-->	
    <link href="/eis/inc/css/jquery-ui.css" rel="stylesheet"/>
    <link rel="stylesheet" href="jsp/Enroll/css/smart_wizard.css"  />
    <link href="/eis/inc/bootstrap/plugin/silviomoreto-bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet"/>

	<script src="/eis/inc/js/jquery.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui-timepicker-addon.js"></script>
	<script src="jsp/Enroll/js/jquery.smartWizard.js"></script>
	<!--script src="jsp/Enroll/js/jquery-confirm.js"></script-->	
	<script src="/eis/inc/bootstrap/js/bootstrap.min.js"></script>
	<!-- script src="/eis/inc/bootstrap/plugin/silviomoreto-bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script-->
    <!-- Include Bootstrap CSS -->
    
    <!-- Theme CSS -->
    <!--link href="jsp/StdProfileEdit/css/freelancer.min.css" rel="stylesheet"-->
    <!--link href="jsp/Enroll/css/adv.css" />
    <link href="jsp/StdProfileEdit/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css"-->
    <!--Optional SmartWizard theme >
    <link href="jsp/Enroll/css/smart_wizard_theme_circles.min.css" rel="stylesheet" type="text/css" />
    <link href="jsp/Enroll/css/smart_wizard_theme_arrows.min.css" rel="stylesheet" type="text/css" />
    <link href="jsp/Enroll/css/smart_wizard_theme_dots.min.css" rel="stylesheet" type="text/css" />
    <link href="jsp/Enroll/css/adv.css" rel="stylesheet" type="text/css" /-->
    
    
    
</head>
<body>



<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" style="font-size:24px;" href="PubEnroll">中華科技大學</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <!-- ul class="nav navbar-nav">
        <li class="active"><a href="#">台北校區 <span class="sr-only">(current)</span></a></li>
        <li><a href="#">新竹校區</a></li>
        <li><a href="#">雲林校區</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">訊息公告 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">台北校區</a></li>
            <li><a href="#">新竹校區</a></li>
            <li><a href="#">雲林校區</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">學術單位</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">行政單位</a></li>
          </ul>
        </li>
      </ul>      
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">電子計算機中心</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">相關連結 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#">教育部</a></li>
            <li><a href="#">技職司</a></li>
            <li><a href="#">北區高中職策略連盟</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">法規檢索</a></li>
          </ul>
        </li>
      </ul-->
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

    <div class="container">
        
       
         <form role="form" action="PubEnroll" method="post" class="form-signin" onsubmit="return confirm('已確認申請資料?');">  
   
        
        <!-- SmartWizard html -->
        
        
        <div id="smartwizard">
        	<c:if test="${empty std}">
            <ul>
                <li><a href="#step-1">開始<br /><small>請註冊或登入</small></a></li>
            </ul>
            </c:if>
            
            <c:if test="${!empty std}">
            <ul>
                <li><a href="#step-1">報名步驟 1<br /><small>填寫報考資料</small></a></li>
                <li><a href="#step-2">報名步驟 2<br /><small>填寫基本資料</small></a></li>
                <li><a href="#step-3">報名步驟 3<br /><small>填寫學歷資料</small></a></li>
                <li><a href="#step-4">報名步驟 4<br /><small>應附文件列表</small></a></li>
                <li><a href="#step-5">報名步驟 5<br /><small>完成報名</small></a></li>
            </ul>
            </c:if>
            <div>
                <div id="step-1" style="margin-top:20px;">                   
                    <div class="row">	                          
	                <div class="col-sm-12">
						<c:if test="${empty std}">
						<%@ include file="login.jsp"%>
						</c:if>						
						<c:if test="${!empty std}">                        
                        <div class="row control-group has-float-label">
						<div class="form-group col-xs-12 floating-label-form-group controls">
						報考項目
	                    <select onChange="showSchooList(this.value)" id="enroll1" class="form-control form-select">
                        	<option value="">選擇報名部制與學制</option>
                        	<c:forEach items="${enrolls}" var="e">
                        	<option value="${e.CampusNo}${e.SchoolNo}">${e.CampusName}${e.SchoolName} - ${e.enroll_name}</option>
                        	</c:forEach>
                       	</select>
		                <p class="help-block text-danger"></p>
		                </div>
						</div>                        
                        <div id="mchose1">                        
                        </div>                       
					</c:if>											
					</div>					            
            	</div>
                
                </div>
                <div id="step-2" style="margin-top:20px;">
				<%@ include file="profile.jsp"%>
                </div>
                <div id="step-3" class="">
				<%@ include file="education.jsp"%>
                </div>
                <div id="step-4" class="">
                <h2>需準備的文件 <small>於報名完成後提供電子檔上傳</small></h2>                    
                <div id="step-4_content"></div>
                </div>
                <div id="step-5" class="">
                <%@ include file="confirm.jsp"%>
                </div> 
            </div>
        </div>
        
        <c:if test="${!empty std}">
        <!-- div class="btn-group navbar-btn" role="group"-->
                <button class="btn btn-default btn-lg" id="prev-btn" type="button">上一步驟</button>
                <button class="btn btn-primary btn-lg" id="next-btn" type="button">下一步驟</button>
                <button class="btn btn-danger btn-lg" style="display:none" id="save-btn" name="method:save">確認報名資料</button>
        <!--/div-->
        </c:if>
    </div>
    
</form>
    
<div style="display:none;">
	
	
<c:forEach items="${enrolls}" var="e">
<div id="a${e.CampusNo}${e.SchoolNo}">
<ul class="list-group">
<c:forEach items="${e.attachs}" var="a">
<li class="list-group-item">${a.attach_name}<div style="float:right;">
<c:if test="${!empty a.online}"><button disabled="disabled"class="btn btn-default "><span class="glyphicon glyphicon-cloud-upload"> 允許提供電子檔</span></button></c:if>
<c:if test="${empty a.online}"><button disabled="disabled"class="btn btn-danger "><span class="glyphicon glyphicon-alert"> 不可提供電子檔</span></button></c:if>
</div></li>


</c:forEach>
</ul>
</div>
</c:forEach>
	
	

	
	
<c:forEach items="${enrolls}" var="e">                        
<div id="s${e.CampusNo}${e.SchoolNo}">
<c:forEach begin="1" end="${e.subsel}" varStatus="i">
<div class="row control-group has-float-label">
<div class="form-group col-xs-12 floating-label-form-group controls">
<select onChange="chooSchool(${e.Oid}, 'ch${e.Oid}${i.index}', this.value)" class="form-control form-select">                        
	<option>第${i.index}志願</option>
	<c:forEach items="${e.depts}" var="d"><option value="${d.Oid}">${d.dept_name}</option></c:forEach>                        
</select>
</div>
</div>
</c:forEach>
</div>                       
</c:forEach>


<c:forEach items="${enrolls}" var="e">                        
<div id="c${e.CampusNo}${e.SchoolNo}">
<c:forEach begin="1" end="${e.subsel}" varStatus="i">
<div class="row control-group has-float-label">
<div class="form-group col-xs-12 floating-label-form-group controls">
<select name="depts" id="ch${e.Oid}${i.index}" class="form-control form-select">                        
	<option value="">第${i.index}志願</option>
	<c:forEach items="${e.depts}" var="d"><option value="${d.Oid}">${d.dept_name}</option></c:forEach>                        
</select>
</div>
</div>
</c:forEach>
</div>                       
</c:forEach>


</div>
    

</div>
<script type="text/javascript">
function chooSchool(eOid, id, val){	
	$("#enrollOid").val(eOid);
	$("#"+id).val(val);
}

$(document).ready(function(){	
	
	$(".dateinput").datepicker({
		changeMonth: true,
		changeYear: true,
		//minDate: '@minDate',
		yearRange: "-40:-15",
		//showButtonPanel: true,
		//dateFormat: 'yymmdd'
		defaultDate: new Date(${school_year+1911-20}, 00, 01)
	});
	$('#webdialog').modal('show');	
    // Smart Wizard
    $('#smartwizard').smartWizard({ 
            selected: 0, 
            theme: 'default',
            transitionEffect:'fade',
            toolbarSettings: {toolbarPosition: 'both',
                              toolbarExtraButtons: [
                                    {label: 'Finish', css: 'btn-info', onClick: function(){ alert('Finish Clicked'); }},
                                    {label: 'Cancel', css: 'btn-danger', onClick: function(){ $('#smartwizard').smartWizard("reset"); }}
                                ]
                            }
    });
                                 
    
    // External Button Events
    $("#reset-btn").on("click", function() {
        // Reset wizard
        $('#smartwizard').smartWizard("reset");
        goTop();
        return true;
    });
    
    $("#prev-btn").on("click", function() {
        // Navigate previous
        $('#smartwizard').smartWizard("prev");
        goTop();
        return true;
    });
    
    $("#next-btn").on("click", function() {
        // Navigate next
        $('#smartwizard').smartWizard("next");
        goTop();
        return true;
    });
    
    $("#theme_selector").on("change", function() {
        // Change theme
        $('#smartwizard').smartWizard("theme", $(this).val());
        goTop();
        return true;
    });
    goTop();
});   

function goTop(){
	var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
	$body.animate({
		scrollTop: 0
	}, 1000);
}  

function showSchooList(sno){
	$("#mchose1").hide();
	$("#mchose1").html("");
	$("#mchose1").html($("#s"+sno).html());
	$("#mchose1").show("slow");
	$("#mchose").html("");
	$("#mchose").html($("#c"+sno).html());	
	
	$("#step-4_content").html("");
	$("#step-4_content").html($("#a"+sno).html());
}

function cp(n){
	if(n==4){
		$("#next-btn").hide("slow");
		$("#save-btn").show("slow");
		
	}else{
		$("#next-btn").show();
		$("#save-btn").hide("slow");
	}
	
	if(n==0){
		$("#prev-btn").hide("slow");
	}else{
		$("#prev-btn").show();
	}
	
	
	
	$("#enroll").val( $("#enroll1").val());	
	$("#student_name").val( $("#student_name1").val() );
	$("#CellPhone").val( $("#CellPhone1").val() );
	$("#telephone").val( $("#telephone1").val() );
	$("#Email").val($("#Email1").val());
	$("#dis").val($("#dis1").val());
	$("#low").val($("#low1").val());
	$("#curr_post").val($("#curr_post1").val());
	$("#curr_addr").val($("#curr_addr1").val());
	$("#perm_post").val($("#perm_post1").val());
	$("#perm_addr").val($("#perm_addr1").val());
	
	$("#schl_name").val($("#schl_name1").val());
	$("#grad_dept").val($("#grad_dept1").val());
	$("#perm_addr").val($("#perm_addr1").val());
	$("#education").val($("#education1").val());
	
}




</script>  
<c:if test="${!empty msg}">
	
	<div class="modal fade" id="webdialog" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">訊息</h4>
				</div>
				<div class="modal-body">
				<p class="lead">${msg.msg}</p>
				<p class="lead text-warning">${msg.warning}</p>
				<p class="lead text-error">${msg.error}</p>
				<p class="lead text-info">${msg.info}</p>
				<p class="lead text-success">${msg.success}</p>
				<p>點畫面任意處繼續...</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">關閉</button>
				</div>
			</div>
		</div>
		
		
	</div>
</c:if>
</body>
</html>
