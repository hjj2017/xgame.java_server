package com.game.gameServer.msg.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.game.part.msg.MsgLog;

/**
 * 开始监听 CG 消息
 *
 * @author hjj2017
 * @since 2016/1/8
 *
 */
public interface IServerStartUp_ListenCGMsg {
    /** 消息处理器数组 */
    static final ChannelHandler[] HANDLER_ARR = {
        new MsgDecoder(),
        new MsgEncoder(),
        new MyChannelHandler(),
    };

    /**
     * 开始监听 CG 消息
     *
     */
    default void startUpListenCGMsg() {
        // 记录异步操作服务初始化日志
        MsgLog.LOG.info(":: 准备监听 CG 消息");

        //
        // 注意这里 : 直接用的默认构造器,
        // NioEventLoopGroup 会默认创建 CPU 数量 x2 条线程.
        // 即有 1 个 CPU 时创建 2 条线程;
        // 当有 2 个 CPU 时创建 4 条线程;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 创建服务器引导程序
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup, bossGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(HANDLER_ARR);
                }
            })
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            // 绑定 IP 地址和端口号
            ChannelFuture f = b.bind(new InetSocketAddress(
                SocketConf.OBJ._bindIpAddr,
                SocketConf.OBJ._bindPort
            )).sync();

            f.channel().closeFuture().sync();
        } catch (Exception ex) {
            // 记录并抛出异常
            MsgLog.LOG.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            // 关闭线程组
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
