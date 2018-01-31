<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>



<input type=hidden name="enrollOid" id="enrollOid" value="" /> 
<div class="row control-group has-float-label">
<div class="form-group col-xs-12 floating-label-form-group controls">
報考項目
<select id="enroll" class="form-control form-select" onchange="$(this).val($('#enroll1').val()),alert('請至第1步驟變更')">
   	<option value="">選擇報名部制與學制</option>
   	<c:forEach items="${enrolls}" var="e">
   	<option value="${e.CampusNo}${e.SchoolNo}">${e.CampusName}${e.SchoolName}</option>
   	</c:forEach>
</select>
<p class="help-block text-danger"></p>
</div>
</div>                  
<div id="mchose">

</div>
<div class="row">                
	                
<div class="col-lg-12">  
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	姓名
	<input readonly type="text" autocomplete="off" placeholder="請輸入中文姓名" id="student_name" name="student_name" value="${std.studentName}" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>
</div>

<div class="row">                
	                
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	身分證明文件號
	<input type="text" readonly autocomplete="off" name="idno" value="${std.idno}" class="form-control"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	生日
	<input type="text" readonly autocomplete="off" value="${fn:substring(std.birthday, 0, 10)}" class="form-control"/>
	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>


<div class="row">                
	                
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	行動電話
	<input type="text" readonly autocomplete="off" placeholder="請輸入常用手機號碼" id="CellPhone" name="CellPhone" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	固網電話
   	<input type="text" readonly autocomplete="off" placeholder="請輸入常用市話號碼" id="telephone" name="telephone" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
    <p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>


<div class="row">  
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	低收入戶   
    <select id="low" readonly name="low" class="form-control form-select" onchange="$(this).val($('#low1').val()),alert('請至第2步驟變更')">      
    <option value="0">不具低收入戶證明</option>
    <option value="1">具低收入戶證明</option>        	                      
    </select>
 	<p class="help-block text-danger"></p>
	</div>
</div>
</div>
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	身心障礙
    <select id="dis" readonly name="dis" class="form-control form-select" onchange="$(this).val($('#dis1').val()),alert('請至第2步驟變更')">    
 	<option value="0">不具身心障礙證明</option>	                     
    <option value="1">具身心障礙證明</option>                             
    </select>
    <p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>

<div class="row">                
	                
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	畢業學校
	<input type="text" readonly id="schl_name" name="schl_name" placeholder="請輸入中文校名" autocomplete="off" value="" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	畢業科系
	<input type="text" readonly id="grad_dept" name="grad_dept" placeholder="請輸入中文科系名稱" autocomplete="off" class="form-control"onClick="alert('請至第2步驟變更申請')"/>
   	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>



<div class="row">                
	                
<div class="col-lg-12">  
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	    同等學歷
	    <select id="education" name="education" readonly class="form-control form-select" onClick="alert('請至第3步驟變更申請')">
		<option value="">不採用同等學歷報考</option>
		<option value='1'>第五條第1款(大學肄(3))</option>
		<option value='2'>第五條第2款(大學肄(4))</option>
		<option value='3'>第五條第3款(大學肄(6))</option>
		<option value='4'>第五條第4款(專科畢業…)</option>
		<option value='5'>第五條第5款(國家考試及格…)</option>
		<option value='6'>第五條第6款(技能檢定合格…)</option>
		<option value='7'>第七條(專業領域具卓越成就表現…)</option>
		<option value='8'>第九條第5項(國外、港澳學歷…)</option>
		<option value='9'>第九條第8項(大陸學歷…)</option>
		</select>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>
</div>

<div class="row"> 
<div class="col-lg-3"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	通訊地郵編
	<input type="text" id="curr_post" readonly name="curr_post" autocomplete="off" placeholder="3或5碼郵遞區號" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-9"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	通訊地址
	<input type="text" id="curr_addr" readonly name="curr_addr" autocomplete="off" placeholder="郵寄通訊地址" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>

<div class="row">                
	                
<div class="col-lg-3"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	   戶籍地郵遞區號
	<input type="text" id="perm_post" readonly name="perm_post" autocomplete="off" placeholder="3或5碼郵遞區號"  class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-9"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	 戶籍地址
	<input type="text" readonly id="perm_addr" name="perm_addr" autocomplete="off" placeholder="申報各項政府資料用的地址" id="student_name"  class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>




<div class="row">                
	                
<div class="col-lg-12">  
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	電子郵件
	<input type="text" readonly autocomplete="off" placeholder="請輸入常用電子郵件信箱" id="Email" name="Email" value="${std.email}" class="form-control" onClick="alert('請至第2步驟變更申請')"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>
</div>
