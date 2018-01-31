<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="row">                
	                
<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	畢業學校
	<input type="text" id="schl_name1" value="${std.schlName}" placeholder="請輸入中文校名" autocomplete="off" value="" class="form-control"/>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-lg-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	畢業科系
	<input type="text" id="grad_dept1" value="${std.gradDept}" placeholder="請輸入中文科系名稱" autocomplete="off" class="form-control"/>
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
	    <select id="education1" class="form-control form-select">
		<option value="">依標準學歷報考</option>
		<option value='1'>第五條第1款 (大學肄(3年))</option>
		<option value='2'>第五條第2款 (大學肄(4年))</option>
		<option value='3'>第五條第3款 (大學肄(6年))</option>
		<option value='4'>第五條第4款 (專科畢)</option>
		<option value='5'>第五條第5款 (國家考試及格)</option>
		<option value='6'>第五條第6款 (技能檢定合格)</option>
		<option value='7'>第七條不分款(專業領域具卓越成就表現)</option>
		<option value='8'>第九條第5項 (國外、港澳學歷)</option>
		<option value='9'>第九條第8項 (大陸學歷)</option>
		</select>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>
</div>


