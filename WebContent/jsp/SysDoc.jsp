<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>說明</title>
<script src="/eis/inc/js/plugin/bootstrap-scrollspy.js"></script>
<script src="/eis/inc/js/plugin/jquery-ui.js"></script>
<script src="/eis/inc/js/plugin/bootstrap-tooltip.js"></script>
<link href="/eis/inc/css/jquery-ui.css" rel="stylesheet" />

<style>
.well{
	min-height:20px;
    padding:19px;
    margin-bottom:20px;
    background-color:#ffffff;
    border:1px solid #999;
    -webkit-border-radius:4px;
    -moz-border-radius:4px;
    border-radius:4px;
    -webkit-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);
    -moz-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);
    box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05)
}
.tree {
    background-color:#fbfbfb;
}
.tree li {
    list-style-type:none;
    margin:0;
    padding:10px 5px 0 5px;
    position:relative
}
.tree li::before, .tree li::after {
    content:'';
    left:-20px;
    position:absolute;
    right:auto
}
.tree li::before {
    border-left:1px solid #999;
    bottom:50px;
    height:100%;
    top:0;
    width:1px
}
.tree li::after {
    border-top:1px solid #999;
    height:20px;
    top:25px;
    width:25px
}
.tree li span {
    -moz-border-radius:5px;
    -webkit-border-radius:5px;
    border:1px solid #999;
    border-radius:5px;
    display:inline-block;
    padding:3px 8px;
    text-decoration:none
}
.tree li.parent_li>span {
    cursor:pointer
}
.tree>ul>li::before, .tree>ul>li::after {
    border:0
}
.tree li:last-child::before {
    height:30px
}
.tree li.parent_li>span:hover, .tree li.parent_li>span:hover+ul li span {
    background:#eee;
    border:1px solid #94a0b4;
    color:#000
}
</style>
<script>




$(function () {
    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', '關閉');
    $('.tree li.parent_li > span').on('click', function (e) {
        var children = $(this).parent('li.parent_li').find(' > ul > li');
        if (children.is(":visible")) {
            children.hide('fast');
            $(this).attr('title', '展開').find(' > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');
        } else {
            children.show('fast');
            $(this).attr('title', 'Collapse this branch').find(' > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');
        }
        e.stopPropagation();
    });
});
</script>
</head>
<!-- body data-spy="scroll" data-target="#navbarExample" data-offset="0"-->
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<div style="position: fixed; left:0px; top:0px;" class="alert">
<button type="button" class="close" onClick="window.parent.$.unblockUI()">&times;</button>
<strong>教學評量</strong> 
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span4">
		<div class="tree well">
		    <ul>
		        <c:forEach items="${sys}" var="s">
		        <li><span><i class="icon-folder-open"></i> ${s.Oid}, ${s.name}</span>        
		            <ul>                
		                <c:forEach items="${s.subSys}" var="ss">	
		                <li class="after"><span><i class="icon-plus-sign"></i> ${ss.Oid}, ${ss.name}</span>
		                    <ul>
		                    	<c:forEach items="${ss.qa}" var="qa">
		                    	<li><span><i class="icon-minus-sign"></i> ${qa.title}</span></li>
		                    	</c:forEach>
		                        <c:forEach items="${ss.help}" var="help">
		                    	<li><span><i class="icon-minus-sign"></i> ${help.title}</span></li>
		                    	</c:forEach>
		                        
		                    </ul>
		                </li>
		                </c:forEach>                
		            </ul>
		        </li>
		        </c:forEach>
		    </ul>
		</div>
		
		
		</div>
		<div class="span8">
		<div class="well"></div>
		</div>
	</div>
</div>






<button onClick="window.parent.$.unblockUI()">關閉</button>
<script>
$(".tree li:has(ul)").find( "li" ).hide('fast');
</script>
</body>
</html>