<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>글작성</title>
</head>
<body>
	<%@ include file="0_top.jsp" %>
	</br>
	<!-- 게시판 글쓰기 양식 영역 시작 -->
	<div>
		<div>
			<form method="post" action="writeAction.jsp">
				<table style="margin-left: auto; margin-right: auto; text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th colspan="2" text-align: center;">게시판 글쓰기 양식</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input type="text" placeholder="글 제목" name="bbsTitle" maxlength="50"></td>
						</tr>
						<tr>
							<td><textarea placeholder="글 내용" name="bbsContent" maxlength="2048" style="height: 350px;"></textarea></td>
						</tr>
					</tbody>
				</table>
				<!-- 글쓰기 버튼 생성 -->
				<button type="submit" class="Crud">글쓰기</button>
			</form>
		</div>
	</div>
	<!-- 게시판 글쓰기 양식 영역 끝 -->
</br>
<%@ include file="0_bottom.jsp" %>
</body>
</html>