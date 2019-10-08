/*
 * Copyright 2018 LINE Corporation
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

package com.fet.crm.nspMicro.util.bean;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.linecorp.bot.model.action.CameraAction;
import com.linecorp.bot.model.action.CameraRollAction;
import com.linecorp.bot.model.action.LocationAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;

public class MessageWithQuickReplySupplier implements Supplier<Message> {
    @Override
    public Message get() {
        final List<QuickReplyItem> items = Arrays.<QuickReplyItem>asList(
                QuickReplyItem.builder()
	              .action(new MessageAction("宜蘭縣天氣如何?", "宜蘭縣天氣如何?"))
	              .build(),
	              QuickReplyItem.builder()
	              .action(new MessageAction("花蓮縣天氣如何?", "花蓮縣天氣如何?"))
	              .build(),
	              QuickReplyItem.builder()
	              .action(new MessageAction("臺東縣天氣如何?", "臺東縣天氣如何?"))
	              .build(),
	              QuickReplyItem.builder()
                  .action(new MessageAction("澎湖縣天氣如何?", "澎湖縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("金門縣天氣如何?", "金門縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("連江縣天氣如何?", "連江縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("臺北市天氣如何?", "臺北市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("新北市天氣如何?", "新北市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("桃園市天氣如何?", "桃園市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("臺中市天氣如何?", "臺中市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("臺南市天氣如何?", "臺南市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("高雄市天氣如何?", "高雄市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("基隆市天氣如何?", "基隆市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("新竹縣天氣如何?", "新竹縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("苗栗縣天氣如何?", "苗栗縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("彰化縣天氣如何?", "彰化縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("南投縣天氣如何?", "南投縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("雲林縣天氣如何?", "雲林縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("嘉義縣天氣如何?", "嘉義縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("嘉義市天氣如何?", "嘉義市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("屏東縣天氣如何?", "屏東縣天氣如何?"))
                  .build()
                              
//                QuickReplyItem.builder()
//                              .action(CameraAction.withLabel("CameraAction"))
//                              .build(),
//                QuickReplyItem.builder()
//                              .action(CameraRollAction.withLabel("CemeraRollAction"))
//                              .build(),
//                QuickReplyItem.builder()
//                              .action(LocationAction.withLabel("Location"))
//                              .build(),
//                QuickReplyItem.builder()
//                              .action(PostbackAction.builder()
//                                                    .label("PostbackAction")
//                                                    .text("PostbackAction clicked")
//                                                    .data("{PostbackAction: true}")
//                                                    .build())
//                              .build()
        );


        
        final QuickReply quickReply = QuickReply.items(items);

        return TextMessage
                .builder()
                .text("Message with QuickReply")
                .quickReply(quickReply)
                .build();
    }
}
