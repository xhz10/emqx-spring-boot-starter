# 介绍一下这个SDK的用法吧

>其实这个东西主要在于使用EMQX服务器作为消息中间件的时候针对SpringBoot的API的封装
>emqx 服务器的安装参考 https://www.emqx.io/docs/zh/v4.4/getting-started/install.html#zip-%E5%8E%8B%E7%BC%A9%E5%8C%85%E5%AE%89%E8%A3%85-linux%E3%80%81macos%E3%80%81windows

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

## 2. 源码讲解

**首先强调！！！！！！！**

**先来一段求生欲**

**写这个东西的初衷是我在做毕业设计的时候考虑到使用emqx的时候原生API调用相对繁琐，所以自己通过一些经验的积累按照兴趣封装了一下，这个东西不高级也不低级，可以理解是恶趣味，也算是印证自身一些想法的四不像产物，代码上会有很多漏洞，也可能某些地方设计的不好，所以如果有新的想法可以和我探讨。**

**一切设计都是基于场景而开发的，可能某些方面能用但是不合理却恰恰符合我毕设上的某个需求呢，对吧！**

### 2.1 总体介绍

该工具主要基于SpringBoot的自动装配功能开发，主要目的在于让使用者根据配置文件动态的配置MQTT通信相关参数

### 2.2 配置类实现


观察这个包可以看到有四个模块，其中condition分别是启动条件和开启**异步**后的线程池配置，而properties主要用于配置文件配置项的设置其中配置内容自行看代码，而MqttAutoConfiguration类则是分别初始化发送和接收的客户端2.4可查看

### 2.3 model模块

![image-20220513162003980](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513162003980.png)

主要分为三个部分，topic主题， message内容，type 自定义类型

### 2.4 client客户端模块

![image-20220513162244598](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513162244598.png)

该模块主要负责发送端和接受端的客户端内容定义

#### 2.4.1 发送端客户端

![image-20220513162607426](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513162607426.png)

核心方法，即publish内容到消息服务器中

#### 2.4.2 接收端客户端

![image-20220513162810167](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513162810167.png)

相信可以看出来，这个类目前没啥用，接收功能我封装成另一种方式了（后续会介绍），这个类主要作用在于设置接收端的回调类以及配置topic，如下图可以看到

![image-20220513172501857](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513172501857.png)

### 2.5 发送模块封装

详细类关系如下

![image-20220513163318292](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513163318292.png)

通过分析类图可以发现，最上层接口为EventServiceHandler 它规范了两种发送方式，即**带返回值Future的发送**与**不带返回值Future的发送**（Future我也了解的不深，可能这块代码写的会不好）

而SuperPubService实现了EventServiceHanler的规范，从他聚合了AsyncService也可以看出AsyncService作为上述规范中**带Future的异步发送数据的功能**的实现，自身则提供了一些**同步发送以及异步发送的功能**实现

PubServiceEasy作为SuperPubService的继承者，针对发送方式做了最后一次构建，即**同步发送不带返回值带回调**、**异步发送带返回值不带回调**（回调参数如果自己选择**null**就没有回调了哦）

最后说到EventHandlerContext，上述所有类的介绍都是发送功能的实现相关，而该类作为一个使用者，聚合了上述功能，从一个使用者的角度去设计源代码也很好理解，都是调用Pub的

![image-20220513164601271](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513164601271.png)

但是虽然目前它存在的意义不大，那如果换个角度思考，**数据的内容审核，安全检查，数据校验**是不是都可以在这个类里面去做，所以这个类也可以理解成一个扩展功能，扩展内容可以随着以后看心情去加。

### 2.6 接收模块的构建

**首先我从五月份实习开始，接触最多的设计模式就是工厂加策略的组合模式，所以这次接收模块我也利用这个组合模式去构建的，正所谓：万物皆可策略模式**

具体类关系如下

![image-20220513171133471](/Users/xuhongzhuo/Library/Application Support/typora-user-images/image-20220513171133471.png)

首先我们来分析这个类图，**从上到下分别是一层一层的聚合关系**，当然最开始我们定义了一个**事件策略规范**，即EventStrategy，SuperSubEventStrategy则是作为TestSub的父类存在，TestSub是什么？在第一章的实现过程我们看到doHandlerEvent的内容是当topic的信息接收到后我们做的事情，而这个做事的方法就是继承自SuperSubEventStrategy。所以我们再观察StrategyMap这个类，我们看到了它聚合了SuperSubEventStrategy，由此根据策略模式我们可以分析出，StrategyMap作为策略的存储容器，**返回的策略都是SuperSubEventStrategy（所有具体策略的父类）**，而StrategyMap可以作为所有的策略的策略工厂来理解，**策略工厂产生策略**，看上去非常合理，那么工厂是如何实现的呢？通过聚合关系我们发现StrategyMap中聚合了一个ApplicationContext类。这是Spring中的bean工厂，我利用StrategyMap的init方法，在**它初始化的时候从配置文件中读出topic对应的实现类，并且写入了IOC容器**，这样策略工厂就构建完毕了。

我们继续看聚合关系，往下看会发现CallBackTopicContext聚合了策略工厂StrategyMap，，其中有一个execute方法，我们不难猜测这个方法的功能就是根据**入参从工厂中取出策略并且执行策略的doHandlerEvent**。

最后跟着聚合关系我们会发现MqttAcceptCallBack这个类聚合了CallBackTopicContext，上述介绍接受端客户端的时候提到过一嘴这个类，这个类负责接收所有自己订阅过的topic的内容，而是否订阅则在配置文件中配置，而聚合了CallBackTopicContext后，我们会意识到，每一个收到的不同类型的topic与内容都将被放入CallBackTopicContext中去按照策略分发topic从而分别执行实现类。那么我们思考一下，MqttAcceptCallBack这个类其实一开始完全不需要只关注配置文件订阅过的topic，而是关注所有topic即可，反正最后内容的执行是依靠CallBackTopicContext去做的，当然这个无伤大雅，后续可以抽时间更新即可。

## 3. 个人总结

这个小工具还是让我学到了很多的，主要开发都是为了迎合我自己的毕业设计，如果有新的想法可以在评论区和我沟通交流，我始终认为只有思想上的碰撞才会出现共同进步，而我周围的人都是值得我学习的人，从好多人身上我也学到了很多很多。希望未来的人生路上会有更多志同道合的人，我们共勉。