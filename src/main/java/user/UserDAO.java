package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserDAO {
	
	private Connection conn; //자바와 데이터베이스를 연결
	private PreparedStatement pstmt; //쿼리문 대기 및 설정
	private ResultSet rs; //결과값 받아오기
	
	//기본 생성자
	//UserDAO가 실행되면 자동으로 생성되는 부분
	//메소드마다 반복되는 코드를 이곳에 넣으면 코드가 간소화된다
	public UserDAO() {
		ResourceBundle rb = null;
		rb = ResourceBundle.getBundle("db", Locale.KOREA);
		String db_password = rb.getString("db_password");
		System.out.println(db_password);
		//github에 DB비밀번호가 노출되지 않도록 보안 설정
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/newDB"; //만들어놓은 newDB (maridDB)
			String dbID = "root";
			String dbPassword = db_password;
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//로그인 영역
		public int login(String userID, String userPassword) {
			String sql = "select userPassword from user where userID = ?";
			//입력된 ID의 매칭되는 비밀번호를 선택한다
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					if(rs.getString(1).equals(userPassword)) {//DB로 구한 비밀번호 = 파라미터의 비밀번호
						return 1; //로그인 성공
					}else
						return 0; //비밀번호 틀림
				}
				return -1; //아이디 없음
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -2; //오류
		}
		
	//회원가입 영역
		public int join(User user) {
			  String sql = "insert into user values(?, ?)";
			  //ID, PW 데이터베이스 추가
			  try {
			    pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1, user.getUserID());
			    pstmt.setString(2, user.getUserPassword());
			    return pstmt.executeUpdate();
			  }catch (Exception e) {
			 	e.printStackTrace();
			  }
			  return -1;
			}	
		
}