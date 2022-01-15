
# xgame.java_server

----

### 介绍
- 该项目以前叫 xgame-code_server，是一个基于 Java 13 实现的游戏服务器框架；
- 一切代码都做了简化，服务器只分为业务服务器和网关服务器；
- 业务服务器会通过 ServerJobType 区分具体功能；
- 集群和配置服务使用了 Nacos；
- 消息通信部分使用 Google Protobuf；
- 删除了对 Excel 文档的支持，直接使用 JSON 或 CSV 文件；
- 如果想对照老版本代码，可以切换到 achive/2021_old_ver 这个分支上；

### 软件架构
- BizServer 业务服务器，所有的游戏逻辑都在这里实现；
- GatewayServer 网关服务器，维护客户端连接；
- Comm 一些通用代码，支撑上面两个服务器；

### 其它
- 如果有什么问题，可以来 Q 群聊聊：15123327（注明：来自 gitee）；
- 我还有个 Unity 前端项目，https://gitee.com/afrx_s_projz/missdie.u3d_client ；
- 搜索“海江的游戏开发”，B 站、抖音上都可以找到我；
