package com.fet.crm.nspMicro.util.bean;

import java.util.List;

public class Location {
	private String locationName;
	private List<WeatherElement> weatherElement;
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public List<WeatherElement> getWeatherElement() {
		return weatherElement;
	}
	public void setWeatherElement(List<WeatherElement> weatherElement) {
		this.weatherElement = weatherElement;
	}
	
}
