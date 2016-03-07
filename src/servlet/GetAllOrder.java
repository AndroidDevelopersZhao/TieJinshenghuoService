package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import keyinfo.RespData;
import keyinfo.jdbc;

import com.google.gson.Gson;

import data.RespData_order;

public class GetAllOrder extends HttpServlet{
	private PrintWriter printWriter;
	private Statement stat;
	private ResultSet res;
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws javax.servlet.ServletException
	,java.io.IOException {doPost(req, resp);};
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		printWriter = resp.getWriter();
		if (req.getParameter("username") == null) {
			printWriter.print(new Gson().toJson(new RespData(-1, "传入的请求类型参数不合法")));
//			printWriter.close();
			return;
		}
		stat = jdbc
		.jdbc(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
				"root", "root");
		
	}
	
	
	
	/**
	 * 查询全部订单信息
	 * @param username
	 */
	private void selectAllorder(String username) {
		ArrayList<String> strs = new  ArrayList<String>();
		// TODO Auto-generated method stub
		//首先查询该用户对应的多有订单
		String sql_select_all_order="select orderid from user_tickets_order_info where username="+ username + "";
//		stat = jdbc
//		.jdbc(
//				"com.mysql.jdbc.Driver",
//				"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
//				"root", "root");
		try {
			res = stat.executeQuery(sql_select_all_order);
			System.out.println("查询所有订单成功，开始索引订单号");
			while (res.next()) {
				String orderi=res.getString("orderid");
//				sys(orderi);
				strs.add(orderi);
			}
			
			System.out.println("所有订单索引完成，返回订单集合大小："+strs.size());
			if (strs.size()==0) {
				printWriter.print(new Gson().toJson(new RespData_order(-1,"未查到相关订单", new ArrayList<String>())));
				  
			}else {
				printWriter.print(new Gson().toJson(new RespData_order(200,"成功的返回", strs)));
				System.out.println("返回的所有订单数据："+new Gson().toJson(new RespData_order(200,"成功的返回", strs)));
				  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			sys("查询所有订单失败");
			printWriter.print(new Gson().toJson(new RespData_order(-1, "订单查询失败",new ArrayList<String>())));
			  
			e.printStackTrace();
		}
	}

}
