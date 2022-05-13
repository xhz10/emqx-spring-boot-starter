# 介绍一下这个SDK的用法吧

>其实这个东西主要在于使用EMQX服务器作为消息中间件的时候针对SpringBoot的API的封装

## 1. 使用方式

### 1.1 配置文件介绍

```yaml
# 按需求配置好配置文件
emqx:
  basic:
    hostUrl: tcp://ip:port
    username: 用户名
    password: 密码
    client-id: id
    cleanSession: true
    reconnect: true
    timeout: 100
    keepAlive: 100
    defaultTopic: testtopic
    isOpen: true # 必须是true
    qos: 1
    topics: # 关注的topic有哪些
      - "emqx/temperature"
      - "emqx/humidity"
      - "emqx/dht11"
      - "emqx/heart"
      - "emqx/pm2d5"
      - "emqx/sound"
  pub: # 发送端配置
    async: true # 是否开启异步发送，默认是false 
  sub: # 接受端配置
    beanMap: # 关注的topic和对应的接收消息后的实现类
      "[emqx/temperature]": "com.xhz.emqx.sub.handler.TemperatureHandler"
      "[emqx/dht11]": "com.xhz.emqx.sub.handler.Dht11Handler"
      "[emqx/device/#]": "com.xhz.emqx.sub.handler.DeviceOpHandler"
      "[emqx/heart]": "com.xhz.emqx.sub.handler.HeartHandler"
      "[emqx/pm2d5]": "com.xhz.emqx.sub.handler.PM2D5Handler"
      "[emqx/sound]": "com.xhz.emqx.sub.handler.SoundHandler"
      
# 接上述异步发送开启后的配置
xhz: # pub 那里开启异步发送后可以在这里配置线程池
  executor:
    # 核心线程数
    core-pool-size: 8
    # 最大线程数
    max-pool-size: 16
    # 等待队列
    queue-capacity: 9999
    # 自定义线程前缀
    thread-name-prefix: xhz-ws-executor-
```

### 1.2 具体应用

#### 1.2.1 测试端项目构建

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151534_87adede0_5560406.png "截屏2022-05-13 下午12.03.29.png")

#### 1.2.2 pom 添加依赖

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151551_292f1af4_5560406.png "截屏2022-05-13 下午12.06.13.png")

**注意如果依赖爆红说明maven仓库找不到这个依赖，需要手动下载该项目到本地进行mvn install 操作** **教程如下**

```bash
# 1. git clone 下来这个项目
git clone https://gitee.com/xu_hong_zhu/emqx-spring-boot-starter.git
# 2. 进入这个项目下
cd emqx-spring-boot-starter
# 3. 在安装了maven的前提下进行打包操作
mvn install
# *** 如果没安装maven那没办法了，自行百度吧 ***
```

**经过上述操作大概率就不会爆红了**

#### 1.2.3 编写发送类
![发送类](https://images.gitee.com/uploads/images/2022/0513/151203_4a23aec3_5560406.png "截屏2022-05-13 下午2.28.03.png")

>注意doProduce 方法的第二个参数一般情况下个人不喜欢写成匿名类的形式，一般都不去查看回调内容，所以个人喜欢的写法如下

![不带回调的发送类](https://images.gitee.com/uploads/images/2022/0513/151237_075efb2c_5560406.png "截屏2022-05-13 下午2.30.04.png")

#### 1.2.4 编写接收类

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151318_d99c7b28_5560406.png "截屏2022-05-13 下午2.30.42.png")

>可以看到接收类继承了SuperSubEventStrategy并且重写了doHandlerEvent方法 重写的内容就是消息接收到后执行的内容

#### 1.2.5 构建配置文件

```yaml
emqx:
  basic:
    hostUrl: tcp://ip:port # 这里一定要换成自己emqx服务器的ip和端口
    username: admin
    password: public
    client-id: equipment_main
    cleanSession: true
    reconnect: true
    timeout: 100
    keepAlive: 100
    defaultTopic: testtopic
    isOpen: true
    qos: 1
    topics:
      - "emqx/test" # 订阅的topic
  sub:
    bean-map:
      "[emqx/test]": com.xhz.testpub.sub.TestSub # 订阅的topic的具体执行
```

#### 1.2.6 执行结果查看

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151405_e8ba570e_5560406.png "截屏2022-05-13 下午2.32.11.png")

>写了一个Controller作为运行入口	

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151428_a9a99042_5560406.png "截屏2022-05-13 下午2.33.51.png")

>执行成功，查看一下控制台看看输出结果

![输入图片说明](https://images.gitee.com/uploads/images/2022/0513/151445_600064d2_5560406.png "截屏2022-05-13 下午2.34.45.png")

>可以观察到sub已经接收到数据了

