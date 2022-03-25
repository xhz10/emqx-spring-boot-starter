package com.xhz.emqxspringbootstarter.strategy;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/11
 */
public interface EventStrategy {

    /**
     * 事件统一处理程序
     */
    void doHandlerEvent(String topic, MqttMessage mqttMessage);
}
