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

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fet.crm.nspMicro.util.bean.HttpResult;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
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
        Map<String, String> params = new HashMap<String, String>();
        
        switch(originalMessageText) {
        	case "87":
        		result = new TextMessage("你全家都87");
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
        	case "@test":	
				try {
					params.put("Authorization", "CWB-F17B9FA6-AE76-4DC5-BF8E-3D3E2EC19F63");
					params.put("sort", "startTime");
					params.put("locationName", "臺北市");
					HttpResult httpResult = HttpUtil.get(
							"https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001", params);
					result = new TextMessage(httpResult.getResult());
				} catch (Exception e) {
					result = new TextMessage("發根問題" + e.getMessage());
				}
			break;
        	default: 
        		result = new TextMessage(originalMessageText);
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
