package com.xhz.emqxspringbootstarter.callback;


import com.xhz.emqxspringbootstarter.strategy.StrategyMap;
import com.xhz.emqxspringbootstarter.strategy.SuperSubEventStrategy;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/11
 * <p>
 * 负责Topic的分发
 */
@Component
public class CallBackTopicContext {


    @Autowired
    private StrategyMap strategyMap;

    public void execute(String topic, MqttMessage mqttMessage) {
        SuperSubEventStrategy nowStrategy = strategyMap.getStrategy(topic);
        nowStrategy.doHandler(topic, mqttMessage);
    }
}
