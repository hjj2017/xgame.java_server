package com.game.gameServer.msg.mina;

import com.game.gameServer.framework.FrameworkError;
import com.game.part.msg.MsgLog;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

/**
 * 开始端口监听
 * 
 * @author hjj2019
 *
 */
public interface IServerStartUp_ListenCGMsg {
	/**
	 * 开始监听 CG 消息
	 * 
	 */
	default void startUpListenCGMsg() {
		// 记录异步操作服务初始化日志
		MsgLog.LOG.info(":: 准备监听 CG 消息");

		// 创建 IO 接收器
		NioSocketAcceptor acceptor = new NioSocketAcceptor();

		// 获取责任链
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		// 处理网络粘包
		chain.addLast("msgCumulative", new MsgCumulativeFilter());

		if (SocketConf.OBJ._msgEncrypt) {
			// 如果消息需要加密...
			chain.addLast(
				"msgDecrypt",
				new MsgDecryptFilter()
			);
		}

		// 添加自定义编解码器
		chain.addLast("msgCodec", new ProtocolCodecFilter(
			new MsgEncoder(),
			new MsgDecoder()
		));

		// 获取会话配置
		IoSessionConfig cfg = acceptor.getSessionConfig();

		// 设置缓冲区大小
		cfg.setReadBufferSize(2048);
		// 设置 session 空闲时间
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 10);

 		// 设置 IO 句柄
		acceptor.setHandler(new MsgIoHandler());
		acceptor.setReuseAddress(true);

		try {
			// 绑定端口
			acceptor.bind(new InetSocketAddress(
				SocketConf.OBJ._bindIpAddr,
				SocketConf.OBJ._bindPort
			));
		} catch (Exception ex) {
			// 输出异常并停止服务器
			MsgLog.LOG.error(ex.getMessage(), ex);
			// 抛出异常!
			throw new FrameworkError(ex);
		}
	}
}
