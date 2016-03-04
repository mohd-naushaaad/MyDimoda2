package com.mydimoda.model;

import java.io.Serializable;

public class CityDatabaseModel implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String name;
	String timezone;
	String tl_hour;
	String t2_hour;
	String t3_hour;
	String createddate;
	int flag;
	int backgroundcolor;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getCreateddate() {
		return createddate;
	}
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getTl_hour() {
		return tl_hour;
	}
	public void setTl_hour(String tl_hour) {
		this.tl_hour = tl_hour;
	}
	public String getT2_hour() {
		return t2_hour;
	}
	public void setT2_hour(String t2_hour) {
		this.t2_hour = t2_hour;
	}
	public String getT3_hour() {
		return t3_hour;
	}
	public void setT3_hour(String t3_hour) {
		this.t3_hour = t3_hour;
	}
	public int getBackgroundcolor() {
		return backgroundcolor;
	}
	public int setBackgroundcolor(int color) {
		return this.backgroundcolor = color;
	}
	
	

}
