package com.xhz.emqxspringbootstarter.config;



import com.xhz.emqxspringbootstarter.callback.MqttAcceptCallback;
import com.xhz.emqxspringbootstarter.client.MqttAcceptClient;
import com.xhz.emqxspringbootstarter.client.MqttSendClient;
import com.xhz.emqxspringbootstarter.config.condition.EmqxCondition;
import com.xhz.emqxspringbootstarter.config.properties.EmqxBasicProperties;
import com.xhz.emqxspringbootstarter.config.properties.EmqxPubProperties;
import com.xhz.emqxspringbootstarter.config.properties.EmqxSubProperties;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/12
 */
@SpringBootConfiguration
@EnableConfigurationProperties({EmqxBasicProperties.class, EmqxSubProperties.class, EmqxPubProperties.class})
@ConditionalOnClass(EmqxCondition.class)
public class MqttAutoConfiguration {


    private static Logger logger = LoggerFactory.getLogger(MqttAutoConfiguration.class);

    /**
     * 目前就这里用到，其实没必要放入IOC容器中
     */
    @Autowired
    private MqttAcceptCallback mqttAcceptCallback;


   /* public MqttClient mqttClient(EmqxBasicProperties emqxBasicProperties) {
        MqttClient mqttClient = null;
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            if (emqxBasicProperties.getIsOpen() == null || !emqxBasicProperties.getIsOpen()) {
                logger.info("默认创建的bean");
                return new MqttClient("tcp://localhost:1883", uuid, new MemoryPersistence());
            }
            mqttClient = new MqttClient(emqxBasicProperties.getHostUrl(), uuid, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(emqxBasicProperties.getUsername());
            options.setPassword(emqxBasicProperties.getPassword().toCharArray());
            options.setConnectionTimeout(emqxBasicProperties.getTimeout());
            options.setKeepAliveInterval(emqxBasicProperties.getKeepAlive());
            options.setCleanSession(true);
            options.setAutomaticReconnect(false);
            mqttClient.setCallback(mqttAcceptCallback);
            mqttClient.connect(options);
            *//**
             * 配置监听哪些topic
             *//*
            if (emqxBasicProperties.getTopics() != null && emqxBasicProperties.getTopics().length > 0) {
                mqttClient.subscribe(emqxBasicProperties.getTopics());
                logger.info("订阅的Topic有" + Arrays.toString(emqxBasicProperties.getTopics()));
            }
        } catch (MqttException e) {
            logger.error("初始化异常", e);
            e.printStackTrace();
        }
        return mqttClient;
    }*/
    @Bean
    @ConditionalOnClass(EmqxCondition.class)
    public MqttSendClient mqttSendClient(EmqxBasicProperties emqxBasicProperties) {
        MqttSendClient mqttSendClient = new MqttSendClient();
        mqttSendClient.setMqttClient(getMqttClient(emqxBasicProperties));
        mqttSendClient.setEmqxBasicProperties(emqxBasicProperties);
        return mqttSendClient;
    }


    @Bean
    @ConditionalOnClass(EmqxCondition.class)
    public MqttAcceptClient mqttAcceptClient(EmqxBasicProperties emqxBasicProperties) {
        MqttAcceptClient mqttAcceptClient = new MqttAcceptClient();
        mqttAcceptClient.setClient(getMqttClient(emqxBasicProperties));
        mqttAcceptClient.setCallback(mqttAcceptCallback);
        return mqttAcceptClient;
    }

    private MqttClient getMqttClient(EmqxBasicProperties emqxBasicProperties) {
        MqttClient mqttClient = null;
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            if (emqxBasicProperties.getIsOpen() == null || !emqxBasicProperties.getIsOpen()) {
                logger.info("默认创建的bean");
                mqttClient = new MqttClient("tcp://localhost:1883", uuid, new MemoryPersistence());
            } else {
                mqttClient = new MqttClient(emqxBasicProperties.getHostUrl(), uuid, new MemoryPersistence());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(emqxBasicProperties.getUsername());
                options.setPassword(emqxBasicProperties.getPassword().toCharArray());
                options.setConnectionTimeout(emqxBasicProperties.getTimeout());
                options.setKeepAliveInterval(emqxBasicProperties.getKeepAlive());
                options.setCleanSession(true);
                options.setAutomaticReconnect(false);
                // mqttClient.setCallback(mqttAcceptCallback);
                mqttClient.connect(options);
                /**
                 * 配置监听哪些topic
                 */
                if (emqxBasicProperties.getTopics() != null && emqxBasicProperties.getTopics().length > 0) {
                    mqttClient.subscribe(emqxBasicProperties.getTopics());
                    logger.info("订阅的Topic有" + Arrays.toString(emqxBasicProperties.getTopics()));
                }
            }
        } catch (MqttException e) {
            logger.error("初始化异常", e);
            e.printStackTrace();
        }
        return mqttClient;
    }
}
