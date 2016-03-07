package com.shanghai;

import com.google.gson.JsonArray;

public class Resp_array {
	private int code = 0x00;
	private arry arry;
	public Resp_array(int code, arry arry) {
		super();
		this.code = code;
		this.arry = arry;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public arry getArry() {
		return arry;
	}
	public void setArry(arry arry) {
		this.arry = arry;
	}
	
	
	
//	{"a":"b0",“data":{[],[]}}
}
