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
    <link rel="stylesheet" href="jsp/Enroll_snato/css/adv.css" />
	<link rel="stylesheet" href="/eis/inc/bootstrap/css/bootstrap.css" />
	
    <link href="/eis/inc/css/jquery-ui.css" rel="stylesheet"/>
    <link rel="stylesheet" href="jsp/Enroll_snato/css/smart_wizard.css"  />
    <link href="/eis/inc/bootstrap/plugin/silviomoreto-bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet"/>

	<script src="/eis/inc/js/jquery.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
	
		
	<script src="/eis/inc/bootstrap/js/bootstrap.min.js"></script>	
    
    <link rel="stylesheet" href="jsp/Enroll/css/adv.css" />
	<link rel="stylesheet" href="/eis/inc/bootstrap/css/bootstrap.css" />

	<link rel="stylesheet" href="/eis/inc/css/jquery-ui.css" />
    <link rel="stylesheet" href="jsp/Enroll_snato/css/smart_wizard.css"  />
    <link rel="stylesheet" href="/eis/inc/bootstrap/plugin/silviomoreto-bootstrap-select/css/bootstrap-select.min.css" />
	
	<link rel="stylesheet" href="/eis/inc/js/plugin/jQuery-File-Upload/css/jquery.fileupload-ui.css">
	
	
	
	<script src="/eis/inc/js/plugin/jquery-ui-timepicker-addon.js"></script>
	<script src="jsp/Enroll_snato/js/jquery.smartWizard.js"></script>	
	
    <script src="/eis/inc/js/plugin/jQuery-File-Upload/js/vendor/jquery.ui.widget.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.iframe-transport.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.fileupload.js"></script>
	
	<script src="/eis/inc/js/plugin/jquery.blockUI.js"></script>
    
</head>
<body>



<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div >
      <center>
      <a  style="color:#888888" href="SNatoEnroll"><h2>中華科技大學新南向國際專班入學申請表<br>
                                          <small>New South-bound International program of Industry-Academia collaboration in CUST</small></h2></a></center>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

    <div class="container">
        
       
         <form role="form" action="SNatoEnroll" method="post" class="form-signin" onsubmit="return confirm('已確認申請資料?');">  
   
        
        <!-- SmartWizard html -->
        
        
        <div id="smartwizard">
        	
            <ul>
            	<li><a href="#step-1">開始<br /><small>Applied Department</small></a></li>
            	<li id="secondStep"><a href="#step-2">申請就讀科系<br /><small>Applied Department</small></a></li>
                <li id="threedStep"><a href="#step-3">申請人資料<br /><small>Personal Information</small></a></li>                
                <li ><a href="#step-4">繳交表件<br /><small>Form</small></a></li>
            </ul>
            
            
            <div>
                <div id="step-1" style="margin-top:20px;">                   
                    
                    

                 <div class="row">
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 護照號碼<small><br>Passport No</small>
							<input type="text" autocomplete="off" placeholder="" id="passport" value="" class="form-control"/>
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
				                    
				<div class="row">
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 出生日期<small><br>Date of Birth</small>
							<input style="font-size: 22;" class="form-control login-form-control dateinput" id="bdate" placeholder=""/>
			
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				</div>
                
   
                    
                    
                    
                    			            
            	</div>
                
                <div id="step-2" style="margin-top:20px;">
					<div class="row">
						<div class="col-lg-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							    請選擇科系 Applied Department / Ngành học đã đăng kí
							    <select id="education1" class="form-control form-select">
								<option value="IA">餐旅管理系 Department of Hospitality Managementsạn (Ngành quản lý nhà hàng khách )</option>
								<option value="1">機械工程系  Department of Mechanical Engineering(Kỹ sư cơ khí)</option>
								<option value="F">食品科學系  Department of  Food Science(Khoa Khoa học Thực phẩm)</option>
								</select>
							    <p class="help-block text-danger"></p>
							</div>
						</div>
						</div>
					</div>
                </div>
                <div id="step-3" class="">
				<%@ include file="profile.jsp"%>
                </div>
                
                <div id="step-4" class="">
                <h1 id="myNo"></h1>
				
				
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 切結書與授權書
							 <span id="f1_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f1_t"></span>
			                 </span>
			                 <small><br>The declaration and the letter of authorization. Bản cam kết và thư ủy quyền</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f1" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
					
					
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 學歷證明：中文或英文最高學歷證明影本
							 <span id="f2_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f2_t"></span>
			                 </span>
			                 <small><br>Educational Certificate：One photocopy of high school diploma in Chinese or English Chứng chỉ giáo dục: một bản sao bằng THPT bằng tiếng Trung hoặc tiếng Anh</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f2" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				
				
				
				
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 成績單：中文或英文最高學歷成績單證明影本
							 <span id="f3_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f3_t"></span>
			                 </span>
			                 <small><br>
								Transcripts：One photocopy of high school transcripts in Chinese or English.
								Học bạ/ Bảng điểm: một bản sao học bạ / bảng điểm bằng tiếng Trung hoặc tiếng Anh
							</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f3" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 身分證明影本
							 <span id="f4_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f4_t"></span>
			                 </span>
			                 <small><br>
								A copy of your identification card Một bản sao chứng minh thư nhân dân
							</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f4" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
					
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 護照或國籍證明影本
							 <span id="f5_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f5_t"></span>
			                 </span>
			                 <small><br>
								A copy of your passport or other proof of nationality Một bản sao hộ chiếu của bạn hoặc giấy tờ liên quan chứng nhận quốc tịch của bạn
							</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f5" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				
				
					<div class="col-sm-12">  
						<div class="row control-group has-float-label">
							<div class="form-group col-xs-12 floating-label-form-group controls">
							 其他有利文件 (如：語文證明、中文或英文自傳、證照、獎狀…等) 
							 <span id="f6_n" class="btn btn-link-2 fileinput-button" style="display:none;">
					        	<i class="glyphicon glyphicon-cloud-upload"></i>
					        	<span id="f6_t"></span>
			                 </span>
			                 <small><br>
								Other advantageous documents. (For example: Language ability certificates, autobiography in Chinese or English, certificates of examinations, awardsetc.) Các tài liệu có lợi khác ( Ví dụ : Chứng chỉ năng lực ngoại ngữ, tự truyện bằng tiếng Trung hoặc tiếng Anh, chứng chỉ các kì thi, giải thưởng,…)</small>
							<input class="fileInput" type="file" multiple name="myFile"  id="f6" onClick="$('#progress .bar').css('width','0%');">
							<p class="help-block text-danger"></p>
							</div>
						</div>
					</div>
				
                </div>
                
                
            </div>
        </div>
        
        
        <!-- div class="btn-group navbar-btn" role="group"-->
                <button class="btn btn-default btn-lg" id="prev-btn" type="button">上一步驟</button>
                <button class="btn btn-primary btn-lg" id="next-btn" type="button" disabled>下一步驟</button>
                
                <button type="button" style="display:none" id="save-btn" class="btn btn-primary btn-lg" onClick="showBox1();window.close();">結束填報</button>
        <!--/div-->
        
    </div>
    
</form>
    
<div class="container">

    <hr>
        <div class="text-center center-block">
            <p class="txt-railway">115311 台北市南港區研究院路三段245號 886 2-2782-1862 ~4 <br><small>No.245, Academia Rd. Sec. 3, Nangang Dist., Taipei City 115311, Taiwan (R.O.C.) </small></p>
            <br>
                <a href="https://www.facebook.com/bootsnipp"><i class="fa fa-facebook-square fa-3x social"></i></a>
	            <a href="https://twitter.com/bootsnipp"><i class="fa fa-twitter-square fa-3x social"></i></a>
	            <a href="https://plus.google.com/+Bootsnipp-page"><i class="fa fa-google-plus-square fa-3x social"></i></a>
	            <a href="mailto:bootsnipp@gmail.com"><i class="fa fa-envelope-square fa-3x social"></i></a>

    

</div>
<script type="text/javascript">
function showBox1(){

	$('#myModal').modal('show')
	$(".modal-body").html("報名已完成，您可以隨時上傳附件檔案。<br>若您還沒有準備好您的附件檔案，網頁關閉後也可以隨時從<a href='SNatoEnroll'>首頁</a>登入您的護照號碼上傳檔案。<br><br><small>Registration is been complete, you can login to this <a href='SNatoEnroll'>website</a> with your passport number after the page closed. All files can then be uploaded at any time.</small>");
	
}


var pno;

$("#f1").click(function() {clickFuntiion("f1")});
$("#f2").click(function() {clickFuntiion("f2")});
$("#f3").click(function() {clickFuntiion("f3")});
$("#f4").click(function() {clickFuntiion("f4")});
$("#f5").click(function() {clickFuntiion("f5")});
$("#f6").click(function() {clickFuntiion("f6")});



function clickFuntiion(fno){

	$("#"+fno).fileupload({
	    autoUpload: true,//自動上傳true
	    url: "putEnrollSnatoFtpFile",
	    type:"post",
	    dataType:"json",	  
	    formData:{
	        pno:pno,
			ftype:fno,
			m:Math.floor(Math.random()*11)
	    },        
	    //formData : {name:'pno',value:getPno()},  
	    done: function (e, data) {
	    	
	    	setTimeout(function(){$.unblockUI();},300);
	    	cp(1);
	    	$("#"+fno+"n").show("slow");
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

	
}





function chooSchool(eOid, id, val){	
	$("#enrollOid").val(eOid);
	$("#"+id).val(val);
}

$(document).ready(function(){	


	$("#passport").change(function(event) {		  
        
        if($("#passport").val().length>=6 && $("#bdate").val().length>=6){

        	$("#next-btn").prop('disabled', false);
        	$("#myNo").text($("#passport").val());
        	//$("#realNo").val($("#passport").val());
        	//pno=$("#passport").val();
        	pno = $("#passport").val();
        	//alert(pno);
        }

        	
  		
    });

	$("#bdate").change(function(event) {
		  
        //alert($("#passport").val().length);
        if($("#passport").val().length>=6 && $("#bdate").val().length>=6){
        	$("#next-btn").prop('disabled', false);
        	$("#myNo").text($("#passport").val());
        	//$("#realNo").val($("#passport").val());
        	//pno=$("#passport").val();
        	pno = $("#passport").val();
        	//alert(pno);
        }else{

        }

        	
  		
    });
	
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
	//alert($('#confirmAll').prop('checked'));
	var confirmAll=$('#confirmAll').prop('checked');
	if(n>=1){
		//判斷已註冊或返回
		$.ajax({
		url:"/pis/LoginOrRegister",
		type:"post",
		data:{
			sex:$("#sex").val(),
			cname:$("#cname").val(),
			ename:$("#ename").val(),
			tel_tw:$("#tel_tw").val(),
			tel_local:$("#tel_local").val(),
			cell:$("#cell").val(),
			email:$("#email").val(),
			depts:$("#education1").val(),
			bdate:$("#bdate").val(),
			addr_h:$("#addr_h").val(),
			addr_m:$("#addr_m").val(),
			birthplace:$("#birthplace").val(),
			nation:$("#nation").val(),
			passport:$("#passport").val(),
			married:$("#married").val(),
			children:$("#children").val(),
			emergency_c:$("#emergency_c").val(),
			emergency_e:$("#emergency_e").val(),
			emergency_tel:$("#emergency_tel").val(),
			emergency_email:$("#emergency_email").val(),
			emergency_addr:$("#emergency_addr").val(),
			school:$("#school").val(),
			school_addr:$("#school_addr").val(),
			school_date:$("#school_date").val(),
			school_m:$("#school_m").val(),
			school_s:$("#school_s").val(),
			c_years:$("#c_years").val(),
			c_school:$("#c_school").val(),
			c_test:$("#c_test").val(),
			c_score:$("#c_score").val(),
			c_l:$("#c_l").val(),
			c_s:$("#c_s").val(),
			c_r:$("#c_r").val(),
			c_w:$("#c_w").val(),
			money_0:$("#money_0").val(),
			money_1:$("#money_1").val(),
			money_2:$("#money_2").val(),
			money_2des:$("#money_2des").val(),
			money_3:$("#money_3").val(),
			money_3des:$("#money_3des").val(),
			health:$("#health").val(),
			health_des:$("#health_des").val(),
			confirmAll:confirmAll
		},


		dataType:"json",
		success:function(data){

			//alert(data.result.f2)

			if(data.result.status=="reged"){
				//已存在資料
				window.location.hash="#step-4";
				$("#next-btn").hide("slow");
				$("#save-btn").show("slow");
				$("#prev-btn").hide("slow");				
				$("#secondStep").hide("slow");
				$("#threedStep").hide("slow");
				$("#myNo").text($("#passport").val());


				
			}

			if(data.result.f1!=null){$("#f1_n").show();$("#f1_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f1+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f1+"</a>");}else{$("#f1_n").hide();}
			if(data.result.f2!=null){$("#f2_n").show();$("#f2_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f2+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f2+"</a>");}else{$("#f2_n").hide();}
			if(data.result.f3!=null){$("#f3_n").show();$("#f3_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f3+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f3+"</a>");}else{$("#f3_n").hide();}
			if(data.result.f4!=null){$("#f4_n").show();$("#f4_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f4+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f4+"</a>");}else{$("#f4_n").hide();}
			if(data.result.f5!=null){$("#f5_n").show();$("#f5_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f5+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f5+"</a>");}else{$("#f5_n").hide();}
			if(data.result.f6!=null){$("#f6_n").show();$("#f6_t").html("<a href='/pis/getFtpFile?path=enroll&file="+data.result.f6+"'><i class='glyphicon glyphicon-cloud-dowload'></i> "+data.result.f6+"</a>");}else{$("#f6_n").hide();}
		},
		error:function(err){
			console.log(err.statusText);
			console.log("異常");
		}
	})

		//未註冊者立即註冊並往下一頁
		

		//已註送至補件區
		
		
	}
	
	if(n==3){
		$("#next-btn").hide("slow");
		$("#save-btn").show("slow");
		showBox1();
	}else{
		$("#next-btn").show();
		$("#save-btn").hide("slow");
	}
	
	if(n==0){
		$("#prev-btn").hide("slow");		
	}else{
		$("#prev-btn").show();
	}
	
	
	
}









</script>  

	
	<!-- div class="modal fade" id="webdialog" tabindex="-1" role="dialog"
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
		
		
	</div-->
	
	
	
	<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">說明</h4>
      </div>
      <div class="modal-body"></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">關閉說明</button>
        <!-- a href="SNatoEnroll" class="btn btn-primary">回到首頁</a-->
      </div>
    </div>
  </div>
</div>

</body>
</html>
