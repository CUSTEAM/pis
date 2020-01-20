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
	
	
	
    <link rel="stylesheet" href="jsp/Enroll/css/adv.css" />
	<link rel="stylesheet" href="/eis/inc/bootstrap/css/bootstrap.css" />

	<link rel="stylesheet" href="/eis/inc/css/jquery-ui.css" />
    <link rel="stylesheet" href="jsp/Enroll/css/smart_wizard.css"  />
    <link rel="stylesheet" href="/eis/inc/bootstrap/plugin/silviomoreto-bootstrap-select/css/bootstrap-select.min.css" />
	
	<link rel="stylesheet" href="/eis/inc/js/plugin/jQuery-File-Upload/css/jquery.fileupload-ui.css">
	
	<script src="/eis/inc/js/jquery.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui-timepicker-addon.js"></script>
	<script src="jsp/Enroll/js/jquery.smartWizard.js"></script>	
	<script src="/eis/inc/bootstrap/js/bootstrap.min.js"></script>
	 
    <script src="/eis/inc/js/plugin/jQuery-File-Upload/js/vendor/jquery.ui.widget.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.iframe-transport.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.fileupload.js"></script>
	
	<script src="/eis/inc/js/plugin/jquery.blockUI.js"></script>
    
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
        
       
	<form role="form" action="EnrollView" method="post" class="form-signin" onsubmit="return confirm('已確認申請資料?');">  
		<div id="smartwizard">
        <ul>
			<li><a href="#step-1">考生資訊服務<br /><small>檢視各項報名資料或補件</small></a></li>
        </ul>
        <div>
	        <div id="step-1" style="margin-top:20px;">
		        <div class="row">
					<div class="col-sm-6">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							報名考試項目
							<input readonly type="text" disabled value="${oStd.enroll.enroll_name}" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
					<div class="col-sm-6">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							准考證
							<c:if test="${empty oStd.reg.no}"><input readonly type="text" disabled value="未核發" style="width:100%;" class="form-control" /></c:if>
							<c:if test="${!empty oStd.reg.no}"><input readonly type="text" disabled value="${oStd.reg.no}" style="width:100%;" class="form-control" /></c:if>
							
							
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				
				
				
				<c:if test="${o_score}">
				<div class="row">
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-3 floating-label-form-group controls">
							筆試
							<input readonly type="text" disabled value="${oStd.reg.score1}" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
							<div class="form-group col-sm-3 floating-label-form-group controls">
							口試
							<input readonly type="text" disabled value="${oStd.reg.score2}" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
							<div class="form-group col-sm-3 floating-label-form-group controls">
							書面
							<input readonly type="text" disabled value="${oStd.reg.score3}" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
							<div class="form-group col-sm-3 floating-label-form-group controls">
							總成績
							<input readonly type="text" disabled value="${oStd.reg.score}" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				</c:if>
				
				<c:forEach items="${oStd.dept}" var="d" varStatus="i">
				<div class="row">
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							第${i.index+1}志願 							
							<c:if test="${!o_match}">
							<input readonly type="text" disabled value="${d.CampusName}${d.SchoolName} - ${d.dept_name}" class="form-control" />
							</c:if>
							
							<c:if test="${o_match}">							
							<c:choose>         
								<c:when test="${empty d.rank}">
					            <input readonly type="text" disabled value="${d.CampusName}${d.SchoolName} - ${d.dept_name} 【未錄取】" class="form-control" />
					         	</c:when>					         
					         	<c:when test="${d.rank==0}">						            
					            <c:if test="${!empty d.checkin}">
					         	<button class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-bookmark" aria-hidden="true"></span> 已報到</button>			            
					            </c:if>
					            
					            <input readonly type="text" disabled value="${d.CampusName}${d.SchoolName} - ${d.dept_name} 【正取】" class="form-control" />
					            </c:when>
					         	<c:when test="${d.rank>0}">
					            <input readonly type="text" disabled value="${d.CampusName}${d.SchoolName} - ${d.dept_name} 【備取${d.rank}】" class="form-control" />
					         	</c:when>
					         	<c:otherwise>
					            No comment sir...
					         	</c:otherwise>
					     	</c:choose>
							
							</c:if>
							
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				</c:forEach>
				
				<div class="row">
					<div class="col-sm-4">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							簡章
							<input style="cursor: pointer;" onClick="window.open('/pis/getFtpFile?path=enroll&file=${oStd.enroll.brochure}');" type="text" readonly value="下載簡章" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
					
					<div class="col-sm-4">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							報名表 
							<input style="cursor: pointer;" onClick="window.open('/pis/EnrollDoc?idno=${oStd.idno}');" type="text" readonly value="下載報名表" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				
					<div class="col-sm-4">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							信封 
							<input style="cursor: pointer;" onClick="window.open('/pis/EnrollEnvelope?&idno=${oStd.idno}');" type="text" readonly value="下載專用信封" class="form-control" />
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				<input type="hidden" id="erOid" value="${oStd.reg.Oid}" />
				
				<c:forEach items="${oStd.atta}" var="a" varStatus="i">
				<div class="row">
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-sm-12 floating-label-form-group controls">
							${a.attach_name}<c:if test="${empty a.online}"> <small><span class="label label-default">請郵寄</span></small></c:if><input type="hidden" name="attOid" value="${a.eOid}"/>					
							<span class="btn btn-link-2 fileinput-button">
				        	<i class="glyphicon glyphicon-cloud-upload"></i>
				        	<span> 上傳檔案</span>
				        	<input type="file" multiple name="myFile"  id="fileupload${a.eOid}" onClick="$('#progress .bar').css('width','0%');">
				    		</span>				    		
		                    <c:if test="${empty a.path}">
		                    <span class="btn btn-link-2 fileinput-button" id="f${a.eOid}">		                    
		                    </span>
		                    </c:if>
		                    <c:if test="${!empty a.path}">
		                    <span class="btn btn-link-2 fileinput-button" id="f${a.eOid}">
				        	<i class="glyphicon glyphicon-cloud-upload"></i>
				        	<a href="/pis/getFtpFile?path=enroll&file=${a.path}"><i class="glyphicon glyphicon-cloud-dowload"></i> 下載檔案</a>
		                    </span>
		                    </c:if>
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				</c:forEach>				
	        </div>
        </div>    
		</div>
		</form>
	</div>

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

<script type="text/javascript">
//var url="putEnrollFtpFile";
//這貨他媽的不能動態載入物件, 連return做都轉成字串
<c:forEach items="${oStd.atta}" var="a" varStatus="i">
$("#fileupload${a.eOid}").fileupload({
    autoUpload: true,//自動上傳true
    url: "putEnrollFtpFile?useid=enroll&eOid=${a.eOid}&erOid="+$("#erOid").val()+"&m="+Math.floor(Math.random()*11),
    //formData:function(){return{useid:"enroll",idno:$("#idno").val(),eOid:}},
    dataType:"json",	        
    done: function (e, data) {
    	$("#f${a.eOid}").html("<a href='/pis/getFtpFile?path=enroll/&file="+data.result.newFileName+"'><i class='glyphicon glyphicon-cloud-download'></i> 下載檔案</a>");
    	setTimeout(function(){$.unblockUI();},300);
    	
    },
    progressall: function (e, data) {
    	$.blockUI({ 
            theme:     true, 
            title:    "檔案處理中", 
            message:  "<div id='progress' class='progress progress-success progress-striped'><div class='bar'></div></div>"
        }); 
        var progress = parseInt(data.loaded / data.total * 100, 10);
        $('#progress .bar').css('width',progress+'%');
    }
});	
</c:forEach>



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

function cp(n){
	
	

}


	   
	    

function ImgError(source){  
    source.src = "/eis/img/icon/User-Black-Folder-icon.png";  
    source.onerror = "";  
    return true;  
}

$(window).load(function() {
	$('#webdialog').modal('show');
});

function showInage(stdNo){
	$.blockUI({ 
		css: { 
            top:  ($(window).height() - 200) /2 + 'px', 
            left: ($(window).width() - 200) /2 + 'px', 
            width: '200px' 
        } ,
		message: "<img onerror='ImgError(this);' src='/eis/getStdimage?myStdNo=${myStdNo}&t="+Math.floor(Math.random()*999)+"'><br>點畫面任意處關閉...", 
        //timeout: 2000 
        
        
    }); 
	$('.blockOverlay').attr('title','Click to unblock').click($.unblockUI); 
}
$(".dateinput").datepicker({
	changeMonth: true,
	changeYear: true,
	//minDate: '@minDate',
	yearRange: "-40:-15",
	//showButtonPanel: true,
	//dateFormat: 'yymmdd'
	defaultDate: new Date(${school_year+1911-20}, 00, 01)
});

//$("input[name='beginDate'], input[name='endDate']" ).datepicker();
</script>
</body>
</html>
