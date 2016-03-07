package com.shanghai;

import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.ResultSet;

public class InsertData {
	private Statement stat;
	private boolean isSucc=false;
	public InsertData(Statement stat){
		this.stat=stat;
	}
	
	public boolean insertData(String...strings){
		
		String sql = "insert into garegedata(user,date, province, city ,project," +
				" road_name, direction, start_time, end_time, v_count ,nv_count ,ps_count) values ('"+strings[0]+"'," +
						"'"+strings[1]+"'," +
								"'"+strings[2]+"'," +
										"'"+strings[3]+"'," +
												"'"+strings[4]+"'," +
														"'"+strings[5]+"'," +
																"'"+strings[6]+"'," +
																		"'"+strings[7]+"'," +
																				"'"+strings[8]+"'," +
																						"'"+strings[9]+"'," +
																								"'"+strings[10]+"'," +
																										"'"+strings[11]+"')";
		try {
			int issu=stat.executeUpdate(sql);
			if (issu>-1) {
				isSucc=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
		
		return isSucc;
	}
}
