# xgame.java_server

----

### 介绍
该项目以前叫 xgame-code_server，是一个基于 Java 13 实现的游戏服务器框架。
一切代码都做了简化，服务器只分为业务服务（BizServer）器和网关服务器（GatewayServer）。

### 软件架构
- BizServer 业务服务器，所有的游戏逻辑都在这里实现；
- GatewayServer 网关服务器，维护客户端连接；
- Comm 一些通用代码，支撑上面两个服务器；

