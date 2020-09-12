package com.example.myapplication.AutoRecord.LoginBean;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("idtype")
	private String idtype;

	@SerializedName("jnuid")
	private String jnuid;

	@SerializedName("jnuId")
	private String jnuId;

	@SerializedName("name")
	private String name;

	@SerializedName("language")
	private String language;

	public void setIdtype(String idtype){
		this.idtype = idtype;
	}

	public String getIdtype(){
		return idtype;
	}

	public void setJnuid(String jnuid){
		this.jnuid = jnuid;
	}

	public String getJnuid(){
		return jnuid;
	}

	public void setJnuId(String jnuId){
		this.jnuId = jnuId;
	}

	public String getJnuId(){
		return jnuId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"idtype = '" + idtype + '\'' + 
			",jnuid = '" + jnuid + '\'' + 
			",jnuId = '" + jnuId + '\'' + 
			",name = '" + name + '\'' + 
			",language = '" + language + '\'' + 
			"}";
		}
}