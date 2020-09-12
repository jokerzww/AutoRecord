package com.example.myapplication.AutoRecord.UploadInfoBean;


import com.example.myapplication.AutoRecord.InfoBean.MainTable;
import com.google.gson.annotations.SerializedName;

public class UploadInfoBean{

	@SerializedName("mainTable")
	private MainTable mainTable;

	@SerializedName("jnuid")
	private String jnuid;

	public void setMainTable(MainTable mainTable){
		this.mainTable = mainTable;
	}

	public MainTable getMainTable(){
		return mainTable;
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
			"UploadInfoBean{" + 
			"mainTable = '" + mainTable + '\'' + 
			",jnuid = '" + jnuid + '\'' + 
			"}";
		}
}