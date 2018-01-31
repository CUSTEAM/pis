<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="panel panel-primary">
	<div class="panel-heading"><h3 class="panel-title">考生報名 - 或登入已報名的資料</h3></div>
	<div class="panel-body">
    	<div class="form-group">
       		<label for="logidno">身分證明文件</label>
				<input class="form-control login-form-control" id="logidno" name="logidno"  placeholder="報名登記的證件號碼如:身分證、居留證、護照"/>
				
			
                             
        </div>
       	<div class="form-group">
			<label for="logbirthday">西元出生日期</label>
			<input class="form-control login-form-control dateinput" id="logbirthday" name="logbirthday"  placeholder="如88年8月8日出生為1999-08-08"/>
			
			
		</div>			                            
		<input type="submit" id="login" name="method:login" value="驗證基本資料" class="btn btn-danger"/>
		報名、查榜、報到等相關作業               
	</div>
</div>