package com.example.myapplication.AutoRecord.InfoBean;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("xbm")
	private String xbm;

	@SerializedName("declare_time")
	private String declareTime;

	@SerializedName("jnuId")
	private String jnuId;

	@SerializedName("nation")
	private String nation;

	@SerializedName("city")
	private String city;

	@SerializedName("counselorList")
	private List<CounselorListItem> counselorList;

	@SerializedName("location_x")
	private String locationX;

	@SerializedName("location_y")
	private String locationY;

	@SerializedName("yxsmc")
	private String yxsmc;

	@SerializedName("mainTable")
	private MainTable mainTable;

	@SerializedName("province")
	private String province;

	@SerializedName("xm")
	private String xm;

	@SerializedName("zy")
	private String zy;

	public void setXbm(String xbm){
		this.xbm = xbm;
	}

	public String getXbm(){
		return xbm;
	}

	public void setDeclareTime(String declareTime){
		this.declareTime = declareTime;
	}

	public String getDeclareTime(){
		return declareTime;
	}

	public void setJnuId(String jnuId){
		this.jnuId = jnuId;
	}

	public String getJnuId(){
		return jnuId;
	}

	public void setNation(String nation){
		this.nation = nation;
	}

	public String getNation(){
		return nation;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setCounselorList(List<CounselorListItem> counselorList){
		this.counselorList = counselorList;
	}

	public List<CounselorListItem> getCounselorList(){
		return counselorList;
	}

	public void setLocationX(String locationX){
		this.locationX = locationX;
	}

	public String getLocationX(){
		return locationX;
	}

	public void setLocationY(String locationY){
		this.locationY = locationY;
	}

	public String getLocationY(){
		return locationY;
	}

	public void setYxsmc(String yxsmc){
		this.yxsmc = yxsmc;
	}

	public String getYxsmc(){
		return yxsmc;
	}

	public void setMainTable(MainTable mainTable){
		this.mainTable = mainTable;
	}

	public MainTable getMainTable(){
		return mainTable;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getProvince(){
		return province;
	}

	public void setXm(String xm){
		this.xm = xm;
	}

	public String getXm(){
		return xm;
	}

	public void setZy(String zy){
		this.zy = zy;
	}

	public String getZy(){
		return zy;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"xbm = '" + xbm + '\'' + 
			",declare_time = '" + declareTime + '\'' + 
			",jnuId = '" + jnuId + '\'' + 
			",nation = '" + nation + '\'' + 
			",city = '" + city + '\'' + 
			",counselorList = '" + counselorList + '\'' + 
			",location_x = '" + locationX + '\'' + 
			",location_y = '" + locationY + '\'' + 
			",yxsmc = '" + yxsmc + '\'' + 
			",mainTable = '" + mainTable + '\'' + 
			",province = '" + province + '\'' + 
			",xm = '" + xm + '\'' + 
			",zy = '" + zy + '\'' + 
			"}";
		}
}