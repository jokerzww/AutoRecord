package com.example.myapplication.AutoRecord.LoginBean;

import com.google.gson.annotations.SerializedName;

public class LoginResponseBean {

	@SerializedName("data")
	private Data data;

	@SerializedName("meta")
	private Meta meta;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMeta(Meta meta){
		this.meta = meta;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"LoginResponse{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}