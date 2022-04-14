<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</head>
<body>
<%@ include file="0_top.jsp" %>
</br>
	<!-- 로그인 양식 -->
	<div>
		<div>
			<div style="padding-top: 20px;">
				<form method="post" action="loginAction.jsp">
					<h5>로그인</h5>
					<div>
						<input type="text" placeholder="아이디" name="userID" maxlength="20">
					</div>
					<div>
						<input type="password" placeholder="비밀번호" name="userPassword" maxlength="20">
					</div>
					<br>
					<button class="LoginButton" type="submit">로그인</button>
				</form>
			</div>
		</div>	
	</div>
</br>
<%@ include file="0_bottom.jsp" %>
</body>
</html>