package com.xhz.emqxspringbootstarter.service.handler;



import com.xhz.emqxspringbootstarter.model.MessageModel;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 */
public interface EventServiceHandler {

    /**
     * 具体发布数据执行操作
     *
     * @param messageModel
     */
    void doHandlerEvent(MessageModel messageModel, MqttCallbackExtended callback);

    Future<MessageModel> doHandlerEventWithFuture(MessageModel messageModel, MqttCallbackExtended callback);

}
