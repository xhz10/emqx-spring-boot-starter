package com.xhz.emqxspringbootstarter.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/14
 *
 * 用户自定义线程池
 */
@Component
@ConfigurationProperties("xhz.executor")
@Data
public class ExecutorProperties {

    /**
     * 核心线程数目
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maxPoolSize;

    /**
     * 队列大小
     */
    private int queueCapacity;

    /**
     * 自定义线程池前缀
     */
    private String threadNamePrefix;
}
