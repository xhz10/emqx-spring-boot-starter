package com.xhz.emqxspringbootstarter.config.executorconf;



import com.xhz.emqxspringbootstarter.config.condition.ExecutorCondition;
import com.xhz.emqxspringbootstarter.config.properties.ExecutorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 */
@EnableAsync(proxyTargetClass = true)
@SpringBootConfiguration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ExecutorConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Autowired
    private ExecutorProperties executorProperties;

    @Bean("asyncServiceExecutor")
    @Conditional(ExecutorCondition.class)
    public AsyncTaskExecutor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        // 因为要进行IO密集型操作，所以是CPU核数 * 2
        executor.setCorePoolSize(executorProperties.getCorePoolSize() == 0 ? 4 : executorProperties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(executorProperties.getMaxPoolSize() == 0 ? 8 : executorProperties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(executorProperties.getQueueCapacity() == 0 ? 9999 : executorProperties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        String name = executorProperties.getThreadNamePrefix();
        if(name == null || "".equals(name)) {
            executor.setThreadNamePrefix("xhz-executor");
        } else {
            executor.setThreadNamePrefix(name);
        }
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
