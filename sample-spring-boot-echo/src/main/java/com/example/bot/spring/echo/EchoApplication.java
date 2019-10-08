/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fet.crm.nspMicro.util.bean.HttpResult;
import com.fet.crm.nspMicro.util.bean.Location;
import com.fet.crm.nspMicro.util.bean.MessageWithQuickReplySupplier;
import com.fet.crm.nspMicro.util.bean.Records;
import com.fet.crm.nspMicro.util.bean.QueryThirtySixWeather;
import com.fet.crm.nspMicro.util.bean.Time;
import com.fet.crm.nspMicro.util.bean.WeatherBo;
import com.fet.crm.nspMicro.util.bean.WeatherElement;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        Message result = null;
        final String originalMessageText = event.getMessage().getText();
        final String token = event.getReplyToken();
        
        switch(originalMessageText) {
        	case "我上班瞜!":
        		result = new TextMessage("上班簽到已完成，請上班加油哦~~");
        		break;
        	case "我下班瞜!":
        		result = new TextMessage("下班簽退已完成，辛苦了哦~~");
        		break;
        	case "@查詢天氣":
        		result = new MessageWithQuickReplySupplier().get();
        		break;
        	case "@ID":
        		result = new TextMessage(event.getSource().getUserId());
        		break;
        	case "token":
        		result = new TextMessage(token);
        		break;
        	case "@Confirm":	
        		ConfirmTemplate confirmTemplate = new ConfirmTemplate(
                        "Do it?",
                        new MessageAction("Yes", "Yes!"),
                        new DatetimePickerAction("123", "test", "date")
                );
        		result = new TemplateMessage("Confirm alt text", confirmTemplate);
        		break;
        	default: 
        		StringBuffer sb = new StringBuffer();
        		
				try {
					if (hasKeyWord(originalMessageText)) {
						WeatherBo weatherBo = getWeather("臺北市");
						
						sb.append("臺北市目前").append("\n");
						sb.append("天氣:").append(weatherBo.getWeather()).append("\n");
						sb.append("降雨機率:").append(weatherBo.getProbabilityOfPrecipitation()).append("%").append("\n");
						sb.append("氣溫:").append(weatherBo.getMinTemperature()).append("~").append(weatherBo.getMaxTemperature()).append("\n");
						sb.append("舒適度:").append(weatherBo.getComfortIndex());
					
						result = new TextMessage(sb.toString()); 
					} 
				} catch (Exception e) {
					sb.append(e.getMessage());
				}
			result = new TextMessage(sb.toString());
        }
        
        return result;
    }
    
    @SuppressWarnings("unused")
	private boolean hasKeyWord(String keyWord) {
    	boolean result = false;
    	
    	if("天氣".indexOf(keyWord) != -1) {
    		
    		if("宜蘭縣".indexOf(keyWord) != -1 &&
				"花蓮縣".indexOf(keyWord) != -1 &&
				"臺東縣".indexOf(keyWord) != -1 &&
				"澎湖縣".indexOf(keyWord) != -1 &&
				"金門縣".indexOf(keyWord) != -1 &&
				"連江縣".indexOf(keyWord) != -1 &&
				"臺北市".indexOf(keyWord) != -1 &&
				"新北市".indexOf(keyWord) != -1 &&
				"桃園市".indexOf(keyWord) != -1 &&
				"臺中市".indexOf(keyWord) != -1 &&
				"臺南市".indexOf(keyWord) != -1 &&
				"高雄市".indexOf(keyWord) != -1 &&
				"基隆市".indexOf(keyWord) != -1 &&
				"新竹縣".indexOf(keyWord) != -1 &&
				"新竹市".indexOf(keyWord) != -1 &&
				"苗栗縣".indexOf(keyWord) != -1 &&
				"彰化縣".indexOf(keyWord) != -1 &&
				"南投縣".indexOf(keyWord) != -1 &&
				"雲林縣".indexOf(keyWord) != -1 &&
				"嘉義縣".indexOf(keyWord) != -1 &&
				"嘉義市".indexOf(keyWord) != -1 &&
				"屏東縣".indexOf(keyWord) != -1 ) {
    			result = true;
    		}
    	}
  
    	return result;
    }
    
    private WeatherBo getWeather(String locationName) throws Exception {
    	WeatherBo result = new WeatherBo();
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("Authorization", "CWB-F17B9FA6-AE76-4DC5-BF8E-3D3E2EC19F63");
		params.put("sort", "startTime");
		params.put("locationName", locationName);
		HttpResult httpResult = HttpUtil.get(
				"https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001", params);
		String respon = httpResult.getResult();
		QueryThirtySixWeather queryThirtySixWeather = JsonUtil.stringToObject(respon, QueryThirtySixWeather.class);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (queryThirtySixWeather.getRecords() != null) {
			Records records = queryThirtySixWeather.getRecords();
			List<Location> locations = records.getLocation();
			Calendar nowCalendar = Calendar.getInstance();
			for (Location location : locations) {
				result.setLocationName(location.getLocationName());
				for(WeatherElement weatherElement: location.getWeatherElement()) {
					List<Time> times = null;
					Calendar startCalendar = null;
					Calendar endCalendar = null;
					switch (weatherElement.getElementName()) {
					//天氣
					case "Wx":
						times = weatherElement.getTime();
						startCalendar = Calendar.getInstance();
						endCalendar = Calendar.getInstance();
						for (Time time : times) {
							Date startDate = sdf.parse(time.getStartTime());
							Date endDate = sdf.parse(time.getEndTime());
							startCalendar.setTime(startDate);
							endCalendar.setTime(endDate);
							if (nowCalendar.before(endCalendar)) {
								result.setWeather(time.getParameter().get("parameterName"));
								break;
							}
						}

						break;
					//降雨機率
					case "PoP":
						times = weatherElement.getTime();
						startCalendar = Calendar.getInstance();
						endCalendar = Calendar.getInstance();
						for (Time time : times) {
							Date startDate = sdf.parse(time.getStartTime());
							Date endDate = sdf.parse(time.getEndTime());
							startCalendar.setTime(startDate);
							endCalendar.setTime(endDate);
							if (nowCalendar.before(endCalendar)) {
								result.setProbabilityOfPrecipitation(time.getParameter().get("parameterName"));
								break;
							}
						}
						
						break;
					//最低溫度
					case "MinT":
						
						times = weatherElement.getTime();
						startCalendar = Calendar.getInstance();
						endCalendar = Calendar.getInstance();
						for (Time time : times) {
							Date startDate = sdf.parse(time.getStartTime());
							Date endDate = sdf.parse(time.getEndTime());
							startCalendar.setTime(startDate);
							endCalendar.setTime(endDate);
							if (nowCalendar.before(endCalendar)) {
								result.setMinTemperature(time.getParameter().get("parameterName"));
								break;
							}
						}
						
						break;
					//最高溫度
					case "MaxT":
						
						times = weatherElement.getTime();
						startCalendar = Calendar.getInstance();
						endCalendar = Calendar.getInstance();
						for (Time time : times) {
							Date startDate = sdf.parse(time.getStartTime());
							Date endDate = sdf.parse(time.getEndTime());
							startCalendar.setTime(startDate);
							endCalendar.setTime(endDate);
							if (nowCalendar.before(endCalendar)) {
								result.setMaxTemperature(time.getParameter().get("parameterName"));
								break;
							}
						}
						
						break;
					//感覺
					case "CI":
						times = weatherElement.getTime();
						startCalendar = Calendar.getInstance();
						endCalendar = Calendar.getInstance();
						for (Time time : times) {
							Date startDate = sdf.parse(time.getStartTime());
							Date endDate = sdf.parse(time.getEndTime());
							startCalendar.setTime(startDate);
							endCalendar.setTime(endDate);
							if (nowCalendar.before(endCalendar)) {
								result.setComfortIndex(time.getParameter().get("parameterName"));
								break;
							}
						}
						break;
					}
				}
			
			}

		}
		
		return result;
    }
    
    @EventMapping
    public Message handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        return new TextMessage("Got postback data " + event.getPostbackContent().getData() + ", param " + event
                .getPostbackContent().getParams().toString());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
