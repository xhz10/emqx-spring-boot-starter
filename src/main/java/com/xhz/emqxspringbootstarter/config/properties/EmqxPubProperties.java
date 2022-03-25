package com.xhz.emqxspringbootstarter.config.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: xuhongzhuo
 * @Date: 2022/3/25 11:53 AM
 */
@Component
@ConfigurationProperties("emqx.pub")
@Data
public class EmqxPubProperties {

    /**
     * 是否开启异步发送
     */
    private boolean async;

}
