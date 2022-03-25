package com.xhz.emqxspringbootstarter.service.context;


import com.xhz.emqxspringbootstarter.model.MessageModel;
import com.xhz.emqxspringbootstarter.service.handler.impl.PubServiceEasy;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 * <p>
 * 事件执行的Context
 * <p>
 * 目前觉得Context存在的意义不大，但是后续可以继续完善
 */
@Component
public class EventHandlerContext {

    private static Logger logger = LoggerFactory.getLogger(EventHandlerContext.class);


    @Autowired
    private PubServiceEasy pubServiceEasy;


    /**
     * @param messageModel
     */
    public void doProduce(MessageModel messageModel, MqttCallbackExtended callback) {
        pubServiceEasy.doMqttHandler(messageModel, callback);
    }

    public Future<MessageModel> doProduceWithFuture(MessageModel messageModel) {
        return pubServiceEasy.doMqttHandlerAsync(messageModel);
    }
}
