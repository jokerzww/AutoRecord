package com.example.myapplication.AutoRecord.LoginBean;

import com.google.gson.annotations.SerializedName;

public class JnuIdInfo{

	@SerializedName("idType")
	private String idType;

	@SerializedName("jnuid")
	private String jnuid;

	public void setIdType(String idType){
		this.idType = idType;
	}

	public String getIdType(){
		return idType;
	}

	public void setJnuid(String jnuid){
		this.jnuid = jnuid;
	}

	public String getJnuid(){
		return jnuid;
	}

	@Override
 	public String toString(){
		return 
			"JnuIdInfo{" + 
			"idType = '" + idType + '\'' + 
			",jnuid = '" + jnuid + '\'' + 
			"}";
		}
}