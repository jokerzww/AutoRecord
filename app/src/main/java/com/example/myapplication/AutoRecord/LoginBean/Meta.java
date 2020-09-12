package com.example.myapplication.AutoRecord.LoginBean;

import com.google.gson.annotations.SerializedName;

public class Meta{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("success")
	private boolean success;

	@SerializedName("response")
	private String response;

	@SerializedName("timestamp")
	private String timestamp;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			",success = '" + success + '\'' + 
			",response = '" + response + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}
}