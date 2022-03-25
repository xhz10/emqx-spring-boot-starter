package com.xhz.emqxspringbootstarter.client;


import com.xhz.emqxspringbootstarter.config.properties.EmqxBasicProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * @author Hongzhuo Xu
 * @date 2022/01/09
 */
public class MqttSendClient {

    private static final Logger logger = LoggerFactory.getLogger(MqttSendClient.class);


    private EmqxBasicProperties emqxBasicProperties;

    private MqttClient mqttClient;


    public void connect(MqttCallbackExtended callback) {
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            mqttClient = new MqttClient(emqxBasicProperties.getHostUrl(), uuid, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(emqxBasicProperties.getUsername());
            options.setPassword(emqxBasicProperties.getPassword().toCharArray());
            options.setConnectionTimeout(emqxBasicProperties.getTimeout());
            options.setKeepAliveInterval(emqxBasicProperties.getKeepAlive());
            options.setCleanSession(true);
            options.setAutomaticReconnect(false);
            try {
                // 设置回调
                mqttClient.setCallback(callback);
                mqttClient.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
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
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * 发布消息
     * 主题格式：
     *
     * @param retained    是否保留
     * @param orgCode     orgId
     * @param pushMessage 消息体
     */
    public void publish(boolean retained, String orgCode, String pushMessage, MqttCallbackExtended callback) {
        MqttMessage message = new MqttMessage();
        message.setQos(emqxBasicProperties.getQos());
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        setCallBack(callback);
        // connect(callback);
        try {
            mqttClient.publish(orgCode, message);
        } catch (MqttException e) {
            e.printStackTrace();
        } finally {
            /*disconnect();
            close();*/
        }
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        try {
            if (mqttClient != null) mqttClient.disconnect();
            logger.info("断开连接");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void close() {
        try {
            if (mqttClient != null) mqttClient.close();
            logger.info("释放资源");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void setEmqxBasicProperties(EmqxBasicProperties emqxBasicProperties) {
        this.emqxBasicProperties = emqxBasicProperties;
    }

    public void setCallBack(MqttCallbackExtended callBack) {
        mqttClient.setCallback(callBack);
    }
}

