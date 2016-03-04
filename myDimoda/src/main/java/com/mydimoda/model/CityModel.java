package com.mydimoda.model;

import java.util.Comparator;

public class CityModel implements Comparable
{
	private String name,zone;

	public CityModel(String name,String zone)
	{
		this.name=name;
		this.zone=zone;
	}
	
	public CityModel(String name)
	{
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public String toString() {
	    return "Name = " + getName() + ", Zone = " + getZone();
	  }

	  public int compareTo(Object o) {
	    if (!(o instanceof CityModel))
	      throw new ClassCastException();

	    CityModel e = (CityModel) o;

	    return name.compareTo(e.getName());
	  }

	  static class SalaryComparator implements Comparator {
	    public int compare(Object o1, Object o2) {
	      if (!(o1 instanceof CityModel) || !(o2 instanceof CityModel))
	        throw new ClassCastException();

	      CityModel e1 = (CityModel) o1;
	      CityModel e2 = (CityModel) o2;

	      return e1.getName().compareTo(e2.getName());
	    }
	  }
}
