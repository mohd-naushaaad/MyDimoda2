package com.mydimoda.object;

import com.mydimoda.database.DbAdapter;

public class DMItemObjectDatabase {

	public String type;
	public String index;
	
	DbAdapter m_DbAdapter;
	public DMItemObjectDatabase()
	{
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	
	
}
