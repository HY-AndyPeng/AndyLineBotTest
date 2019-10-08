package com.fet.crm.nspMicro.util.bean;

import java.util.List;

public class Records {
	private String datasetDescription;
	private List<WeatherElement> weatherElement;
	
	public String getDatasetDescription() {
		return datasetDescription;
	}
	public void setDatasetDescription(String datasetDescription) {
		this.datasetDescription = datasetDescription;
	}
	public List<WeatherElement> getWeatherElement() {
		return weatherElement;
	}
	public void setWeatherElement(List<WeatherElement> weatherElement) {
		this.weatherElement = weatherElement;
	}
	
}
