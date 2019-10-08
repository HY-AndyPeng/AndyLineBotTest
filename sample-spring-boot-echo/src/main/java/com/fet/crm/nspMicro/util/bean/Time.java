package com.fet.crm.nspMicro.util.bean;

import java.util.Map;

public class Time {
	private String startTime;
	private String endTime;
	private Map<String, String> parameter;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Map<String, String> getParameter() {
		return parameter;
	}
	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
	
}
