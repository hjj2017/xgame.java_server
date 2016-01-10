package com.game.robot.kernal;

import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
 * 机器人类
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class Robot {
    /** 创建事件循环 */
    static final EventLoopGroup NETTY_WORK_GROUP = new NioEventLoopGroup();
    /** 机器人计数器 */
    static final AtomicInteger ROBOT_COUNTER = new AtomicInteger(0);

    /** 游戏服 IP 地址 */
    public String _gameServerIpAddr = "0.0.0.0";
    /** 游戏服端口号 */
    public int _gameServerPort = 8001;
    /** 游戏服务器名称 */
    public String _gameServerName = "S01";
    /** 用户名称 */
    public String _userName = "";
    /** 用户密码 */
    public String _userPass = "";
    /** 玩家角色 UId */
    public long _humanUId = -1L;

    /** 运行策略 */
    private FocusModule _currFocusModule = null;
    /** 消息队列 */
    final LinkedBlockingQueue<? super Object> _msgQ = new LinkedBlockingQueue<>();
    /** 数据字典 */
    public final Map<Object, Object> _dataMap = new ConcurrentHashMap<>();
    /** 信道对象 */
    Channel _ch = null;

    /**
     * 类参数构造器
     * 
     * @param userName 用户名称
     * @param userPass 用户密码
     * 
     */
    Robot(String userName, String userPass) {
        this._userName = userName;
        this._userPass = userPass;
    }

    /**
     * 设置当前被聚焦的模块
     * 
     * @param value
     * 
     */
    void putCurrFocusModule(FocusModule value) {
        this._currFocusModule = value;
    }

    /**
     * 跳转到下一个功能模块
     * 
     */
    public void gotoNextModule() {
        if (this._currFocusModule != null) {
            this._currFocusModule = this._currFocusModule.getNext();
        }
    }

    /**
     * 跳转到下一个功能模块并执行 Ready 命令
     * 
     * @see Robot#gotoNextModule()
     * 
     */
    public void gotoNextModuleAndReady() {
        // 先跳转到下一个功能模块
        this.gotoNextModule();

        if (this._currFocusModule != null) {
            // 如果当前聚焦的模块不为空, 则
            // 尝试执行 Ready 命令...
            this._currFocusModule.test(this, ModuleReadyCmd.OBJ);
        }
    }

    /**
     * 启动机器人
     * 
     */
    void start() {
        if (this._currFocusModule == null) {
            // 如果当前聚焦模块为空,
            // 则直接退出!
            RobotLog.LOG.error(MessageFormat.format(
                "机器人 {0} 第一个模块为空",
                this._userName
            ));
            return;
        }

        ROBOT_COUNTER.incrementAndGet();

        // 不管怎么说,
        // 先尝试给第一个模块发送第一条指令! 即,
        // 让第一个模块发送 CG 消息...
        this._currFocusModule.test(this, ModuleReadyCmd.OBJ);
        // 消化所有消息
        this.digestAllMsg();
    }

    /**
     * 消化所有消息
     * 
     */
    private void digestAllMsg() {
        // 是否有错?
        boolean hasErr = false;

        try {
            while (true) {
                // 获取消息对象, 
                // 但是最多只等待 20 秒!
                // 如果超过这个时间, 
                // 则返回空值...
                Object msgObj = this._msgQ.poll();
    
                if (msgObj == null) {
                    // 如果消息对象为空,
                    // 则直接退出!
                    break;
                }

                if (this._currFocusModule == null) {
                    // 如果当前聚焦模块为空, 
                    // 则直接退出!
                    RobotLog.LOG.error(MessageFormat.format(
                        "机器人 {0} 当前模块为空, 机器人 {0} 已经完成此次测试使命, 即将断开连接...",
                        this._userName
                    ));
                    hasErr = true;
                    break;
                }

                // 运行策略对象
                this._currFocusModule.test(
                    this, msgObj
                );
            }
        } catch (Exception ex) {
            // 记录错误日志
            RobotLog.LOG.error(ex.getMessage(), ex);
            hasErr = true;
        }

        if (hasErr) {
            // 如果出错就断开连接
            this.disconnect();
        }
    }

    /**
     * 连接到游戏服
     * 
     */
    public void connectToGameServer() {
        // 创建客户端引导程序
        Bootstrap b = new Bootstrap();

        b.group(NETTY_WORK_GROUP);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                    new MsgDecoder(),
                    new MsgEncoder(),
                    new MyChannelHandler(Robot.this)
                );
            }
        });

        try {
            // 连接服务器后获取信道
            Channel ch = b.connect(new InetSocketAddress(
                this._gameServerIpAddr,
                this._gameServerPort
            )).sync().channel();
            // 设置信道
            this._ch = ch;
        } catch (Exception ex) {
            // 记录并抛出异常
            RobotLog.LOG.error(ex.getMessage(), ex);
            this.disconnect();
        }
    }

    /**
     * 断开连接
     * 
     */
    public void disconnect() {
        if (this._ch != null) {
            this._ch.close();
        }

        ROBOT_COUNTER.decrementAndGet();
    }

    /**
     * 给游戏服务器发送消息
     * 
     * @param msgObj
     * 
     */
    public void sendMsg(Object msgObj) {
        if (msgObj != null &&
            this._ch != null) {
            this._ch.writeAndFlush(msgObj);
        }
    }

    /**
     * 处理消息
     *
     * @param msgObj
     *
     */
    public void processMsg(Object msgObj) {
        if (msgObj != null) {
            this._msgQ.offer(msgObj);
            this.digestAllMsg();
        }
    }
}
