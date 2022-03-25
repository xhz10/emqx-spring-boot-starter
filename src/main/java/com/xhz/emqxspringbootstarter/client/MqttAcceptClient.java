package com.xhz.emqxspringbootstarter.client;


import com.xhz.emqxspringbootstarter.callback.MqttAcceptCallback;
import com.xhz.emqxspringbootstarter.config.properties.EmqxBasicProperties;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/09
 */
public class MqttAcceptClient {

    private static final Logger logger = LoggerFactory.getLogger(MqttAcceptClient.class);

    public MqttClient client;

    public MqttClient getClient() {
        return this.client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void setCallback(MqttCallback callback) {
        this.client.setCallback(callback);
    }

    /**
     * 客户端连接
     */
    /*public void connect() {
        MqttClient client;
        try {
            client = new MqttClient(emqxBasicProperties.getHostUrl(), emqxBasicProperties.getClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(emqxBasicProperties.getUsername());
            options.setPassword(emqxBasicProperties.getPassword().toCharArray());
            options.setConnectionTimeout(emqxBasicProperties.getTimeout());
            options.setKeepAliveInterval(emqxBasicProperties.getKeepAlive());
            options.setAutomaticReconnect(emqxBasicProperties.getReconnect());
            options.setCleanSession(emqxBasicProperties.getCleanSession());
            setClient(client);
            try {
                // 设置回调
                client.setCallback(mqttAcceptCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 重新连接
     */
    /*public void reconnection() {
        try {
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
   /* public void subscribe(String topic, int qos) {
        logger.info("==============开始订阅主题==============" + topic);
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 取消订阅某个主题
     *
     * @param topic
     */
 /*   public void unsubscribe(String topic) {
        logger.info("==============开始取消订阅主题==============" + topic);
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }*/
}

