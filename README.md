# xgame-code_server-java8
Xgame 是一个基于 Java 语言实现的 SLG 游戏服框架。
（客户端项目是 xgame-code_client，不过目前还没有提交任何代码）
Xgame 消息层使用 MINA，IO 采用 EclipseLink + MySQL，场景服务是我自己写的一个单线程框架。
这套框架的最初来源是“人人游戏”的服务器架构，
当然，仅限于实现思路而不是代码……
原来的代码存在着诸多的不严谨，所以问题一直都比较严重。
而 Xgame 则做出了很多改进！例如：
* 将消息解码与消息黏包算法分开；
* 修改 IoOperation 的返回值为 boolean 类型，以避免死循环（纠正了原框架不严谨的地方）；
* Excel 模板加载类动态创建并编译；
* 日志服务器框架调整为 HTTP 方式；
