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
	
	//�⺻ ������
	public BbsDAO() {
		ResourceBundle rb = null;
		rb = ResourceBundle.getBundle("db", Locale.KOREA);
		String db_password = rb.getString("db_password");
		System.out.println(db_password);
		//github�� DB��й�ȣ�� ������� �ʵ��� ���� ����
		
		try {
			String dbURL = "jdbc:mariadb://localhost:3306/bbs";
			String dbID = "root";
			String dbPassword = db_password;
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			//DB���� �غ� �Ϸ�
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	//�ۼ����� ��ȯ �޼ҵ�
	public String getDate() {
		String sql = "select now()"; //����ð�
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//preparedStatement�� ���� : �޼�Ʈ�� ?�� ���� ���� �����ؼ� �ڵ尡 ��� - ex) pstmt.setInt(1,100) -> ù���� ?�� 100����
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
				//1���� ���ڿ��� ġȯ �� ����
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";//�����ͺ��̽� ����
	}
	
	//���� �Խñ� ���� ��ȣ ��ȯ �޼ҵ�
	public int getNext() {
		//���� �Խñ��� ������������ ��ȸ�Ͽ� ���� ������ �۹�ȣ ���ϱ�
		String sql = "select bbsID from bbs order by bbsID desc"; //bbsID�� ������������ ���� �� bbsID ��ȸ
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
				//���� �Խñ� ���� ��ȣ ��ȯ
			}
			return 1; //�Խù��� ���� ��� 1 ��ȯ
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽� ����
	}
	
	//�۾��� �޼ҵ�
	public int write(String bbsTitle, String userID, String bbsContent) {
		//POST�� ���� , �����ID , ������ �Է� �޾� DB�� ���� 
		String sql = "insert into bbs values(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); //bbsAvailable = 0 	>> �����Ȱ�ó�� ǥ��
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽� ����
	}
	
	//�Խñ� ����Ʈ �޼ҵ�
	public ArrayList<Bbs> getList(int pageNumber){
		//��������ȣ�� �� ��� 1~10 ���� �� �迭�� ��ȯ
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		//bbsID�� �Ʒ� ��������� �۰� , �������� ���� ���� ������������ 10������ �����Ѵ�
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);//���� ������ �۹�ȣ - 2���������� ���������� -10 
			rs = pstmt.executeQuery();
			while(rs.next()) {//��ȣ���ڰ� ū ������ ���ʴ�� �迭 ����
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
	
	//����¡ ó�� �޼ҵ�
	public boolean nextPage(int pageNumber) {
		String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1";
		//� ������ ���� bbsID�� �ְ� �װ��� ��ȿ�� ��� �׸��� ��ȯ
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);//���� ������ �۹�ȣ - 2���������ʹ� �������� -10
			rs = pstmt.executeQuery();
			if(rs.next()) {//���� ������ bbsID��ȣ���� ���� bbsID�� ������ true
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//�ϳ��� �Խñ��� ���� �޼ҵ�
	public Bbs getBbs(int bbsID) {
		String sql = "select * from bbs where bbsID = ?";
		//bbsID = ? �� ��� �׸��� ��ȯ
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
	
	//�Խñ� ���� �޼ҵ�
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String sql = "update bbs set bbsTitle = ?, bbsContent = ? where bbsID = ?";
		//id, ����, �������� �Է¹������� �Է�
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽� ����
	}
	
	//�Խñ� ���� �޼ҵ�
	public int delete(int bbsID) {
		//���� �����͸� �����ϴ� ���� �ƴ϶� �Խñ� ��ȿ���ڸ� '0'���� �����Ѵ�
		String sql = "update bbs set bbsAvailable = 0 where bbsID = ?";
		//�Է¹��� bbsID�� ��ȿ���� 0���� ����
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //�����ͺ��̽� ���� 
	}
	
}