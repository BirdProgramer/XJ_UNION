import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	public static final String tname="userinfo";
	private static String db="xj_union";
	public static boolean insert(String sql) {
    	try {
    		Connection conn = getConn(db);
			Statement st=conn.createStatement();
			st.executeUpdate(sql);
			System.out.println("insert finish");
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return false;
		}
	}
	public static boolean update(String sql) {
		try{
			Connection conn = getConn(db);
			Statement st=conn.createStatement();
			if(st.executeUpdate(sql)==1) {
				return true;
			}else{
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	public static boolean query(String sql,String requests[],String answers[]) {
		try {
    		Connection conn = getConn(db);
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			if(rs.next()) {
				for(int i=0;i<requests.length;i++) {
					answers[i]=new String(rs.getString(requests[i]));
				}
				st.close();
				conn.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return false;
	}
	private static Connection getConn(String db)
	{
		//����Connection����
        Connection conn = null;
        //����������
        String driver = "com.mysql.jdbc.Driver";
        //URLָ��Ҫ���ʵ����ݿ���
        String url = "jdbc:mysql://127.0.0.1:3306/"+db;
        //MySQL����ʱ���û���
        String user = "root";
        //MySQL����ʱ������
        String password = "123456";
        try {
        	//������������
            Class.forName(driver);
            //1.getConnection()����������MySQL���ݿ⣡��
            conn = DriverManager.getConnection(url,user,password);
        } catch(ClassNotFoundException e) {   
            //���ݿ��������쳣����
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
        } catch(SQLException e) {
            //���ݿ�����ʧ���쳣����
            e.printStackTrace();  
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return conn;
	}
}
