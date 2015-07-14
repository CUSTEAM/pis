<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.box {
	width: 300px;
	height: 300px;
	box-shadow: 1px 5px 5px #666;
	
	max-width: 300px;
	padding: 19px 29px 29px;
	background-color: #f8f8f8;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}
</style>
<script>
	function checkout() {

		if ($("#birthday").val().length >= 5
				&& $("#idno").val().length >= 9) {
			$("#parentNameArea").show();
			$.ajax({
				type : "POST",
				url : "parentSuggestion",
				dataType : "json",
				data : {
					idno : $("#idno").val()
				},
				success : function(d) {
					//$("#tmp").html(JSON.stringify(d));
					//callback(d.list);
					var popover = $("#parentName").data('popover');
					$('#parentName').popover("show");
					$("#parentName").attr('data-content', d.sugg);
					popover = $("#parentName").data('popover');
					popover.setContent();
					popover.$tip.addClass(popover.options.placement);
					setTimeout(function() {
						$('#parentName').popover("hide");
					}, 5000);
				}
			});

		} else {
			$("#parentNameArea").hide();
		}

	}

	$(document).ready(function() {

		$("#birthday").keypress(function() {
			checkout();
		});

		$("#idno").keypress(function() {
			checkout();
		});

		$('#q1').popover("show");

		setTimeout(function() {
			$('#q1').popover("hide");
		}, 10000);
		
	    $('.carousel').carousel({
	        interval: 5000
	        });

	});
</script>

				


<div class="container">

      <div class="masthead">
        <h3 class="muted">家長資訊平台</h3>
        
      </div>
      
      
      
      
      
      
      
      
      
      <div id="myCarousel" class="carousel slide">
                <ol class="carousel-indicators">
                  <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                  <li data-target="#myCarousel" data-slide-to="1"></li>
                  <li data-target="#myCarousel" data-slide-to="2"></li>
                </ol>
                <div class="carousel-inner">
                  <div class="item active">
                    <img src="/pis/img/head_dilg.gif" alt="">
                    <div class="carousel-caption">
                      <h4>瞭解學生出席狀況</h4>
                      <p>提供貴子弟各項缺課資訊、加總與統計</p>
                    </div>
                  </div>
                  <div class="item">
                    <img src="/pis/img/head_score.gif" alt="">
                    <div class="carousel-caption">
                      <h4>瞭解學生學習狀況</h4>
                      <p>提供歷年成績、走勢圖、平均分數與名次圖表</p>
                    </div>
                  </div>
                  <div class="item">
                    <img src="/pis/img/head_score.gif" alt="">
                    <div class="carousel-caption">
                      <h4>瞭解教師輔助學習狀況</h4>
                      <p>提供歷年來教師對貴子弟的個別輔導記錄與細節</p>
                    </div>
                  </div>
                </div>
                <a class="left carousel-control" href="#myCarousel" data-slide="prev">&lsaquo;</a>
                <a class="right carousel-control" href="#myCarousel" data-slide="next">&rsaquo;</a>
              </div>
      
      
      
      
      
      
      
      


      <hr>

      <!-- Example row of columns -->
      <div class="row-fluid">
        <div class="span4">
          <div class="box">
	<form action="Parent" method="post" name="ParentLogin" class="form-signin">
		
		<div class="input-prepend">
			<span class="add-on">身份證號 </span> <input type="text" name="idno"
				id="idno" size="12" placeholder="子女身份證號">
		</div>

		<div class="input-prepend">
			<span class="add-on">出生日期</span> <input type="password" size="12"
				name="birthday" id="birthday" placeholder="民國年月日">
		</div>

		<div class="input-prepend" id="parentNameArea"
			style="display: none;">
			<span class="add-on">家長代表</span> <input type="text" size="12"
				name="parentName" id="parentName" placeholder="請輸入家長中文全名"
				rel="popover" title="提示" data-content="" data-placement="bottom">
		</div>

		<label class="checkbox"> </label> <input type="submit" id="login"
			name="method:login" value="登入" class="btn btn-danger" /> <input
			type="button" value="?" class="btn btn-warning" />
	</form>
	<img src="img/androidlogo.png"/>
</div>
        </div>
        <div class="span4">
          <h2>登入方式</h2>
          <p>身份證號欄位請輸入貴子弟身分證號。</p>
          <p>密碼欄位為貴子弟的出生日期。例如貴子弟的出生日期為88年8月18日，您的密碼欄位請輸入880818。</p>
          <p>依畫面提示輸入家長姓名，即可登入平台。</p>
        </div>
        <div class="span4">
          <h2>注意事項</h2>
          <p>家長資訊平台提供家長即時查詢貴子弟在校當學期缺曠、獎懲、選課、成績、導師及歷年成績等資訊。</p>
          <p>由於本校資訊安全規定，資料查詢時不會同時顯示貴子弟大名與相關資料的直接對應。</p>
          <p>所有記錄依各權責單位，於學期末結算為準。</p>
       </div>
       
      </div>

      <hr>

      <div class="footer">
        <p>&copy; 中華科技大學</p>
      </div>

    </div> <!-- /container -->