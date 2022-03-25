package com.xhz.emqxspringbootstarter.service.async.impl;


import com.xhz.emqxspringbootstarter.client.MqttSendClient;
import com.xhz.emqxspringbootstarter.config.properties.EmqxBasicProperties;
import com.xhz.emqxspringbootstarter.model.MessageModel;
import com.xhz.emqxspringbootstarter.service.async.AsyncService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 */
@Component
public class AsyncServiceImpl implements AsyncService {


    private static Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);


    @Autowired
    private EmqxBasicProperties emqxBasicProperties;


    /**
     * 实际的异步执行
     * @param topic
     * @param pushMessage
     */
    @Override
    @Async("asyncServiceExecutor")
    public Future<MessageModel> executeAsync(String topic, String pushMessage, MqttCallbackExtended callback) {
        MqttClient mqttClient = connectV2(callback);
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(emqxBasicProperties.getQos());
            message.setRetained(false);
            message.setPayload(pushMessage.getBytes());
            mqttClient.publish(topic, message);
            logger.info("异步发送成功" + Thread.currentThread().getName() +mqttClient.getClientId());
            MessageModel futureModel = MessageModel
                    .builder()
                    .message(pushMessage)
                    .topic(topic)
                    .build();
            return new AsyncResult<MessageModel>(futureModel);
        } catch (MqttException e) {
            logger.error("异步发送失败",e);
            return new AsyncResult<>(MessageModel
                    .builder()
                    .message("发送失败，自行寻找原因")
                    .topic("err")
                    .build());
        }finally {
            closeConnect(mqttClient);
        }
    }

    public void closeConnect(MqttClient mqttClient) {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient connectV2(MqttCallbackExtended callback) {
        MqttClient client = null;
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            client = new MqttClient(emqxBasicProperties.getHostUrl(), uuid, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(emqxBasicProperties.getUsername());
            options.setPassword(emqxBasicProperties.getPassword().toCharArray());
            options.setConnectionTimeout(emqxBasicProperties.getTimeout());
            options.setKeepAliveInterval(emqxBasicProperties.getKeepAlive());
            options.setCleanSession(true);
            options.setAutomaticReconnect(false);
            try {
                // 设置回调
                client.setCallback(callback);
                //logger.info("设置了callBack" + callback.toString());
                client.connect(options);
                if (emqxBasicProperties.getTopics() != null && emqxBasicProperties.getTopics().length > 0) {
                    client.subscribe(emqxBasicProperties.getTopics());
                    logger.info("订阅的Topic有" + Arrays.toString(emqxBasicProperties.getTopics()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}
