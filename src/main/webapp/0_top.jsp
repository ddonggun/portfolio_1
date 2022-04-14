<!doctype html>
<html>
  <head>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <!-- 한글깨짐방지 -->
    <link rel="stylesheet" href="./css/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  
<body>
	<%
		// 메인 페이지로 이동했을 때 세션에 값이 담겨있는지 체크
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
	%>
	
<div class="header">
    <table style="text-align: left">
			<%
				if(userID == null){
			%>
						<td width="350"><button class="LoginButton" onclick="location.href='./login/login.jsp'">로그인</button>
						<button class="LoginButton" onclick="location.href='./login/join.jsp'">회원가입</button></td>
			<%
				}else{
			%>			
						<td width="350"><button class="LoginButton" onclick="location.href='./login/logoutAction.jsp'">로그아웃</button></td>
			<%
				}
			%>
	</table>
	<h1 style="text-align: center">안녕하세요</h1>
</div>

<div class="topnav">
		<a href="./downloads.jsp">다운로드</a>
        <a href="./bbs.jsp">게시판</a>
        <a href="./link.jsp">참조사이트</a>
</div>		

<div style="margin-left: auto; margin-right: auto; text-align: center; border: 1px solid #dddddd">

</body>
</html>
