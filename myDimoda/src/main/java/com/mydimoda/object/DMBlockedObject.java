package com.mydimoda.object;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.mydimoda.model.DatabaseModel;

public class DMBlockedObject {

	public List<DMItemObject> blockedList;
	public List<DMItemObject> getBlockedList() {
		return blockedList;
	}
	public void setBlockedList(List<DMItemObject> blockedList) {
		this.blockedList = blockedList;
	}
	public List<DMItemObjectDatabase> blockedList_db;
	public DatabaseModel blockedListt;
	JSONObject data;
	public DMBlockedObject()
	{
		blockedList = new ArrayList<DMItemObject>();
		blockedList_db = new ArrayList<DMItemObjectDatabase>();
		blockedListt = new DatabaseModel();
	}
	
	

	
	
	
	
}
