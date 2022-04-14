<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="bbs.Bbs" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../css/style.css">
<title>게시판</title>
</head>
<body>
<%@ include file="0_top.jsp" %>
	<%
		int pageNumber = 1; //기본은 1 페이지를 할당
		// 만약 파라미터로 넘어온 오브젝트 타입 'pageNumber'가 존재한다면
		// 'int'타입으로 캐스팅을 해주고 그 값을 'pageNumber'변수에 저장한다
		if(request.getParameter("pageNumber") != null){
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}
	%>
  
<!-- 게시판 메인 페이지 영역 시작 -->
<br>
	<div>
		<div>
			<table style="margin-left: auto; margin-right: auto; text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th style="text-align: center;">번호</th>
						<th style="text-align: center;">제목</th>
						<th style="text-align: center;">작성자</th>
						<th style="text-align: center;">작성일</th>
					</tr>
				</thead>
				<tbody>
					<%
						BbsDAO bbsDAO = new BbsDAO(); // 인스턴스 생성
						ArrayList<Bbs> list = bbsDAO.getList(pageNumber);
						for(int i = 0; i < list.size(); i++){
					%>
					<tr>
						<td><%= list.get(i).getBbsID() %></td>
						<!-- 게시글 제목을 누르면 해당 글을 볼 수 있도록 링크를 걸어둔다 -->
						<td><a href="./board/view.jsp?bbsID=<%= list.get(i).getBbsID() %>">
							<%= list.get(i).getBbsTitle() %></a></td>
						<td><%= list.get(i).getUserID() %></td>
						<td><%= list.get(i).getBbsDate().substring(0, 11) + list.get(i).getBbsDate().substring(11, 13) + "시"
							+ list.get(i).getBbsDate().substring(14, 16) + "분" %></td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			</div>
			<br>
			<div>
			<!-- 페이징 처리 영역 -->
			<%
				if(pageNumber != 1){
			%>
			<button class="Crud" onclick="location.href='bbs.jsp?pageNumber=<%=pageNumber - 1 %>'">이전</button>
			<%
				}if(bbsDAO.nextPage(pageNumber + 1)){
			%>
			<button class="Crud" onclick="location.href='bbs.jsp?pageNumber=<%=pageNumber + 1 %>'">다음</button>
			<%
				}
			%>
			<!-- 글쓰기 버튼 생성 -->
			<button class="Crud" onclick="location.href='./board/write.jsp'">글작성</button>
		</div>
	</div>
  
  
	<br>
	<%@ include file="0_bottom.jsp" %>
</body>
</html>