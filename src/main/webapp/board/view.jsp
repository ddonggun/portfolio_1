<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="bbs.BbsDAO" %>
<!DOCTYPE html>
<html>
<head>
<title>목록</title>
</head>
<body>
	<%@ include file="0_top.jsp" %>
	</br>
		<%
		// bbsID를 초기화 시키고
		// 'bbsID'라는 데이터가 넘어온 것이 존재한다면 캐스팅을 하여 변수에 담는다
		int bbsID = 0;
		if(request.getParameter("bbsID") != null){
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		
		// 만약 넘어온 데이터가 없다면
		if(bbsID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다')");
			script.println("location.href='../bbs.jsp'");
			script.println("</script");
		}
		
		// 유효한 글이라면 구체적인 정보를 'bbs'라는 인스턴스에 담는다
		Bbs bbs = new BbsDAO().getBbs(bbsID);
	%>
	
	<!-- 게시판 글 보기 양식 영역 시작 -->
	<div>
		<div>
			<table style="margin-left: auto; margin-right: auto; text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="2" style="text-align: center;">목록</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%;">글 제목</td>
						<td colspan="2"><%= bbs.getBbsTitle().replaceAll(" ", "&nbsp;")
								.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>") %></td>
					</tr>
					<tr>
						<td>작성자</td>
						<td colspan="2"><%= bbs.getUserID() %></td>
					</tr>
					<tr>
						<td>작성일자</td>
						<td colspan="2"><%= bbs.getBbsDate().substring(0, 11) + bbs.getBbsDate().substring(11, 13) + "시"
								+ bbs.getBbsDate().substring(14, 16) + "분" %></td>
					</tr>
					<tr>
						<td>내용</td>
						<td colspan="2" style="height: 200px; text-align: left;"><%= bbs.getBbsContent().replaceAll(" ", "&nbsp;")
							.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>") %></td>
					</tr>
				</tbody>
			</table>
			<button class="Crud" onclick="location.href='../bbs.jsp'">목록</button>
			<!-- 해당 글의 작성자가 본인이라면 수정과 삭제가 가능하도록 코드 추가 -->
			<%
				if(userID != null && userID.equals(bbs.getUserID())){
			%>
					<button class="Crud" onclick="location.href='update.jsp?bbsID=<%= bbsID %>'">수정</button>
					<button class="Crud" onclick="location.href='deleteAction.jsp?bbsID=<%= bbsID %>'">삭제</button>
			<%
				}
			%>
		</div>
	</div>
	<!-- 게시판 글 보기 양식 영역 끝 -->
	</br>
<%@ include file="0_bottom.jsp" %>
</body>
</html>