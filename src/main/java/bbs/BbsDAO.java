package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale; //db properties
import java.util.ResourceBundle; //db properties

public class BbsDAO {

	private Connection conn;
	private ResultSet rs;
	
	//기본 생성자
	public BbsDAO() {
		ResourceBundle rb = null;
		rb = ResourceBundle.getBundle("db", Locale.KOREA);
		String db_password = rb.getString("db_password");
		System.out.println(db_password);
		//github에 DB비밀번호가 노출되지 않도록 보안 설정
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/bbs";
			String dbID = "root";
			String dbPassword = db_password;
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			//DB연결 준비 완료
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//작성일자 반환 메소드
	public String getDate() {
		String sql = "select now()"; //현재시간
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//preparedStatement의 장점 : 메서트와 ?를 통해 값을 전달해서 코드가 깔끔 - ex) pstmt.setInt(1,100) -> 첫번쨰 ?를 100으로
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
				//1열을 문자열로 치환 후 리턴
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";//데이터베이스 오류
	}
	
	//현재 게시글 다음 번호 반환 메소드
	public int getNext() {
		//현재 게시글을 내림차순으로 조회하여 가장 마지막 글번호 구하기
		String sql = "select bbsID from bbs order by bbsID desc"; //bbsID를 내림차순으로 정렬 후 bbsID 조회
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
				//현재 게시글 다음 번호 반환
			}
			return 1; //게시물이 없는 경우 1 반환
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	//글쓰기 메소드
	public int write(String bbsTitle, String userID, String bbsContent) {
		//POST로 제목 , 사용자ID , 콘텐츠 입력 받아 DB로 전달 
		String sql = "insert into bbs values(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); //bbsAvailable = 0 	>> 삭제된것처럼 표시
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	//게시글 리스트 메소드
	public ArrayList<Bbs> getList(int pageNumber){
		//페이지번호로 글 목록 1~10 생성 후 배열로 반환
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		//bbsID가 아래 계산결과보다 작고 , 삭제되지 않은 것을 내림차순으로 10개까지 정렬한다
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);//가장 마지막 글번호 - 2페이지부터 페이지마다 -10 
			rs = pstmt.executeQuery();
			while(rs.next()) {//번호숫자가 큰 순서로 차례대로 배열 저장
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//페이징 처리 메소드
	public boolean nextPage(int pageNumber) {
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1";
		//어떤 값보다 작은 bbsID가 있고 그것이 유효한 모든 항목을 반환
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);//가장 마지막 글번호 - 2페이지부터는 페이지당 -10
			rs = pstmt.executeQuery();
			if(rs.next()) {//현재 마지막 bbsID번호보다 작은 bbsID가 있으면 true
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//하나의 게시글을 보는 메소드
	public Bbs getBbs(int bbsID) {
		String sql = "select * from bbs where bbsID = ?";
		//bbsID = ? 인 모든 항목을 반환
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//게시글 수정 메소드
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String sql = "update bbs set bbsTitle = ?, bbsContent = ? where bbsID = ?";
		//id, 제목, 콘텐츠에 입력받은값을 입력
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	//게시글 삭제 메소드
	public int delete(int bbsID) {
		//실제 데이터를 삭제하는 것이 아니라 게시글 유효숫자를 '0'으로 수정한다
		String sql = "update bbs set bbsAvailable = 0 where bbsID = ?";
		//입력받은 bbsID의 유효값을 0으로 설정
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류 
	}
	
}