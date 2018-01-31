<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="row">                
	                
<div class="col-sm-4">  
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	 姓名
	<input type="text" autocomplete="off" placeholder="請輸入中文姓名" id="student_name1" value="${std.studentName}" class="form-control"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>
            
	                
<div class="col-sm-4"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	    身分證明文件號
	    <input type="text" autocomplete="off" disabled value="${std.idno}" class="form-control"/>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-sm-4"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
        生日
        <input type="text" autocomplete="off" disabled value="${fn:substring(std.birthday, 0, 10)}" class="form-control"/>
        <p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>


<div class="row">                
	                
<div class="col-sm-4"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	    行動電話
	    <input type="text" autocomplete="off" placeholder="請輸入常用手機號碼" id="CellPhone1" value="${std.cellPhone}" class="form-control"/>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-sm-4"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
        固網電話
        <input type="text" autocomplete="off" placeholder="請輸入常用市話號碼" id="telephone1" value="${std.telephone}" class="form-control"/>
        <p class="help-block text-danger"></p>
    </div>
</div>
</div>

<div class="col-sm-4">  
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	    電子郵件
	    <input type="text" autocomplete="off" placeholder="請輸入常用電子郵件信箱" id="Email1" value="${std.email}" class="form-control"/>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>
</div>


<div class="row">  
<div class="col-sm-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	低收入戶
	<select id="low1" class="form-control form-select">      
	<option <c:if test="${std.low eq '0'}">selected</c:if> value="0">不具低收入戶證明</option>
	<option <c:if test="${std.low eq '1'}">selected</c:if> value="1">具低收入戶證明</option>        	                      
	</select>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>
<div class="col-sm-6"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	身心障礙
	<select id="dis1" class="form-control form-select">    
	<option <c:if test="${std.dis eq '0'}">selected</c:if> value="0">不具身心障礙證明</option>	                     
	<option <c:if test="${std.dis eq '1'}">selected</c:if> value="1">具身心障礙證明</option>                             
	</select>
	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>

<div class="row"> 
<div class="col-sm-3"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	通訊地郵編
	<input type="text" id="curr_post1" value="${std.currPost}" autocomplete="off" placeholder="3或5碼郵遞區號" class="form-control" onKeyUp="$('#perm_post1').val($(this).val())"/>
	<p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-sm-9"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	通訊地址
	<input type="text" id="curr_addr1" value="${std.currAddr}" autocomplete="off" placeholder="郵寄通訊地址" class="form-control" onKeyUp="$('#perm_addr1').val($(this).val())"/>
	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>

<div class="row">                
	                
<div class="col-sm-3"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	   戶籍地郵遞區號
	    <input type="text" id="perm_post1" value="${std.permPost}" autocomplete="off" placeholder="3或5碼郵遞區號" class="form-control"/>
	    <p class="help-block text-danger"></p>
	</div>
</div>
</div>

<div class="col-sm-9"> 
<div class="row control-group has-float-label">
	<div class="form-group col-xs-12 floating-label-form-group controls">
	戶籍地址
	<input type="text" id="perm_addr1" value="${std.permAddr}" autocomplete="off" placeholder="戶籍地址"  class="form-control"/>
   	<p class="help-block text-danger"></p>
    </div>
</div>
</div>
</div>





