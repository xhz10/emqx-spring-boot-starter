package com.xhz.emqxspringbootstarter.service.handler.impl;


import com.alibaba.fastjson.JSONObject;
import com.xhz.emqxspringbootstarter.client.MqttSendClient;
import com.xhz.emqxspringbootstarter.config.properties.EmqxBasicProperties;
import com.xhz.emqxspringbootstarter.config.properties.EmqxPubProperties;
import com.xhz.emqxspringbootstarter.model.MessageModel;
import com.xhz.emqxspringbootstarter.service.async.AsyncService;
import com.xhz.emqxspringbootstarter.service.handler.EventServiceHandler;
import com.xhz.emqxspringbootstarter.utils.JSONUtils;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 */
@Component
public class SuperPubService implements EventServiceHandler {

    protected static final Logger logger = LoggerFactory.getLogger(SuperPubService.class);

    private static MqttSendClient sendClient;

    private String topic;


    @Autowired
    private EmqxPubProperties emqxPubProperties;

    /*@Autowired
    private AsyncTaskExecutor asyncServiceExecutor;*/

    @Autowired
    private AsyncService asyncService;

    @Override
    public void doHandlerEvent(MessageModel messageModel, MqttCallbackExtended callback) {
        try {
            // 验证合法性
            // 数据封装
            if (sendClient == null) {
                logger.error("注入client失败");
                return;
            }
            String message = buildMessageAndTopic(messageModel);
            if (topic == null || "".equals(topic)) {
                logger.error("topic 数据为空，已经放入垃圾队列");
            }
            /**
             * 判断是否是异步执行
             */
            if (emqxPubProperties.isAsync()) {
                doHandlerAsync(topic, message, callback);
            } else {
                doHandlerSync(topic, message, callback);
            }
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        }
    }

    @Override
    public Future<MessageModel> doHandlerEventWithFuture(MessageModel messageModel, MqttCallbackExtended callback) {
        try {
            // 验证合法性
            // 数据封装
            if (sendClient == null) {
                logger.error("注入client失败");
                return new AsyncResult<>(null);
            }
            String message = buildMessageAndTopic(messageModel);
            if (topic == null || "".equals(topic)) {
                logger.error("topic 数据为空，已经放入垃圾队列");
            }
            /**
             * 判断是否是异步执行
             */
            if (emqxPubProperties.isAsync()) {
                return doHandlerAsync(topic, message, callback);
            } else {
                return new AsyncResult<>(null);
            }
        } catch (Exception e) {
            logger.error("发送消息失败", e);
        }
        return new AsyncResult<>(null);
    }


    /**
     * 设置topic
     *
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    protected String buildMessageAndTopic(MessageModel messageModel) {
        JSONObject json = new JSONObject();
        if (!ObjectUtils.isEmpty(messageModel)) {
            String topic = messageModel.getTopic();
            if (topic == null || "".equals(topic)) {
                setTopic("err");
                return JSONUtils.isErr("topic数据为空").toJSONString();
            } else {
                setTopic(topic);
            }
            try {
                json = (JSONObject) JSONObject.toJSON(messageModel);
            } catch (Exception e) {
                logger.error("JSON数据类型转换异常", e);
            }
        }
        return JSONUtils.isOk(json).toJSONString();
    }

    /**
     * <p>
     * 异步pub
     *
     * @param topic 主题
     */
    public Future<MessageModel> doHandlerAsync(String topic, String message, MqttCallbackExtended callback) {
        return asyncService.executeAsync(topic, message, callback);
    }

    /* *//**
     * 更新后的异步操作
     *
     * @param topic
     * @param message
     * @return
     *//*
    public Future<?> doHandlerAsyncV2(String topic, String message) {
        Future<?> submit = asyncServiceExecutor.submit(() -> {
            sendClient.publish(false, topic, message);
            logger.info("异步发送成功" + Thread.currentThread().getName());
        });
        return submit;
    }
*/

    /**
     * 同步pub
     *
     * @param topic
     * @param message
     */
    public void doHandlerSync(String topic, String message, MqttCallbackExtended callback) {
        sendClient.publish(false, topic, message, callback);
        logger.info("同步发送成功" + Thread.currentThread().getName());
    }

    @Autowired
    public void setSendClient(MqttSendClient sendClient) {
        SuperPubService.sendClient = sendClient;
    }

/*    @Autowired
    public void setAsyncServiceExecutor(AsyncTaskExecutor asyncServiceExecutor) {
        this.asyncServiceExecutor = asyncServiceExecutor;
    }*/

    public void setCallBack(MqttCallbackExtended callBack) {
        sendClient.setCallBack(callBack);
    }
}
