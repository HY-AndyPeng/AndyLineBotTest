package com.fet.crm.nspMicro.util.bean;

public class WeatherBo {
	private String locationName;
	private String weather;
	private String probabilityOfPrecipitation;
	private String comfortIndex;	//舒適度
	private String maxTemperature;	//高 溫(度C)
	private String minTemperature;	//低 溫(度C)
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getProbabilityOfPrecipitation() {
		return probabilityOfPrecipitation;
	}
	public void setProbabilityOfPrecipitation(String probabilityOfPrecipitation) {
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
	}
	public String getComfortIndex() {
		return comfortIndex;
	}
	public void setComfortIndex(String comfortIndex) {
		this.comfortIndex = comfortIndex;
	}
	public String getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public String getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(String minTemperature) {
		this.minTemperature = minTemperature;
	}
	
	
}
