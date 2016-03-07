package keyinfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Util;

import com.google.gson.Gson;

import data.AddOrderInfo;
import data.AddPhoneInfoRespData;
import data.RespData_UpdataAmount;
import data.RespData_order;
import data.TicketsPerson;
import data.VersionRespData;

public class SaveKeyInfo extends HttpServlet {
	private PrintWriter printWriter;
	private String privateKey_service;
	private Statement stat;

	// private ResultSet res;

	@Override
	synchronized protected void doGet(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);

	}

	@Override
	synchronized protected void doPost(final HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		printWriter = resp.getWriter();
		stat = jdbc
				.jdbc(
						"com.mysql.jdbc.Driver",
						"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
						"root", "root");
		if (req.getParameter("type") == null) {
			printWriter.print(new Gson()
					.toJson(new RespData(-1, "传入的请求类型参数不合法")));
			printWriter.flush();
			return;
		}
		int type = Integer.valueOf(req.getParameter("type"));
		String t = null;
		switch (type) {

		case 1:
			sys("生成秘钥对");
			RandomKey();
			// printWriter.flush();
			break;
		case 2:
			sys("注册账号");
			if (isUserNameAndPswNull(req)) {
				String username = req.getParameter("username");
				String password = req.getParameter("password");
				register(username, password);
			} else {
				printWriter.print(new Gson()
						.toJson(new RespData(-1, "用户名不能为空")));

			}
			// printWriter.flush();
			break;
		case 3:
			sys("登陆账号");
			if (isUserNameAndPswNull(req)) {
				login(req.getParameter("username"), req
						.getParameter("password"));
			} else {
				printWriter.print(new Gson().toJson(new RespData(-1,
						"用户名或密码不能为空")));
			}
			// printWriter.flush();
			break;
		case 4:
			sys("找回密码");
			if (isUserNameAndNewPswNull(req)) {
				String username = req.getParameter("username");
				String newpassword = req.getParameter("newpassword");
				forgotPsw(username, newpassword);
			} else {
				printWriter.print(new Gson().toJson(new RespData(-1,

				"用户名或密码不能为空")));
			}
			// printWriter.flush();
			break;
		case 5:
			sys("查询个人余额信息");
			if (isUserNameNull(req)) {
				String username = req.getParameter("username");
				selectUserAmount(username);
			}
			// printWriter.flush();
			break;
		case 6:
			sys("插入个人信息");
			if (isUserNameNull(req)) {
				AddMyInfo(req);
			}
			// printWriter.flush();
			break;
		case 7:
			sys("查询个人信息");
			if (isUserNameNull(req)) {
				String username = req.getParameter("username");
				selectUserInfo(username);
			}
			// printWriter.flush();
			break;
		case 8:
			sys("插入订单信息");
			if (isUserNameAndOrderIdNull(req)) {
				String username = req.getParameter("username");
				String orderId = req.getParameter("orderId");
				insertDataToTicketsSQL(username, orderId);
			}
			// printWriter.flush();
			break;
		case 9:
			sys("更新车票订单信息表");
			if (isUserNameAndOrderIdNull(req)) {
				String username = req.getParameter("username");
				String orderId = req.getParameter("orderId");
				updataTiketsSQL(req, username, orderId);
			}

			break;
		case 10:
			sys("获取全部订单");
			if (isUserNameNull(req)) {
				String username = req.getParameter("username");
				if (isExist(username)) {
					selectAllorder(username);
				} else {
					printWriter.print(new Gson().toJson(new RespData_order(-1,
							"用户不存在", null)));
				}
				// /**
				// * 1.交换秘钥 -----成功例子-----{"code"=200,"data"="key"}
				// * 2.注册账号 -----成功例子-----{"code"=200,"data"="注册成功"}
				// * 3.登陆账号 -----成功例子-----{"code"=200,"data"="验证通过"}
				// * 4.找回密码 -----成功例子-----{"code"=200,"data"="新密码设置成功"}
				// * 5.查询余额 -----成功例子-----{"code"=200,"data"="100.25"}
				// * 6.插入购票人信息 -----成功例子-----{"code"=200,"data"="数据插入成功"}
				// * 7.查询购票人信息
				// * 8.插入订单信息
				// * 9.更新车票订单信息表
				// * 10.获取全部订单
				// * 11.获取未支付订单
				// * 12.获取待出票订单
				// * 13.获取出票成功的订单
				// */

			} else {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"用户名不能为空", null)));
			}

			break;
		case 11:
			sys("获取未支付订单");

			if (isUserNameNull(req)) {
				String username = req.getParameter("username");

				if (isExist(username)) {
					selectNotPayOrder(username);
				} else {
					printWriter.print(new Gson().toJson(new RespData_order(-1,
							"用户不存在", null)));
				}
			} else {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"用户名不能为空", null)));
			}

			break;
		case 12:
			sys("获取待出票订单");
			if (isUserNameNull(req)) {
				String username = req.getParameter("username");

				if (isExist(username)) {
					selectPayedOrder(username);
				} else {
					printWriter.print(new Gson().toJson(new RespData_order(-1,
							"用户不存在", null)));
				}
			}
			// printWriter.flush();
			break;
		case 13:
			sys("获取出票成功的订单");
			if (isUserNameNull(req)) {
				String username = req.getParameter("username");

				if (isExist(username)) {
					selectSuccOrder(username);
				} else {
					printWriter.print(new Gson().toJson(new RespData_order(-1,
							"用户不存在", null)));
				}
			}
			// printWriter.flush();
			break;
			//更新用胡余额
		case 14:
			String amount = req.getParameter("amount");
			if (isUserNameNull(req) &&amount!=null &&!amount.equals("")) {
				String username = req.getParameter("username");
				
				if (isExist(username)) {
					//更新用户余额
					updataUserAmount(username,amount);
				} else {
					printWriter.print(new Gson().toJson(new RespData_order(-1,
							"用户不存在", null)));
				}
			}
			break;
		case 15:
			//版本检测
			getVersion();
			break;
		case 16:
			//添加手机订单充值信息
			if (req.getParameter("username")!=null) {
				String username = req.getParameter("username");
				String orderid = req.getParameter("orderid");
				String price = req.getParameter("price");
				String info = req.getParameter("info");
				String mobphone = req.getParameter("mobphone");
				String time = req.getParameter("time");
				addorderinfo(username,orderid,price,info,mobphone,time);
			}else {
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "用户名不能为空", null)));
			}
			break;
		case 17:
			//查询手机订单信息
			if (req.getParameter("username")!=null) {
				String username = req.getParameter("username");
				selectPhoneOrderInfo(username);
			}else {
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "用户名不能为空", null)));
			}
			break;
		case 18 :
			//更新手机充值订单信息
			if (req.getParameter("username")!=null) {
				String username = req.getParameter("username");
				String info = req.getParameter("info");
				String sta = req.getParameter("sta");
				String sporder_id = req.getParameter("sporder_id");
				updatePhoneOrderInfo(username,info,sta,sporder_id);
			}else {
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "用户名不能为空", null)));
			}
			
			break;
		default:
			printWriter.print(new Gson().toJson(new RespData(-1, "参数类型未知")));
			// printWriter.flush();
			break;

		}

		// printWriter.close();//------
	}

	 private void updatePhoneOrderInfo(String username, String info, String sta,String sporder_id) {
		// TODO Auto-generated method stub
		String SQL = "update addphoneamount set state="+sta+",stateinfo='"+info+"' where orderid = '"+sporder_id+"'";
		System.out.println("语句："+SQL);

		try {
			int a = stat.executeUpdate(SQL);
			if (a > -1) {
				System.out.println("订单更新成功");
				selectPhoneOrderUseOrderid(username,sporder_id);
			} else {
				System.out.println("订单更新失败");
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "订单更新失败", null)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "数据库异常", null)));
		}
	}
		/**
		 * 根据订单号查询该订单的最心状态
		 * @param orderid
		 */
	 synchronized private void selectPhoneOrderUseOrderid(String username,String orderid) {
		 
		String sql = "select * from addphoneamount where orderid = '"+orderid+"' and username="+username+"";
		System.out.println("执行语句："+sql);
		stat = jdbc
		.jdbc(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
				"root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql);
			if(res.first()){
				String time = res.getString("time");
				String order_id = res.getString("orderid");
				String price = res.getString("price");
				String info =res.getString("info");
				String mobphone = res.getString("mobphone");
				String state =res.getString("state");
				String stateinfo=res.getString("stateinfo");
				AddOrderInfo addinfo=new AddOrderInfo(time, order_id, price, info, mobphone, state, stateinfo);
				printWriter.print(new Gson().toJson(addinfo));
				System.out.println("成功返回如下数据："+new Gson().toJson(addinfo));
//				res.close();
			}else {
				System.out.println("数据不存在");
			}
			
			
//			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("异常一次i");
			printWriter.print(new Gson().toJson(new AddOrderInfo(null, null, null, null, null, null, null)));
		}finally{
			try {
				
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据用户名索引全部订单
	 * @param username
	 */
	private void selectPhoneOrderInfo(String username) {
		// TODO Auto-generated method stub
		String sql = "select * from addphoneamount where username = "+username+"";
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql);
			List<AddOrderInfo> addOrderInfos = new ArrayList<AddOrderInfo>();
			while(res.next()){
				String time = res.getString("time");
				String orderid = res.getString("orderid");
				String price = res.getString("price");
				String info =res.getString("info");
				String mobphone = res.getString("mobphone");
				String state =res.getString("state");
				String stateinfo=res.getString("stateinfo");
				AddOrderInfo addinfo = new AddOrderInfo(time, orderid, price, info, mobphone, state, stateinfo);
				addOrderInfos.add(addinfo);
			}
			if (addOrderInfos.size()==0) {
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "该用户暂没有订单", null)));
			}else {
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(200, "查询成功", addOrderInfos)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addorderinfo(String username, String orderid, String price,
			String info, String mobphone, String time) {
			String insertSQL ="insert into addphoneamount(username,orderid,price,info,mobphone,time)" +
					"values('"+username+"','"+orderid+"','"+price+"','"+info+"','"+mobphone+"','"+time+"')";
			int a;
			try {
				a = stat.executeUpdate(insertSQL);
				if (a > -1) {
					printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(200, "插入数据成功", null)));
					System.out.println("手机订单数据插入成功");

				} else {
					printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "插入数据失败", null)));

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				printWriter.print(new Gson().toJson(new AddPhoneInfoRespData(-1, "插入数据失败", null)));
			}
			
	}

	private void getVersion() {
		// TODO Auto-generated method stub
		String sql = "select * from version where ID = 1";
		ResultSet res = null;
			try {
				res = stat.executeQuery(sql);
				if (res.first()) {
					
					String version = res.getString("version");
					String result  = res.getString("result");
					printWriter.print(new Gson().toJson(new VersionRespData(200, version, result)));
				}else {
					printWriter.print(new Gson().toJson(new VersionRespData(-1, "null", "版本查询失败")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				printWriter.print(new Gson().toJson(new VersionRespData(-1, "null", "版本查询失败")));
			}
	}

	private void updataUserAmount(String username, String amount) {
		// TODO Auto-generated method stub
		
		
		Double d = Double.valueOf(amount);
		System.out.println("d="+d);
		String SQL = "update userdb set amount="+d+" where username = '"+username+"'";
		try {
			int a = stat.executeUpdate(SQL);
			if (a > -1) {
				printWriter.print(new Gson().toJson(new RespData_UpdataAmount(200,
						"余额更新成功")));
				System.out.println("余额更新成功");

			} else {
				printWriter.print(new Gson().toJson(new RespData_UpdataAmount(-1,
						"余额更新失败")));
				System.out.println("余额更新失败");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printWriter.print(new Gson().toJson(new RespData_UpdataAmount(-1, "数据库异常")));
		}
	}

	/**
	 * 获取出票成功的订单
	 * 
	 * @param username
	 */
	private void selectSuccOrder(String username) {
		// TODO Auto-generated method stub
		String sql_select_notPay_order = "select orderid from user_tickets_order_info where username="
				+ username + " AND status= " + Util.CHUPIAOCHENGGONG + "";
		ArrayList<String> lists = new ArrayList<String>();
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql_select_notPay_order);
			System.out.println("出票成功订单查询成功，开始索引订单号");
			while (res.next()) {
				String orderi = res.getString("orderid");
				sys(orderi);
				lists.add(orderi);
			}

			System.out.println("索引完成，返回订单集合大小：" + lists.size());
			if (lists.size() == 0) {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"该用户出票成功的订单为空", new ArrayList<String>())));
				// printWriter.close();
			} else {
				printWriter.print(new Gson().toJson(new RespData_order(200,
						"成功的返回", lists)));
				// printWriter.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			sys("出票成功订单获取失败");
			printWriter.print(new Gson().toJson(new RespData_order(-1,
					"订单查询失败", new ArrayList<String>())));
			// printWriter.close();
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 获取待出票订单
	 * 
	 * @param username
	 */
	private void selectPayedOrder(String username) {
		// TODO Auto-generated method stub
		String sql_select_notPay_order = "select orderid from user_tickets_order_info where username="
				+ username + " AND status= " + Util.DAICHUPIAO + "";
		ArrayList<String> lists = new ArrayList<String>();
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql_select_notPay_order);
			System.out.println("待出票订单查询成功，开始索引订单号");
			while (res.next()) {
				String orderi = res.getString("orderid");
				sys(orderi);
				lists.add(orderi);
			}

			System.out.println("索引完成，返回订单集合大小：" + lists.size());
			if (lists.size() == 0) {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"该用户的待支付订单为空", new ArrayList<String>())));
				// printWriter.close();
			} else {
				printWriter.print(new Gson().toJson(new RespData_order(200,
						"成功的返回", lists)));
				// printWriter.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			sys("待出票订单获取失败");
			printWriter.print(new Gson().toJson(new RespData_order(-1,
					"订单查询失败", new ArrayList<String>())));
			// printWriter.close();
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 未支付订单
	 * 
	 * @param username
	 */
	private void selectNotPayOrder(String username) {
		// TODO Auto-generated method stub
		String sql_select_notPay_order = "select orderid from user_tickets_order_info "
				+ "where username="
				+ username
				+ " AND status= "
				+ Util.WEIZHIFU + "";
		ArrayList<String> lists = new ArrayList<String>();
		stat = jdbc
				.jdbc(
						"com.mysql.jdbc.Driver",
						"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
						"root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql_select_notPay_order);
			System.out.println("未支付订单查询成功，开始索引订单号");
			while (res.next()) {
				String orderi = res.getString("orderid");
				sys(orderi);
				lists.add(orderi);
			}

			System.out.println("索引完成，返回订单集合大小：" + lists.size());
			if (lists.size() == 0) {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"该用户的未支付订单为空", new ArrayList<String>())));
				// printWriter.close();
			} else {
				printWriter.print(new Gson().toJson(new RespData_order(200,
						"成功的返回未支付订单", lists)));
				printWriter.flush();
				System.out.println("成功的返回未支付订单："
						+ new Gson().toJson(new RespData_order(200, "成功的返回",
								lists)));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			sys("未支付订单获取失败");
			printWriter.print(new Gson().toJson(new RespData_order(-1,
					"订单查询失败", new ArrayList<String>())));

			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		// printWriter.flush();
		// printWriter.close();
	}

	/**
	 * 查询全部订单信息
	 * 
	 * @param username
	 */
	synchronized private void selectAllorder(String username) {
		ArrayList<String> strs = new ArrayList<String>();
		// TODO Auto-generated method stub
		// 首先查询该用户对应的多有订单
		String sql_select_all_order = "select orderid from user_tickets_order_info where username="
				+ username + "";
		stat = jdbc
				.jdbc(
						"com.mysql.jdbc.Driver",
						"jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
						"root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery(sql_select_all_order);
			System.out.println("查询所有订单成功，开始索引订单号");
			while (res.next()) {
				String orderi = res.getString("orderid");
				// sys(orderi);
				strs.add(orderi);
			}

			System.out.println("所有订单索引完成，返回订单集合大小：" + strs.size());
			if (strs.size() == 0) {
				printWriter.print(new Gson().toJson(new RespData_order(-1,
						"未查到相关订单", new ArrayList<String>())));

			} else {
				printWriter.print(new Gson().toJson(new RespData_order(200,
						"成功的返回全部订单", strs)));
				System.out.println("----------------" + printWriter == null);
				// printWriter.flush();
				System.out.println("成功的返回全部订单："
						+ new Gson().toJson(new RespData_order(200, "成功的返回",
								strs)));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// sys("查询所有订单失败");
			printWriter.print(new Gson().toJson(new RespData_order(-1,
					"订单查询失败", new ArrayList<String>())));

			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		// printWriter.flush();
		// printWriter.close();
	}
	/**
	 * charu购票疼信息
	 * @param req
	 */
	private void AddMyInfo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		String username = null;
		String name = null;
		String year = null;
		String city = null;
		String sex = null;
		String cardNo = null;
		String phoneNum = null;
		try {
			username = req.getParameter("username");
			name = req.getParameter("name");
			year = req.getParameter("year");
			city = req.getParameter("city");
			sex = req.getParameter("sex");
			cardNo = req.getParameter("cardNo");
			phoneNum = req.getParameter("phoneNum");
			// username=req.getParameter("username");

		} catch (Exception e) {
			// TODO: handle exception
		}

		System.err.println("username:" + username);

		if (username == null || username.equals("")) {
			printWriter.print(new Gson().toJson(new RespData(-1, "用户名不能为空")));

			return;
		}
		if (name == null || name.equals("") || year == null || year.equals("")
				|| city == null || city.equals("") || sex == null
				|| sex.equals("") || cardNo == null || cardNo.equals("")
				|| phoneNum == null || phoneNum.equals("")) {
			printWriter.print(new Gson().toJson(new RespData(-1, "至少有一项为空值")));

			return;
		}

		addInfo(username, name, year, city, sex, cardNo, phoneNum);
	}



	private void addInfo(String username, String name, String year,
				String city, String sex, String cardNo, String phoneNum) {
			// stat = jdbc
			// .jdbc(
			// "com.mysql.jdbc.Driver",
			// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
			// "root", "root");
			ResultSet res = null;
			try {
				res = stat.executeQuery("select * from userdb where username="
						+ username + "");
				if (res.first()) {
					System.out.println("用户名正确，开始添加数据");

					// int amount = res.getInt("amount");
					// System.out.println("户名对应的余额为："+amount);
				String SQL ="insert into ticketspersoninfo(username,lname,lyear,lcity,lsex,lcardno,lphonenum)values('"+username+"','"+name+"','"+year+"','"+city+"','"+sex+"','"+cardNo+"','"+phoneNum+"')";
//					String SQL = "insert into  set  = '" + name
//							+ "'  = '" + year + "'  = '" + city
//							+ "'  = '" + sex + "'  = '" + cardNo
//							+ "'  = '" +  + "',username="
//							+  + "";
					int a = stat.executeUpdate(SQL);
					if (a > -1) {
						printWriter.print(new Gson().toJson(new RespData(200,
								"数据插入成功")));
						System.out.println("个人数据插入成功");

					} else {
						printWriter.print(new Gson().toJson(new RespData(-1,
								"数据插入失败")));

					}

				} else {
					printWriter.print(new Gson().toJson(new RespData(-1,
							"该账号未注册，请注册后再试")));

				}
			} catch (SQLException e) {
				printWriter.print(new Gson().toJson(new RespData(-1, "账号只能为电话号码")));
				// TODO Auto-generated catch block

				e.printStackTrace();
			} finally {
				if (res != null)
					try {
						res.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}








//	private void AddMyInfo(HttpServletRequest req) {
//		// TODO Auto-generated method stub
//		String username = null;
//		String name = null;
//		String year = null;
//		String city = null;
//		String sex = null;
//		String cardNo = null;
//		String phoneNum = null;
//		try {
//			username = req.getParameter("username");
//			name = req.getParameter("name");
//			year = req.getParameter("year");
//			city = req.getParameter("city");
//			sex = req.getParameter("sex");
//			cardNo = req.getParameter("cardNo");
//			phoneNum = req.getParameter("phoneNum");
//			// username=req.getParameter("username");
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//		System.err.println("username:" + username);
//
//		if (username == null || username.equals("")) {
//			printWriter.print(new Gson().toJson(new RespData(-1, "用户名不能为空")));
//
//			return;
//		}
//		if (name == null || name.equals("") || year == null || year.equals("")
//				|| city == null || city.equals("") || sex == null
//				|| sex.equals("") || cardNo == null || cardNo.equals("")
//				|| phoneNum == null || phoneNum.equals("")) {
//			printWriter.print(new Gson().toJson(new RespData(-1, "至少有一项为空值")));
//
//			return;
//		}
//
//		addInfo(username, name, year, city, sex, cardNo, phoneNum);
//	}

	private void updataTiketsSQL(HttpServletRequest req, String username,
			String orderId) {
		// TODO Auto-generated method stub
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		this.rreq = req;
		String user_orderid = AA("user_orderid");
		String msg = AA("msg");
		String orderamoun = AA("orderamoun");
		String status = AA("status");
		String checi = AA("checi");
		String ordernumber = AA("ordernumber");
		String submit_time = AA("submit_time");
		String deal_time = AA("deal_time");
		String cancel_time = AA("cancel_time");
		String pay_time = AA("pay_time");
		String finished_time = AA("finished_time");

		//
		String refund_time = AA("refund_time");
		String juhe_refund_time = AA("juhe_refund_time");
		String train_date = AA("train_date");
		String from_station_name = AA("from_station_name");
		String from_station_code = AA("from_station_code");
		String to_station_name = AA("to_station_name");
		String to_station_code = AA("to_station_code");
		String refund_money = AA("refund_money");

		String passengerid = AA("passengerid");
		String passengersename = AA("passengersename");
		String piaotype = AA("piaotype");
		String piaotypename = AA("piaotypename");
		String passporttypeseid = AA("passporttypeseid");
		String passporttypeseidname = AA("passporttypeseidname");
		String passportseno = AA("passportseno");
		String price = AA("price");
		String zwcode = AA("zwcode");
		String zwname = AA("zwname");
		String ticket_no = AA("ticket_no");
		String cxin = AA("cxin");
		String reason = AA("reason");

		// ///////////////

		String returnsuccess = AA("returnsuccess");
		String returnmoney = AA("returnmoney");
		String returntime = AA("returntime");
		String returnfailid = AA("returnfailid");
		String returnfailmsg = AA("returnfailmsg");
		String returntype = AA("returntype");

		// 小方法

		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("用户名正确，开始查询订单是否存在");

				res = stat
						.executeQuery("select * from user_tickets_order_info where orderid="
								+ orderId + "");

				if (res.first()) {
					// 订单存在，做更新。
					String SQL = "update user_tickets_order_info set "
							+ "user_orderid = '"
							+ user_orderid
							+ "' ,msg = '"
							+ msg
							+ ""
							+ "' ,orderamoun = '"
							+ orderamoun
							+ "' ,status = '"
							+ status
							+ ""
							+ "' ,checi = '"
							+ checi
							+ "' ,ordernumber = '"
							+ ordernumber
							+ "',submit_time='"
							+ submit_time
							+ "'"
							+ ",deal_time='"
							+ deal_time
							+ "',cancel_time='"
							+ cancel_time
							+ "',pay_time='"
							+ pay_time
							+ "',finished_time='"
							+ finished_time
							+ "'"
							+ ",refund_time='"
							+ refund_time
							+ "',juhe_refund_time='"
							+ juhe_refund_time
							+ "',train_date='"
							+ train_date
							+ "',from_station_name='"
							+ from_station_name
							+ "'"
							+ ",from_station_code='"
							+ from_station_code
							+ "',to_station_name='"
							+ to_station_name
							+ "',to_station_code='"
							+ to_station_code
							+ "',refund_money='"
							+ refund_money
							+ "'"
							+ ",passengerid='"
							+ passengerid
							+ "',passengersename='"
							+ passengersename
							+ "',piaotype='"
							+ piaotype
							+ "',piaotypename='"
							+ piaotypename
							+ "'"
							+ ",passporttypeseid='"
							+ passporttypeseid
							+ "',passporttypeseidname='"
							+ passporttypeseidname
							+ "',passportseno='"
							+ passportseno
							+ "',price='"
							+ price
							+ "'"
							+ ",zwcode='"
							+ zwcode
							+ "',zwname='"
							+ zwname
							+ "',ticket_no='"
							+ ticket_no
							+ "',cxin='"
							+ cxin
							+ "'"
							+ ",reason='"
							+ reason
							+ "',returnsuccess='"
							+ returnsuccess
							+ "',returnmoney='"
							+ returnmoney
							+ "',returntime='"
							+ returntime
							+ "'"
							+ ",returnfailid='"
							+ returnfailid
							+ "',returnfailmsg='"
							+ returnfailmsg
							+ "',returntype='"
							+ returntype
							+ "' where orderid=" + orderId + "";
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
					// 订单不存在，做insert。
					printWriter.print(new Gson().toJson(new RespData(-1,
							"订单号不存在")));

				}
			} else {
				printWriter
						.print(new Gson().toJson(new RespData(-1, "该账户不存在")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private HttpServletRequest rreq = null;

	public String AA(String ssss) {
		String str = null;
		try {
			str = rreq.getParameter(ssss);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}

	/**
	 * 插入新数据到订票表
	 * 
	 * @param username
	 * @param orderId
	 */
	private void insertDataToTicketsSQL(String username, String orderId) {
		// TODO Auto-generated method stub
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("用户名正确，开始插入购票订单号到新数据库");
				String str = "insert into user_tickets_order_info(username,orderid)values('"
						+ username + "','" + orderId + "')";

				int a = stat.executeUpdate(str);
				if (a > -1) {
					// 如果语句执行成功时会返回一个int型的大于-1的值！
					printWriter.print(new Gson().toJson(new RespData(200,
							"订单号存入成功")));

				} else {
					printWriter.print(new Gson().toJson(new RespData(-1,
							"订单号存入失败")));
					System.out.println("订单号存入失败，数据库异常");

				}
			} else {
				printWriter
						.print(new Gson().toJson(new RespData(-1, "该账户不存在")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private ArrayList<TicketsPerson> persons = new ArrayList<TicketsPerson>();

	private void selectUserInfo(String username) {
		// TODO Auto-generated method stub
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		ResultSet res = null;
		System.out.print("开始执行-----查询用户名");
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("用户名正确，开始查询该用户的个人信息");
				res = stat.executeQuery("select * from ticketspersoninfo where username="
						+ username + "");
				while (res.next()) {
					
					String name = null;
					String year = null;
					String city = null;
					String sex = null;
					String cardNo = null;
					String phoneNum = null;

					name = res.getString("lname");
					year = res.getString("lyear");
					city = res.getString("lcity");
					sex = res.getString("lsex");
					cardNo = res.getString("lcardno");
					phoneNum = res.getString("lphonenum");
					sys("查詢的name=" + name + "");
					if (!name.equals("") || !year.equals("")
							|| !city.equals("") || !sex.equals("")
							|| !cardNo.equals("") || !phoneNum.equals("")) {
						persons.add(new TicketsPerson(name, year, city, sex,
								cardNo, phoneNum));

					}
				}

				if (persons.size() != 0) {
					printWriter.print(new Gson().toJson(new RespData_UserInfo(
							200, "成功的返回", persons)));
					persons=new ArrayList<TicketsPerson>();
				} else {
					printWriter.print(new Gson().toJson(new RespData_UserInfo(
							-1, "无数据", new ArrayList<TicketsPerson>())));
				}
				
			} else {
				printWriter.print(new Gson().toJson(new RespData_UserInfo(-1,
						"用户名不存在",null)));

			}
		} catch (SQLException e) {
			printWriter.print(new Gson().toJson(new RespData_UserInfo(-1,
					"数据库异常", new ArrayList<TicketsPerson>())));

			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

//	private void addInfo(String username, String name, String year,
//			String city, String sex, String cardNo, String phoneNum) {
//		// stat = jdbc
//		// .jdbc(
//		// "com.mysql.jdbc.Driver",
//		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
//		// "root", "root");
//		ResultSet res = null;
//		try {
//			res = stat.executeQuery("select * from userdb where username="
//					+ username + "");
//			if (res.first()) {
//				System.out.println("用户名正确，开始添加数据");
//
//				// int amount = res.getInt("amount");
//				// System.out.println("户名对应的余额为："+amount);
//				String SQL = "update userdb set lname = '" + name
//						+ "' ,lyear = '" + year + "' ,lcity = '" + city
//						+ "' ,lsex = '" + sex + "' ,lcardno = '" + cardNo
//						+ "' ,lphonenum = '" + phoneNum + "' where username="
//						+ username + "";
//				int a = stat.executeUpdate(SQL);
//				if (a > -1) {
//					printWriter.print(new Gson().toJson(new RespData(200,
//							"数据插入成功")));
//					System.out.println("个人数据插入成功");
//
//				} else {
//					printWriter.print(new Gson().toJson(new RespData(-1,
//							"数据插入失败")));
//
//				}
//
//			} else {
//				printWriter.print(new Gson().toJson(new RespData(-1,
//						"该账号未注册，请注册后再试")));
//
//			}
//		} catch (SQLException e) {
//			printWriter.print(new Gson().toJson(new RespData(-1, "账号只能为电话号码")));
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//		} finally {
//			if (res != null)
//				try {
//					res.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
//
//	}

	private void selectUserAmount(String username) {
		// TODO Auto-generated method stub
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("用户名正确，开始查询该用户的余额");

				Double amount = res.getDouble("amount");
				System.out.println("户名对应的余额为：" + amount);
				printWriter.print(new Gson().toJson(new RespData(200, amount
						+ "".trim())));

			} else {
				printWriter.print(new Gson().toJson(new RespData(-1,
						"该账号未注册，请注册后再试")));

			}
		} catch (SQLException e) {
			printWriter.print(new Gson().toJson(new RespData(-1, "账号只能为电话号码")));
			// TODO Auto-generated catch block

			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void forgotPsw(String username, String newpassword) {
		// TODO Auto-generated method stub
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				System.out.println("数据存在，开始设置新密码");
				String newpassword1 = new String(Utils.decryptByPrivateKey(
						Base64Utils.decode(newpassword), privateKey_service))
						.trim();
				String SQL = "update userdb set password = '"
						+ XXMD5Util.getMD5String(newpassword1)
						+ "' where username='" + username + "'";
				int a = stat.executeUpdate(SQL);
				if (a > -1) {
					printWriter.print(new Gson().toJson(new RespData(200,
							"新密码设置成功")));
					System.out.println("新密码设置成功");

				} else {
					printWriter.print(new Gson().toJson(new RespData(-1,
							"密码修改失败，请检查密码格式是否正确")));

				}
			} else {
				printWriter.print(new Gson().toJson(new RespData(-1,
						"该账号未注册，请注册后再试")));

			}
		} catch (SQLException e) {
			printWriter.print(new Gson().toJson(new RespData(-1, "账号只能为电话号码")));

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			printWriter.print(new Gson()
					.toJson(new RespData(-1, "解密异常，请退出软件重试")));

			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void login(String username, String password) {
		// stat = jdbc
		// .jdbc(
		// "com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
		// "root", "root");
		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");

			if (res.first()) {
				System.out.println("接收到的 passworfd：" + password);
				try {
					String password1 = new String(Utils.decryptByPrivateKey(
							Base64Utils.decode(password), privateKey_service))
							.trim();
					if (XXMD5Util.checkPassword(password1, res
							.getString("password"))) {
						printWriter.print(new Gson().toJson(new RespData(200,
								"验证通过")));

					} else {
						printWriter.print(new Gson().toJson(new RespData(-1,
								"账号密码不匹配")));

					}
				} catch (Exception e) {
					// TODO: handle exception
					printWriter.print(new Gson().toJson(new RespData(-1,

					"账号密码不匹配")));

				}

			} else {
				printWriter.print(new Gson().toJson(new RespData(-1, "账号未注册")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库语句执行错误");
			printWriter.print(new Gson().toJson(new RespData(-1, "数据库异常")));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void register(String num, String psw) {
		// TODO Auto-generated method stub
		// System.out.println("收到账号：" + num);
		ResultSet res = null;
		try {
			String p = new String(Utils.decryptByPrivateKey(Base64Utils
					.decode(psw), privateKey_service)).trim();
			System.out.println("收到密码：" + p);
			String psw_md5 = XXMD5Util.getMD5String(new String(Utils
					.decryptByPrivateKey(Base64Utils.decode(psw),
							privateKey_service)).trim());
			System.out.println("摘要后：" + psw_md5);
			System.out.println("使用MD5校验的结果："
					+ XXMD5Util.checkPassword(p, psw_md5));
			// stat = jdbc
			// .jdbc(
			// "com.mysql.jdbc.Driver",
			// "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8",
			// "root", "root");
			// 用注册页面传来的用户名去数据库查找，如果已经存在则返回error

			res = stat.executeQuery("select * from userdb where username="
					+ num + "");

			if (res.first()) {
				// jo = new JSONObject();
				// System.out.println("数据存在");
				// jo.put("code", -1);
				// jo.put("msg", "账号已经存在");
				// writer.print(jo.toString());
				printWriter.print(new Gson().toJson(new RespData(-1, "数据已存在")));
				System.out.println("数据存在");

			} else {
				// jo = new JSONObject();
				System.out.println("数据不存在,开始注册");
				String str1 = "insert into userdb(username,password)values('"
						+ num + "','" + psw_md5 + "')";

				int a = stat.executeUpdate(str1);
				if (a > -1) {
					// 如果语句执行成功时会返回一个int型的大于-1的值！
					// jo.put("code", 88);
					// jo.put("msg", "注册成功");
					// writer.print(jo.toString());
					System.out.println("注册成功");
					printWriter.print(new Gson().toJson(new RespData(200,
							"注册成功")));

				} else {
					// jo.put("code", -1);
					// jo.put("msg", "数据库异常");
					// writer.print(jo.toString());
					printWriter.print(new Gson().toJson(new RespData(-1,
							"注册失败，数据库异常")));
					System.out.println("注册失败，数据库异常");

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("服务器解密失败");

		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		// printWriter.flush();
		// printWriter.print(new Gson().toJson(new RespData(200, "即将开始注册")));
	}

	private void RandomKey() {
		Map<String, Object> keyMap;
		try {
			keyMap = Utils.genKeyPair();
			// 生成Base64解码码后的字符串秘钥
			String publicKey_service = Utils.getPublicKey(keyMap);
			privateKey_service = Utils.getPrivateKey(keyMap);
			System.out.println("服务器密钥对生成成功，开始存入文件");
			// 存储公钥
			File file_pu = new File("C:/MyRSAKey/publicKey_service");
			if (file_pu.exists()) {
				file_pu.delete();
			}
			FileOutputStream out_pu = new FileOutputStream(file_pu);
			out_pu.write(publicKey_service.getBytes());
			out_pu.flush();
			out_pu.close();

			System.out.println("服务端公钥存储成功，开始存入私钥");
			// 生成Base64解码码后的字符串秘钥
			File file_pr = new File("C:/MyRSAKey/privateKey_service");
			if (file_pr.exists()) {
				file_pr.delete();
			}
			FileOutputStream out_pr = new FileOutputStream(file_pr);
			out_pr.write(privateKey_service.getBytes());
			out_pr.flush();
			out_pr.close();
			System.out.println("服务端私钥存储成功");
			byte[] pp = Utils.encrypt(publicKey_service.getBytes(), Utils.MODE);
			System.out.println("开始返回服务器公钥给客户端:");
			printWriter.print(new Gson().toJson(new RespData(200,
					new String(pp))));
			System.out.println("返回成功,返回给客户端客户端de数据："

			+ new Gson().toJson(new RespData(200, new String(pp))));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务器密钥对生成失败");
		}
		// printWriter.flush();

	}

	/**
	 * 查询该用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	public boolean isExist(String username) {
		boolean isExist = false;
		ResultSet res = null;
		try {
			res = stat.executeQuery("select * from userdb where username="
					+ username + "");
			if (res.first()) {
				isExist = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isExist = false;
		} finally {
			if (res != null)
				try {
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return isExist;
	}

	/**
	 * 判断用户名是否为空
	 * 
	 * @param req
	 * @return
	 */
	public boolean isUserNameNull(HttpServletRequest req) {
		boolean b = false;
		if (req.getParameter("username") != null) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断用户名和密码是否为空
	 * 
	 * @param req
	 * @return
	 */
	public boolean isUserNameAndPswNull(HttpServletRequest req) {
		boolean b = false;
		if (req.getParameter("username") != null
				&& req.getParameter("password") != null) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断用户名和新密码是否为空
	 * 
	 * @param req
	 * @return
	 */
	public boolean isUserNameAndNewPswNull(HttpServletRequest req) {
		boolean b = false;
		if (req.getParameter("username") != null
				&& req.getParameter("newpassword") != null) {
			b = true;
		}
		return b;
	}

	/**
	 * 判断用户名和订单号是否为空
	 * 
	 * @param req
	 * @return
	 */
	public boolean isUserNameAndOrderIdNull(HttpServletRequest req) {
		boolean b = false;
		if (req.getParameter("username") != null
				&& req.getParameter("orderId") != null) {
			b = true;
		}
		return b;
	}

	public void sys(String msg) {
		System.out.println("NewClientService:"
				+ msg
				+ ",请求时间："
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
	}
}
