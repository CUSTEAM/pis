<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>中華科技大學 報名咨詢</title>
<!-- custom-theme -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Course Register Form Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template,
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- //custom-theme -->
<!-- css files -->
<link href="//fonts.googleapis.com/css?family=Poppins:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&amp;subset=devanagari,latin-ext" rel="stylesheet">
<link href="//fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i&amp;subset=cyrillic,cyrillic-ext,greek,greek-ext,latin-ext,vietnamese" rel="stylesheet">
<link href="jsp/counselor/css/style.css" type="text/css" rel="stylesheet" media="all">
<!-- //css files -->

<link rel="stylesheet" href="css/font-awesome.css"> <!-- Font-Awesome-Icons-CSS -->

</head>
<!-- body starts -->
<body>
<!-- section -->
<section class="register">
	<div class="register-full">
		<div class="register-left">
			
				<div class="logo">
					<a href="#"><span class="fa fa-graduation-cap" aria-hidden="true"></span></a>
				</div>
				<h1>中華科技大學 報名預約</h1>
				<p>有關考試資訊、課程介紹、最新升學管道與報名資訊歡迎填單，我們會儘速與您聯繫！ </p>
				<!-- div class="content3">
					<ul>
						<li><a class="" href="www.cust.edu.tw"> 更多系所資訊</a></li>
						
					</ul>
				</div-->
			
		</div>
		<div class="register-right">
			<div class="register-in">
				<h2><span class="fa fa-pencil"></span> 填寫連絡資料</h2>
				<div class="register-form">
					<form action="Counselor" method="post">
						<div class="fields-grid">
							<div class="styled-input agile-styled-input-top">
								<input type="text" name="student_name" required="" value="${student_name}">
								<label>連絡人姓名</label>
								<span></span>
							</div>

							<div class="styled-input">
								<input type="tel" name="cell_phone" required="" value="${cell_phone}">
								<label>連絡電話</label>
								<span></span>
							</div>
							
							<c:forEach items="${Schools}" var="s">
								<button type="button" name="btnn" class="btn" onClick="$('#SchoolNo').val('${s.schNo}'),getSchool(), setColor(), $(this).addClass('btn-danger');">${s.schName}</button>
							</c:forEach>

							<select name="SchoolNo" id="SchoolNo" style="display:none">
									<c:forEach items="${Schools}" var="s">
									<option <c:if test='${s.id eq  SchoolNo}'>selected</c:if> value="${s.schNo}">${s.schName}</option>
									</c:forEach>
								</select>
							<!-- div class="styled-input agile-styled-input-top">
								<select name="SchoolNo" id="SchoolNo" required="">
									<c:forEach items="${Schools}" var="s">
									<option <c:if test='${s.id eq  SchoolNo}'>selected</c:if> value="${s.schNo}">${s.schName}</option>
									</c:forEach>
								</select>
								<span></span>
							</div-->




							<div class="styled-input agile-styled-input-top">
								<select name="DeptNo" id="DeptNo" required="">
									<option value="">請選擇系所*</option>

									<option></option>

								</select>
								<span></span>
							</div>






							<div class="styled-input" style="padding:15px;">
								<!--input type="text" name="note" value="${note}"-->
								<textarea  name="note" class="form-control">${note}</textarea>
								<label>附註</label>
								<span></span>
							</div>
							
							
							
							
							
							
							
							
							
							
							
							<div class="clear"> </div>
							 <label class="checkbox"><i></i>填寫以上資料按下送出，將有專人與您連繫</label>
						</div>
						<input type="submit" name="method:confirm" value="送出">
					</form>
				</div>
			</div>
			<div class="clear"> </div>
		</div>
	<div class="clear"> </div>
	</div>
	<!-- copyright -->
	<p class="agile-copyright">© <a href="www.cust.edu.tw" target="_blank">中華科技大學</a></p>
	<!-- //copyright -->
	<script>
	function setColor(){
		//alert($("#SchoolNo").val());
		$("[name='btnn']").removeClass( "btn-danger" )
		$("[name='btnn']").css("btn btn-lg");
	}
	function getSchool(){
		//alert($("#SchoolNo").val());
		switch ($("#SchoolNo").val()){
		  default:
		  case '':
		      $("#DeptNo option").remove();
		      break;
		  <c:forEach items="${Schools}" var="s">
		  case '${s.schNo}':
		      $("#DeptNo option").remove();
		      var array = [ <c:forEach items="${s.d}" var="d">'${d.name}',</c:forEach> ];
		      var sarray = [ <c:forEach items="${s.d}" var="d">'${d.id}',</c:forEach> ];
		      var tarray = [ <c:forEach items="${s.d}" var="d"><c:if test="${d.id eq SchoolNo}">'selected',</c:if><c:if test="${sarray[i] ne SchoolNo}">'',</c:if></c:forEach> ];
		      $.each(array, function(i, val) {
		    	  $("#DeptNo").append($("<option "+tarray[i]+"  value='" + sarray[i] + "'>" + array[i] + "</option>"));
		      });
		      break;
		  </c:forEach>
		 }
	}

	$("#SchoolNo").change(function(){
		getSchool();
	});

	$(document).ready(function () {
		getSchool();
	});
	</script>
</section>
<!-- //section -->
</body>
<!-- //body ends -->
</html>