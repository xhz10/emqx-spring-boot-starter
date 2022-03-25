package com.xhz.emqxspringbootstarter.config.properties;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: xuhongzhuo
 * @Date: 2022/3/25 11:53 AM
 */
@Component
@ConfigurationProperties("emqx.sub")
@Data
public class EmqxSubProperties {

    /**
     * 映射
     */
    private Map<String, String> beanMap;

}
