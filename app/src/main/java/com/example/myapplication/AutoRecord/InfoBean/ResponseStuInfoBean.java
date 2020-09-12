package com.example.myapplication.AutoRecord.InfoBean;

import com.google.gson.annotations.SerializedName;

public class ResponseStuInfoBean {

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
			"ResponseStuInfo{" + 
			"data = '" + data + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}