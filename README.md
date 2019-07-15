# snake 百万级别的实时通讯NIO框架

数据库建议：Ignite/cockroach
发现服务：Zookeeper

1. messagecreator: 

基于Netty的Java消息对象池，基于Netty零拷贝专为Netty做优化。

长时间运行，消息池和Netty总内存占用不超过2G，不用考虑GC。


2. zk-common

Zookeeper基础服务发现组件。


3. login

登陆服务器，Http请求，实际应用中改为Https，集中登陆时用户排队。

返回用户登陆状态、token和connector的IP和Port。

可以部署多个login服务器，去除单点，不存储状态。

命令服务：Zookeeper发现centerserver，Netty长连接到centerserver，
		  根据数据库表user_state.id%centerserver个数来查找。

4. centerserver

中心服务器，可以部署多个，去除单点，不存储状态。

根据数据库表user_state.id%centerserver总个数来查找。

处理用户登陆、退出等消息。

5. connector

网关服务器，部署多个。

每个客户端连接在用户认证成功时，创建一个到transfer服务器的连接，把消息转发过去。

这两个连接用同一个EventLoop，Netty零拷贝加快传输速度。

性能：服务器 i3 CPU, 16G 内存，部署login，centerserver，connector（回传）。
	  客户端：ThinkPad笔记本，40字节消息 * 1000000个每用户，800k/s平均传输速率。
	  connector CPU占用不超过11%。
	  推算：每秒可以处理8 * 800K = 6.4M加密消息(回传测试)。

命令服务：Zookeeper发现centerserver，Netty长连接到centerserver，
		  根据数据库表user_state.id%centerserver个数来查找。
		  
认证算法：mangos认证算法，Secure Remote Password（安全远程密码），现在明文传输token。
加密算法：mangos RC4算法。

6. transfer

转发服务器，可以直接用来做逻辑服务器。

转发服务器开始发送的都是明文。

如果后台功能较多，可以直连逻辑服务器（邮件）、聊天服务器（家族、好友）、排行服务器、跨服战斗服务器等。

逻辑服务器直连充值服务器。


7. client

测试用客户端。
