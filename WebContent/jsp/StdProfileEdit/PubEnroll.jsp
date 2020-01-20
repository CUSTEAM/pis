<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-tw">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	<meta name="decorator" content="none"/>
    <title>中華科技大學</title>
	<link rel="stylesheet" href="/eis/inc/js/plugin/jQuery-File-Upload/css/jquery.fileupload-ui.css">
	<link href="/eis/inc/css/jquery-ui.css" rel="stylesheet"/>
    <!-- Bootstrap Core CSS -->
    <link href="/eis/inc/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- Theme CSS -->
    <link href="jsp/StdProfileEdit/css/freelancer.min.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="jsp/StdProfileEdit/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
	<style>
	
	</style>
</head>

<body id="page-top" class="index">
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
<form role="form" action="PubEnroll" method="post"class="registration-form">
<div id="skipnav"><a href="#maincontent">Skip to main content</a></div>

    <!-- Navigation -->
    <nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand" href="#page-top">中華科技大學 網路報名</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="page-scroll">
                        <c:if test="${empty std}"><a href="#page-top">登入</a></c:if>
                        <c:if test="${!empty std}"><a href="#regist">考試報名</a></c:if>
                    </li>
                    <c:if test="${!empty std}">
                    <li class="page-scroll">
                        <a href="#portfolio">基本資料</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#about">家庭資料</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#contact">連絡資料</a>
                    </li>
                    </c:if>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>

    <!-- Header -->
    <c:if test="${empty std}">
    <header>
        <div class="container" id="maincontent" tabindex="-1">
            <div class="row">
                
                            
                
                <c:if test="${empty std}">
                          
                <div class="col-lg-12">                
                	<div>
							<div class="form-top">
								<div class="form-top-left">
									<h3>開始報名或登入已建立的帳號</h3>
									<p>請輸入身分證號與出生日期開始</p>
								</div>
								<div class="form-top-right">
									<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</div>
							</div>
							<div class="form-bottom">
								
									<div class="form-group">
										<label class="sr-only" for="form-first-name">身分證號</label> <input
											type="text" name="idno" placeholder="身分證號 (報名登記的證件號碼如:身分證、居留證、護照)"
											class="login">
									</div>
									<div class="form-group">
										<label class="sr-only" >西元出生日期</label> <input
											type="text" name="birthday" placeholder="西元出生日期  (如88年8月8日出生為1999-08-08)"
											class="login dateinput">
									</div>
									<label>
									<button name="method:login" type="submit" class="btn">驗證基本資料</button>
									報名、查榜、報到等相關作業
									</label>
							</div>
						</div> 
				</div>
				
				
                </c:if>                
            </div>
        </div>
    </header>
	</c:if>
	<c:if test="${!empty std}">
	
	
	
	
	
	
	
	<section id="regist">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2>考試報名</h2>
                    <br>
                    <hr class="info-primary">
                </div>
            </div>
            
            
            <div class="panel-group" id="accordion">  
  
			  <c:forEach items="${enrolls}" var="e">
			  <div class="panel panel-primary">
			    <div class="panel-heading" > 
			      <p class="panel-title" data-toggle="collapse" data-parent="#accordion" href="#collapse${e.CampusNo}${e.SchoolNo}">${e.CampusName}${e.SchoolName}</p>
			    </div>
			    <div id="collapse${e.CampusNo}${e.SchoolNo}" class="accordion-body collapse">
			      	<div class="accordion-inner">
			      		<ul class="list-group">
			      		<c:forEach items="${e.depts}" var="d">
					  	<li class="list-group-item">${d.dept_name} ,${d.quota}</li>
					  	</c:forEach>
						</ul>
			      
			      
			      </div>
			    </div>
			  </div>
			  </c:forEach>
  
  
  
  
			</div>
                       
        </div>
    </section>
	
	
	
	
	
	
	
    <!-- Portfolio Grid Section -->
    <section id="portfolio">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2>填寫基本資料</h2>
                    <br>
                    <hr class="info-primary">
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">   
                
				<div class="row control-group">
				
				</div>				
                    
               	<div class="row control-group">
				
				</div>                    
                    
                <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="schl_name">應屆或非應屆畢業校</label>畢業學校
                    <input type="text" autocomplete="off" placeholder="應屆或非應屆畢業校" id="schl_name" name="schl_name" value="" class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
                    
                <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="grad_dept">畢業科系</label>畢業科系
                    <input type="text" autocomplete="off" placeholder="畢業科系" name="grad_dept" id="grad_dept" value="" class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
                    
                </div>
                
                <div class="col-md-6">
                
                <div class="row control-group">
				
				</div>
                
                <div class="row control-group">
				
				</div>
                
                <div class="row control-group">
				
				</div>
                </div>                
            </div>            
        </div>
    </section>

    <!-- About Section -->
    <section class="success" id="about">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2>家庭資料</h2><small>登記完成後不再顯示</small><br><br>
                    <hr class="fam-light">
                </div>
            </div>
            <div class="row">
                
                <div class="col-md-12">
                
                <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="parent_name">緊急連絡人</label>家長姓名
                    <input style="color:#ffffff;" type="text" autocomplete="off" 
                    placeholder="緊急連絡人姓名" id="parent_name" name="parent_name" 
                    value="" class="aform form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
				
				<div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="telephone">連絡電話</label>連絡電話
                    <input style="color:#ffffff;" type="text" autocomplete="off" 
                    placeholder="緊急連絡人電話" id="telephone" name="telephone" value="${std.telephone}"
                    class="form-control"/><p class="help-block text-danger"></p>
                </div>
				</div>
				
				<div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="perm_post">戶籍地址郵遞區號</label>郵遞區號
                    <input style="color:#ffffff;" type="text" autocomplete="off" 
                    placeholder="戶籍地址郵遞區號" id="perm_post" name="perm_post" value="${std.permPost}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
				
				<div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="liner">戶籍地址所在里</label>戶籍里
                    <input style="color:#ffffff;" type="text" autocomplete="off" 
                    placeholder="戶籍地址所在里" id="liner" name="liner" value="${std.liner}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
				
				<div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="perm_addr">戶籍地址不需填寫鄰里資料</label>戶籍地址
                    <input style="color:#ffffff;" type="text" autocomplete="off" 
                    placeholder="戶籍地址 (不需填寫鄰里資料)" id="perm_addr" name="perm_addr" value="${std.permAddr}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
				</div>
                
                </div>                
            </div>
        </div>
    </section>

    <!-- Contact Section -->
    <section id="contact">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2>連絡資料</h2><br>
                    <hr class="cont-primary">
                </div>
            </div>
            <div class="row">
			
			
            
            <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="grad_dept">行動電話</label>行動電話
                    <input type="text" autocomplete="off" 
                    placeholder="個人連絡電話" id="CellPhone" name="CellPhone" value="${std.cellPhone}"
                    class="form-control"/><p class="help-block text-danger"></p>
                </div>
			</div>                
                
           <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="curr_post">通訊地址郵遞區號</label>郵遞區號
                    <input type="text" autocomplete="off" 
                    placeholder="通訊地址郵遞區號" id="curr_post" name="curr_post" value="${std.currPost}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
			</div>
				
			<div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="curr_addr">通訊地址不需填寫鄰里資料</label>通訊地址
                    <input type="text" autocomplete="off" 
                    placeholder="通訊地址" id="curr_addr" name="curr_addr" value="${std.currAddr}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
			</div>
                
           <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                    <label for="perm_addr">慣用的電子郵件信箱</label>電子郵件
                    <input type="text" autocomplete="off" 
                    placeholder="慣用的電子郵件信箱" id="Email" name="Email" value="${std.email}" 
                    class="form-control"/>
                    <p class="help-block text-danger"></p>
                </div>
			</div>
         
           <div class="row control-group">
				<div class="form-group col-xs-12 floating-label-form-group controls">
                   <br>
                   <button class="btn btn-danger " name="method:save" 
                   type="submit" onClick="$.blockUI({message:null});">儲存輸入資料</button>
                   <br>
                   <input type="checkbox" checked disabled> 同意個人資料於校內各項工作使用
               </div>
			</div>
                
            </div>
        </div>
    </section>
</c:if>



    <!-- Footer -->
    <footer class="text-center">
        <div class="footer-above">
            <div class="container">
                <div class="row">
                    <div class="footer-col col-md-4">
                        <h3>連絡學校</h3>
                        <p>南港區研究院路三段245號<br>27821862 教務126 學務138</p>
                    </div>
                    <div class="footer-col col-md-4">
                        <h3>網路資源</h3>
                        <div class="social-login">
	                        	
	                        	<div class="social-login-buttons">
		                        	<a class="btn btn-link-2" href="#">中華首頁</a>
		                        	<a class="btn btn-link-2" href="#">電子郵件</a>
		                        	<a class="btn btn-link-2" href="#">資訊系統</a>
	                        	</div>
	                        </div>
                    </div>
                    <div class="footer-col col-md-4">
                        <h3>關於學籍簡卡</h3>
                        <p>依規定入學前需建立基本資料<br>由教/學務指示電算中心建置輸入平台</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer-below">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        中華科技大學
                    </div>
                </div>
            </div>
        </div>
    </footer>


    <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
    <div class="scroll-top page-scroll hidden-sm hidden-xs hidden-lg hidden-md">
        <a class="btn btn-primary" href="#page-top">
            <i class="fa fa-chevron-up"></i>
        </a>
        
        

    
    
    
    
    
    
    
    
    
    <!-- Portfolio Modals -->
    
    
    
    
    
    
	
    <!-- jQuery -->
    
    <script src="/eis/inc/js/jquery.js"></script>
    
	<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
	<script src="/eis/inc/js/plugin/jquery-ui-timepicker-addon.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/eis/inc/bootstrap/js/bootstrap.min.js"></script>

    <!-- Theme JavaScript -->
    <script src="jsp/StdProfileEdit/js/freelancer.min.js"></script>
    
    <script src="/eis/inc/js/plugin/jQuery-File-Upload/js/vendor/jquery.ui.widget.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.iframe-transport.js"></script>
	<script src="/eis/inc/js/plugin/jQuery-File-Upload/js/jquery.fileupload.js"></script>
	
	<script src="/eis/inc/js/plugin/jquery.blockUI.js"></script>
	
	


	
	
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
	                	$("#files").html("<img class='img-circle' src='/eis/getStdimage?myStdNo=${myStdNo}&t="+Math.floor(Math.random()*999)+"'>");
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
/*var Arraycolor=new Array("#18BC9C","#abd3d8","#99CCFF","#FFCCFF","#FFCC99");
var n=0;
function turncolors(){
	n++;
	if (n==(Arraycolor.length-1)) n=0;
  
	
	$("header").css('background-color', Arraycolor[n]);



  	setTimeout("turncolors()",1000);
}
turncolors();*/
//$("input[name='beginDate'], input[name='endDate']" ).datepicker();
</script>
</form>
</body>

</html>
