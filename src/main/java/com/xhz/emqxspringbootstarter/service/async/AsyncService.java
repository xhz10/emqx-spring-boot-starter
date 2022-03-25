package com.xhz.emqxspringbootstarter.service.async;



import com.xhz.emqxspringbootstarter.model.MessageModel;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

import java.util.concurrent.Future;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 *
 * 执行异步任务的接口
 */
public interface AsyncService {

    /**
     * 异步任务的执行
     */
    Future<MessageModel> executeAsync(String topic, String message, MqttCallbackExtended callback);
}
