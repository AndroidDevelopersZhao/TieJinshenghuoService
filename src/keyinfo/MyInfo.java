package keyinfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MyInfo extends HttpServlet{
	private PrintWriter printWriter;
	private Statement stat;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	} 
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		 printWriter=resp.getWriter();
		if (req.getParameter("my")!=null) {
			MyData data = new Gson().fromJson(req.getParameter("my"), MyData.class);
			stat=jdbc.jdbc("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8", "root", "root");
			String str1="insert into myinfodb(username,city,networkstat,imei,iesi,mtype,mtyb,number)values('"+data.getUsername()+
			"','"+data.getCity()+"','"+data.getNetWorkState()+"','"+data.getIMEI()+"'," +
					"'"+data.getIESI()+"','"+data.getMtype()+"','"+data.getMtyb()+"'," +
							"'"+data.getNumer()+"')";
			try {
				stat.execute(str1);
				System.out.println("数据插入成功");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("插入语句执行错误");
			}
			System.out.println("my:"+req.getParameter("my"));
				printWriter.print(new Gson().toJson(new RespData(200, "OK")));
				
		}
		
		 
	}
	
}
