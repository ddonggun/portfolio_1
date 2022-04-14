package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserDAO {
	
	private Connection conn; //�ڹٿ� �����ͺ��̽��� ����
	private PreparedStatement pstmt; //������ ��� �� ����
	private ResultSet rs; //����� �޾ƿ���
	
	//�⺻ ������
	//UserDAO�� ����Ǹ� �ڵ����� �����Ǵ� �κ�
	//�޼ҵ帶�� �ݺ��Ǵ� �ڵ带 �̰��� ������ �ڵ尡 ����ȭ�ȴ�
	public UserDAO() {
		ResourceBundle rb = null;
		rb = ResourceBundle.getBundle("db", Locale.KOREA);
		String db_password = rb.getString("db_password");
		System.out.println(db_password);
		//github�� DB��й�ȣ�� ������� �ʵ��� ���� ����
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/newDB"; //�������� newDB (maridDB)
			String dbID = "root";
			String dbPassword = db_password;
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//�α��� ����
		public int login(String userID, String userPassword) {
			String sql = "select userPassword from user where userID = ?";
			//�Էµ� ID�� ��Ī�Ǵ� ��й�ȣ�� �����Ѵ�
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					if(rs.getString(1).equals(userPassword)) {//DB�� ���� ��й�ȣ = �Ķ������ ��й�ȣ
						return 1; //�α��� ����
					}else
						return 0; //��й�ȣ Ʋ��
				}
				return -1; //���̵� ����
			}catch (Exception e) {
				e.printStackTrace();
			}
			return -2; //����
		}
		
	//ȸ������ ����
		public int join(User user) {
			  String sql = "insert into user values(?, ?)";
			  //ID, PW �����ͺ��̽� �߰�
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