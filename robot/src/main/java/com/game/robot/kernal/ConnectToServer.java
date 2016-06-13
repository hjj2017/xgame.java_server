package com.game.robot.kernal;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.game.gameServer.msg.netty.MsgDecoder;
import com.game.gameServer.msg.netty.MsgEncoder;
import com.game.robot.RobotLog;

/**
 * 连接到游戏服务器
 * 
 * @author hjj2017
 * @since 2016/5/27
 *
 */
class ConnectToServer {
	/** 单例对象 */
	public static ConnectToServer OBJ = new ConnectToServer();

	/** 创建事件循环 */
	private final EventLoopGroup _eventLoopGroup = new NioEventLoopGroup();
	
	/** 游戏服 IP 地址 */
	public String _gameServerIpAddr = "0.0.0.0";
	/** 游戏服端口号 */
	public int _gameServerPort = 8001;
	/** 引导程序 */
	private Bootstrap _B;

	/**
	 * 类默认构造器
	 * 
	 */
	private ConnectToServer() {
	}

	/**
	 * 连接到服务器并且获得 IO 会话
	 * 
	 */
	Channel connectAndGetChannel() {
		if (this._B == null) {
			synchronized (ConnectToServer.class) {
				if (this._B == null) {
					// 创建客户端引导程序
					Bootstrap b = new Bootstrap();

					b.group(_eventLoopGroup);
					b.option(ChannelOption.TCP_NODELAY, true);
					b.option(ChannelOption.SO_KEEPALIVE, true);
					b.channel(NioSocketChannel.class);
					b.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
								new MsgDecoder(),
								new MsgEncoder()
							);
						}
					});

					this._B = b;
				}
			}
		}

		try {
			// 连接服务器后获取信道
			Channel ch = this._B.connect(new InetSocketAddress(
				this._gameServerIpAddr,
				this._gameServerPort
			)).await().channel();

			return ch;
		} catch (Exception ex) {
			// 记录并抛出异常
			RobotLog.LOG.error(ex.getMessage(), ex);
		}

		return null;
	}
}
