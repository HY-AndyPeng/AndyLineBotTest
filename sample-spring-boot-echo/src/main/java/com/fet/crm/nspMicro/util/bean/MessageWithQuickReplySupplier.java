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
	              .action(new MessageAction("宜蘭縣", "宜蘭縣天氣如何?"))
	              .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("臺北市", "臺北市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("新北市", "新北市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("桃園市", "桃園市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("基隆市", "基隆市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("新竹縣", "新竹縣天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("新竹市", "新竹市天氣如何?"))
                  .build(),
                  QuickReplyItem.builder()
                  .action(new MessageAction("苗栗縣", "苗栗縣天氣如何?"))
                  .build()
                              
        );


        
        final QuickReply quickReply = QuickReply.items(items);

        return TextMessage
                .builder()
                .text("請選擇地區")
                .quickReply(quickReply)
                .build();
    }
}
