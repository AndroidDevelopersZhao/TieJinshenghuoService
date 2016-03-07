package keyinfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc {
	public static Statement jdbc(String forName,String url,String serviceUserName,
			String servicePassWord){
		Statement stat = null;
		try {
			Class.forName(forName);
			Connection conn=DriverManager.getConnection(url, serviceUserName, servicePassWord);
			stat=conn.createStatement();
			
		} catch (ClassNotFoundException e) {
			System.out.println("数据库连接失败");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("加载驱动失败");
			e.printStackTrace();
		}
		return stat;
	}
}
