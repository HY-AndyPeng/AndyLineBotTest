package com.fet.crm.nspMicro.util.bean;

import java.util.List;

public class WeatherElement {
	private String elementName;
	private List<Time> time;
	
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public List<Time> getTime() {
		return time;
	}
	public void setTime(List<Time> time) {
		this.time = time;
	} 
	
}
