package keyinfo;

import java.util.ArrayList;

import data.TicketsPerson;

public class RespData_UserInfo {
	
	private int code =-1;
	private String result = null;
	private ArrayList<TicketsPerson> users = null;
	public RespData_UserInfo(int code,String result,ArrayList<TicketsPerson> users) {
	
	this.code=code;
	this.result=result;
	this.users=users;
}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<TicketsPerson> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<TicketsPerson> users) {
		this.users = users;
	}
	
	
	


	
}
