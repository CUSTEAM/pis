<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script>$(function(){$('[data-submenu]').submenupicker();});</script>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button class="navbar-toggle" type="button" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand">中華科技大學</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">			
			<c:forEach items="${sysmenu}" var="m">
				<c:forEach items="${m.rule}" var="r">				
				<c:if test="${fn:indexOf(cookie['unit'].value, r.unit_id)>-1}">				
				<li class="dropdown">
					<a tabindex="0" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" data-submenu>${m.name}</a>
					<ul class="dropdown-menu">
					<c:forEach items="${m.menu}" var="mm">
					<c:if test="${fn:length(mm.menu)<1}">						
						<li><a href="${mm.path}"><i class="icon-list-alt" style="margin-top: 3px;"></i> ${mm.name}</a></li>
					</c:if>					
					<c:if test="${fn:length(mm.menu)>0}">					
					<li class="dropdown-submenu">
					<a tabindex="0"><i class="icon-calendar" style="margin-top: 5px;"></i> ${mm.name}</a>
						<ul class="dropdown-menu">
							<c:forEach items="${mm.menu}" var="mmm">							
							<c:if test="${fn:length(mmm.menu)<1}">
							<li><a href="${mmm.path}">${mmm.name}</a></li>
							</c:if>							
							<c:if test="${fn:length(mmm.menu)>0}">
							<li class="dropdown-submenu">
								<a tabindex="0" data-toggle="dropdown" data-submenu>${mmm.name}</a>
								<ul class="dropdown-menu">
								<c:forEach items="${mmm.menu}" var="mmmm">
									<li><a href="${mmmm.path}">${mmmm.name}</a></li>
								</c:forEach>
								</ul>
							</li>
							</c:if>						
							</c:forEach>
						</ul>
					</li>					
					</c:if>										
					</c:forEach>					
					</ul>				
				</li>				
				</c:if>				
				</c:forEach>			
			</c:forEach>
			</ul>
			<c:if test="${!empty cookie['unit']}">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="/eis/TaskManager">意見反應單</a></li>			
				<li class="dropdown"><a href="/eis/SeminarReg">活動報名</a></li>			
				<li class="dropdown"><a href="/eis/Logout">登出</a></li>
			</ul>
			</c:if>
		</div>
	</div>
</nav>
