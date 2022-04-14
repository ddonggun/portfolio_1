<%@page import="java.io.PrintWriter"%>
<%@page import="user.UserDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("utf-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID" />
<jsp:setProperty name="user" property="userPassword" />
<!DOCTYPE html>
<html>

<head>

</head>

<body>
	<%
		// 현재 세션 상태를 체크
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		
		// 이미 로그인했으면 회원가입을 할 수 없게 한다
		if(userID != null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인이 되어 있습니다')");
			script.println("location.href='../index.html'");
			script.println("</script>");
		}
	
		//빈 값이 있으면 입력 하라고 표시해주고 아니라면
		if(user.getUserID() == null || user.getUserPassword() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다')");
			script.println("history.back()");
			script.println("</script>");
		}else{
			//userDAO 생성 후 user객체에 저장된 userID와 userPassword를 UserDAO를 통해 검사
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			if(result == -1){ //이미존재하는 경우
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다')");
				script.println("history.back()");
				script.println("</script>");
			}else { //그렇지 않은 경우
				session.setAttribute("userID", user.getUserID());
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('회원가입 성공')");
				script.println("location.href='../index.html'");
				script.println("</script>");
			}
		}
	%>
</body>
</html>