package com.xhz.emqxspringbootstarter.strategy;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/11
 * <p>
 * 目前抽象类，主要负责Sub的抽象内容，是一个抽象的策略，里面多封装一些类
 */
@Component
public class SuperSubEventStrategy implements EventStrategy {

    protected Logger logger = LoggerFactory.getLogger(SuperSubEventStrategy.class);


    /**
     * Sub 实际执行类
     *
     * @param topic
     * @param mqttMessage
     */
    public void doHandler(String topic, MqttMessage mqttMessage) {
        logger.info("鉴权: " + topic);
        logger.info("数据校验: " + mqttMessage.toString());
        logger.info("内容审核: " + mqttMessage.toString());
        doHandlerEvent(topic, mqttMessage);
    }

    @Override
    public void doHandlerEvent(String topic, MqttMessage mqttMessage) {
        logger.info("super do it");
    }
}
