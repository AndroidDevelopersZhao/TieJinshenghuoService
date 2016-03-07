package com.shanghai;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import keyinfo.RespData;
import keyinfo.jdbc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class GaregeService extends HttpServlet{
	
	private Statement stat;
	private PrintWriter printWriter;
	private String user,date, province, city ,project, road_name, direction, start_time, end_time, v_count ,nv_count ,ps_count;
	private java.sql.ResultSet set;
	private java.util.List<String> list_users,dates, provinces, citys ,projects, road_names, 
	directions, start_times, end_times, v_counts ,nv_counts ,ps_counts;
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
		list_users=new ArrayList<String>();
		dates=new ArrayList<String>();
		provinces=new ArrayList<String>();
		citys=new ArrayList<String>();
		projects=new ArrayList<String>();
		road_names=new ArrayList<String>();
		directions=new ArrayList<String>();
		start_times=new ArrayList<String>();
		end_times=new ArrayList<String>();
		v_counts=new ArrayList<String>();
		nv_counts=new ArrayList<String>();
		ps_counts=new ArrayList<String>();
		
		printWriter=resp.getWriter();
		System.out.println("有客户端进入，时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//链接db
		stat=jdbc.jdbc("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/newclient?characterEncoding=utf-8", "root", "root");
		if (stat==null) {
			System.out.println("链接失败");
			printWriter.print(new Gson().toJson(new RespData(-1, "数据库异常")));
			return;
		}
		String str_req=req.getParameter("type");
		if (str_req==null) {
			printWriter.print(new Gson().toJson(new RespData(-1, "参数有误")));
			return;
		}else if (str_req.equals("")) {
			printWriter.print(new Gson().toJson(new RespData(-1, "参数不能为空")));
			return;
		}else {
			try {
				int type=Integer.valueOf(str_req);
				if (type==FinalData.TYPE_INSERTDATA) {
					user=req.getParameter("user");
					date=req.getParameter("date");
					province=req.getParameter("province");
					city=req.getParameter("city");
					project=req.getParameter("project");
					road_name=req.getParameter("road_name");
					direction=req.getParameter("direction");
					start_time=req.getParameter("start_time");
					end_time=req.getParameter("end_time");
					v_count=req.getParameter("v_count");
					nv_count=req.getParameter("nv_count");
					ps_count=req.getParameter("ps_count");
					InsertData insertData =new InsertData(stat);
					boolean b=insertData.insertData(user,date, province, city ,project, road_name, direction, start_time, end_time, v_count ,nv_count ,ps_count);
					if (!b) {
						printWriter.print(new Gson().toJson(new RespData(-1, "插入数据失败")));
						return;
					}else {
						printWriter.print(new Gson().toJson(new RespData(200, "插入成功")));
					}
				}else if (type==FinalData.TYPE_SELECT) {
					String sql  = "select * from garegedata";
					try {
						set = stat.executeQuery(sql);
						while (set.next()) {
							String user=set.getString("user");
							String date=set.getString("date");
							String province=set.getString("province");
							String city=set.getString("city");
							String project=set.getString("project");
							String road_name=set.getString("road_name");
							String direction=set.getString("direction");
							String start_time=set.getString("start_time");
							String end_time=set.getString("end_time");
							String v_count=set.getString("v_count");
							String nv_count=set.getString("nv_count");
							String ps_count=set.getString("ps_count");
							list_users.add(user);
							dates.add(date);
							provinces.add(province);
							citys.add(city);
							projects.add(project);
							road_names.add(road_name);
							directions.add(direction);
							start_times.add(start_time);
							end_times.add(end_time);
							v_counts.add(v_count);
							nv_counts.add(nv_count);
							ps_counts.add(ps_count);
							
						}
						JsonArray jsonArray = new JsonArray();
						printWriter.print(new Gson().toJson(new Resp_array(200,new arry(list_users, dates, provinces, citys, projects, road_names, directions, 
								start_times, end_times, v_counts, nv_counts, ps_counts))));
					} catch (Exception e) {
						// TODO: handle exception
						printWriter.print(new Gson().toJson(new RespData(-1, "查询失败")));
					}
					
					
				}else {
					printWriter.print(new Gson().toJson(new RespData(-1, "参数类型错误")));
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
				printWriter.print(new Gson().toJson(new RespData(-1, "参数格式不合法")));
			}
			
		}
		
		
		
		
		// user date province city project road_name direction start_time end_time v_count nv_count ps_count
		
	}
		
}
