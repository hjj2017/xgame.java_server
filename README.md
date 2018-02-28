关于 Xgame-code_server
====
#### 游戏服务器开发本质上是完成业务逻辑向多线程的映射
----

Xgame-code_server 是一个基于 Java 语言实现的适用于 SLG 游戏类型的服框架。
项目中包括：
- game_part 零件部分；
- game_server 游戏服务器，有点类似于 Tomcat Server；
- game_bizModule 业务模块；

Xgame-code_server 消息层使用 Netty，IO 采用 EclipseLink + MySQL，场景服务为单线程架构。
这套框架的最初来源是“人人游戏”的服务器架构，
当然，仅限于实现思路而不是代码……
Xgame-code_server 参考了以往的经验，同时也做出了很多改进！
例如：

- 修改 IoOperation 的返回值为 boolean 类型，以避免死循环（纠正了原框架不严谨的地方）；
- 使用通用 Dao 代码，减少无用的 Dao 派生类；
- **使用 Javassist 技术动态创建并编译 Excel 模板类**；
- Excel 模板类支持多层嵌套（理论上没有上限）；
- **使用 Javassist 技术动态创建并编译消息类**；
- 消息类支持多层嵌套（理论上没有上限）；
- 修改延迟保存罗辑（LazySaving），使用起来更方便、更明确，只实现 ILazySavingObj 接口即可；
- 使用 Maven 打包和部署；

----

# 关注我们

QQ 群：327606822

微信公众号：myXgame

![微信公众号](WeiXinGongZhongHao.jpg "myXgame")

----

# Xgame 宗旨

- 精确；
- 极简；
- 注重架构设计的同时，对代码风格的要求极严苛；
- 有意思；

关于“有意思”这件事，后续的文档中会陆续展现……
