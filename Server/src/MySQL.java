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
			// TODO 自动生成的 catch 块
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	private static Connection getConn(String db)
	{
		//声明Connection对象
        Connection conn = null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名
        String url = "jdbc:mysql://127.0.0.1:3306/"+db;
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "123456";
        try {
        	//加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            conn = DriverManager.getConnection(url,user,password);
        } catch(ClassNotFoundException e) {   
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");   
            e.printStackTrace();   
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();  
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return conn;
	}
}
