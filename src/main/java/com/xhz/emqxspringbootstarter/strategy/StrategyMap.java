package com.xhz.emqxspringbootstarter.strategy;


import com.xhz.emqxspringbootstarter.config.properties.EmqxSubProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/15
 */
@Component
public class StrategyMap implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(StrategyMap.class);

    private static Map<String, SuperSubEventStrategy> beanMap = new HashMap<>();

    @Autowired
    private EmqxSubProperties emqxSubProperties;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * beanMap 的初始化操作
     * 把beanName和topic的映射关系放入Map
     */
    private void init() throws ClassNotFoundException {
        Map<String, String> propMap = emqxSubProperties.getBeanMap();
        if (!CollectionUtils.isEmpty(propMap)) {
            // 如果获取到了配置文件
            Iterator<String> iter = propMap.keySet().iterator();
            while (iter.hasNext()) {
                // 根据全类名构建类
                String classKey = iter.next();
                String classValue = propMap.get(classKey);
                logger.info("classKey is :" + classKey + "   " + "classValue is : " + classValue);
                SuperSubEventStrategy strategy = (SuperSubEventStrategy) applicationContext.getBean(Class.forName(classValue));
                beanMap.put(classKey,strategy);
            }
        }
    }

   /* @Scheduled(cron = "0 * * * * ?")
    public void updateBeanMap() throws ClassNotFoundException {
        logger.info("1分钟初始化一次beanMap");
        init();
    }*/


    public SuperSubEventStrategy getStrategy(String topic) {
        logger.info(beanMap.get(topic).toString());
        return beanMap.get(topic);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // bean 初始化的时候执行一次
        init();
    }
}
