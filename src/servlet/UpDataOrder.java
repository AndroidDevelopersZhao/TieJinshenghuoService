package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import keyinfo.RespData;
import keyinfo.jdbc;

public class UpDataOrder  extends HttpServlet{
	private PrintWriter printWriter;
	private Statement stat;
	private ResultSet res;
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
		printWriter = resp.getWriter();
		stat = jdbc
		.jdbc(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
				"root", "root");
		
		if (isUserNameAndOrderIdNull(req)) {
			String username = req.getParameter("username");
			String orderId = req.getParameter("orderId");
			updataTiketsSQL(req,username, orderId);
		}
	}
	/**
	 * 判断用户名和订单号是否为空
	 * @param req
	 * @return
	 */
	public boolean isUserNameAndOrderIdNull(HttpServletRequest req){
		boolean b = false;
		if (req.getParameter("username") != null
				&& req.getParameter("orderId") != null) {
			b=true;
		}	
		return b;
	}
	
	private void updataTiketsSQL(HttpServletRequest req,String username, String orderId) {
		// TODO Auto-generated method stub
//		stat = jdbc
//				.jdbc(
//						"com.mysql.jdbc.Driver",
//						"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
//						"root", "root");
		this.rreq =req;
		String user_orderid=AA("user_orderid");
		String msg=AA("msg");
		String orderamoun=AA("orderamoun");
		String status=AA("status");
		String checi=AA("checi");
		String ordernumber=AA("ordernumber");
		String submit_time=AA("submit_time");
		String deal_time=AA("deal_time");
		String cancel_time=AA("cancel_time");
		String pay_time=AA("pay_time");
		String finished_time=AA("finished_time");
		
		//
		String refund_time=AA("refund_time");
		String juhe_refund_time=AA("juhe_refund_time");
		String train_date=AA("train_date");
		String from_station_name=AA("from_station_name");
		String from_station_code=AA("from_station_code");
		String to_station_name=AA("to_station_name");
		String to_station_code=AA("to_station_code");
		String refund_money=AA("refund_money");
		
		String passengerid=AA("passengerid");
		String passengersename=AA("passengersename");
		String piaotype=AA("piaotype");
		String piaotypename=AA("piaotypename");
		String passporttypeseid=AA("passporttypeseid");
		String passporttypeseidname=AA("passporttypeseidname");
		String passportseno=AA("passportseno");
		String price=AA("price");
		String zwcode=AA("zwcode");
		String zwname=AA("zwname");
		String ticket_no=AA("ticket_no");
		String cxin=AA("cxin");
		String reason=AA("reason");
		
		/////////////////
		
		String returnsuccess=AA("returnsuccess");
		String returnmoney=AA("returnmoney");
		String returntime=AA("returntime");
		String returnfailid=AA("returnfailid");
		String returnfailmsg=AA("returnfailmsg");
		String returntype=AA("returntype");
		
			//小方法
		


		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("用户名正确，开始查询订单是否存在");

				res = stat.executeQuery("select * from user_tickets_order_info where orderid="
						+ orderId + "");
				
				if (res.first()) {
									//订单存在，做更新。
					String SQL = "update user_tickets_order_info set " +
							"user_orderid = '" + user_orderid+ "' ,msg = '" + msg + "" +
							"' ,orderamoun = '" + orderamoun+ "' ,status = '" + status + "" +
							"' ,checi = '" + checi
							+ "' ,ordernumber = '" + ordernumber + "',submit_time='"+submit_time+"'" +
							",deal_time='"+deal_time+"',cancel_time='"+cancel_time+"',pay_time='"+pay_time+"',finished_time='"+finished_time+"'" +
							",refund_time='"+refund_time+"',juhe_refund_time='"+juhe_refund_time+"',train_date='"+train_date+"',from_station_name='"+from_station_name+"'" +
							",from_station_code='"+from_station_code+"',to_station_name='"+to_station_name+"',to_station_code='"+to_station_code+"',refund_money='"+refund_money+"'" +
							",passengerid='"+passengerid+"',passengersename='"+passengersename+"',piaotype='"+piaotype+"',piaotypename='"+piaotypename+"'" +
							",passporttypeseid='"+passporttypeseid+"',passporttypeseidname='"+passporttypeseidname+"',passportseno='"+passportseno+"',price='"+price+"'" +
							",zwcode='"+zwcode+"',zwname='"+zwname+"',ticket_no='"+ticket_no+"',cxin='"+cxin+"'" +
							",reason='"+reason+"',returnsuccess='"+returnsuccess+"',returnmoney='"+returnmoney+"',returntime='"+returntime+"'" +
							",returnfailid='"+returnfailid+"',returnfailmsg='"+returnfailmsg+"',returntype='"+returntype+"' where orderid="+ orderId + "";
			int a = stat.executeUpdate(SQL);
			if (a > -1) {
				printWriter.print(new Gson().toJson(new RespData(200,
						"数据更新成功")));
				System.out.println("数据更新成功");
				  
			} else {
				printWriter.print(new Gson().toJson(new RespData(-1,
						"数据更新失败")));
				  
			}
				} else {
					//订单不存在，做insert。
					printWriter.print(new Gson().toJson(new RespData(-1,
					"订单号不存在")));
					  
				}
			} else {
				printWriter.println(new Gson().toJson(new RespData(-1, "该账户不存在")));
				  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	private HttpServletRequest rreq=null;
	public String AA(String ssss){
		String str =null;
		try {
			str=rreq.getParameter(ssss);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}
}
