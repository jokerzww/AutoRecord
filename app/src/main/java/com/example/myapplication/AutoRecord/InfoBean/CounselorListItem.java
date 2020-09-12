package com.example.myapplication.AutoRecord.InfoBean;

import com.google.gson.annotations.SerializedName;

public class CounselorListItem{

	@SerializedName("rsbh")
	private String rsbh;

	@SerializedName("dw")
	private Object dw;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private Object id;

	@SerializedName("type")
	private Object type;

	public void setRsbh(String rsbh){
		this.rsbh = rsbh;
	}

	public String getRsbh(){
		return rsbh;
	}

	public void setDw(Object dw){
		this.dw = dw;
	}

	public Object getDw(){
		return dw;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(Object id){
		this.id = id;
	}

	public Object getId(){
		return id;
	}

	public void setType(Object type){
		this.type = type;
	}

	public Object getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"CounselorListItem{" + 
			"rsbh = '" + rsbh + '\'' + 
			",dw = '" + dw + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}