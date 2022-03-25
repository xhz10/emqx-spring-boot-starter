package com.xhz.emqxspringbootstarter.service.handler.impl;



import com.xhz.emqxspringbootstarter.model.MessageModel;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/12
 */
@Component
public class PubServiceEasy extends SuperPubService {

    public void doMqttHandler(MessageModel messageModel, MqttCallbackExtended callback) {
        doHandlerEvent(messageModel, callback);
    }

    public Future<MessageModel> doMqttHandlerAsync(MessageModel messageModel) {
        return doHandlerEventWithFuture(messageModel, null);
    }
}
