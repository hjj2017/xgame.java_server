package com.game.robot.kernal;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.netty.channel.Channel;

import com.game.robot.RobotLog;

/**
 * 机器人类
 * 
 * @author hjj2019
 * @since 2015/5/14
 * 
 */
public final class Robot {
    /** 游戏服务器名称 */
    public String _gameServerName = "S01";
    /** 用户名称 */
    public String _userName = "";
    /** 用户密码 */
    public String _userPass = "";
    /** 玩家角色 UId */
    public long _humanUId = -1L;
    /** 玩家角色名称 */
    public String _humanName = "";
    /** 最后活动时间 */
    public long _lastActiveTime = -1L;

    /** 运行策略 */
    private FocusModule _currFocusModule = null;
    /** CG 消息队列 */
    private final ConcurrentLinkedQueue<? super Object> _cgMsgQ = new ConcurrentLinkedQueue<>();
    /** GC 消息队列 */
    private final ConcurrentLinkedQueue<? super Object> _gcMsgQ = new ConcurrentLinkedQueue<>();
    /** 信道对象 */
    private Channel _ch = null;
    /** 数据字典 */
    public final Map<Object, Object> _dataMap = new ConcurrentHashMap<>();

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
        // 结束标志
        boolean overFlag = false;

        try {
            while (true) {
                // 获取消息对象, 
                // 但是最多只等待 20 秒!
                // 如果超过这个时间, 
                // 则返回空值...
                Object msgObj = this._gcMsgQ.poll();
    
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
                    overFlag = true;
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
            overFlag = true;
        }

        if (overFlag) {
            // 结束机器人
            this.disconnectAndQuit();
        }
    }

    /**
     * 连接到游戏服
     * 
     */
    public void connectToGameServer() {
        // 连接服务器后获取信道
        Channel ch = ConnectToServer.OBJ.connectAndGetChannel();

        if (ch == null) {
            // 如果信道对象为空,
            // 则直接退出!
            RobotLog.LOG.error(MessageFormat.format(
                "玩家 {0} 连接服务器失败",
                this._userName
            ));

            this.disconnectAndQuit();
            return;
        }

        // 添加一个处理器
        ch.pipeline().addLast(new MyChannelHandler(Robot.this));
        // 设置信道
        this._ch = ch;
    }

    /**
     * 断开连接
     * 
     */
    public void disconnect() {
        if (this._ch != null) {
            try {
                this._ch.disconnect().await();
            } catch (Exception ex) {
                // 记录并抛出异常
                RobotLog.LOG.error(
                    ex.getMessage(), ex
                );
            }
        }

        // 清除消息队列
        this._cgMsgQ.clear();
        this._gcMsgQ.clear();
        // 清除数据
        this._dataMap.clear();
    }

    /**
     * 断开连接并退出测试
     *
     */
    public void disconnectAndQuit() {
        this.disconnect();
        AllRobotManager.OBJ.removeRobot(this._userName);
    }

    /**
     * 给游戏服务器发送消息
     *
     * @param cgMsgObj
     */
    public void sendCGMsg(Object cgMsgObj) {
        this.sendCGMsg(cgMsgObj, -1000L);
    }

    /**
     * 给游戏服务器发送消息
     *
     * @param objCGMsg
     * @param afterMillis
     *
     */
    public void sendCGMsg(Object objCGMsg, long afterMillis) {
        if (objCGMsg == null) {
            return;
        }

        // 获取当前时间
        long nowTime = System.currentTimeMillis();
        // CG　消息执行时间点
        long cgMsgExecTimePoint = nowTime + afterMillis;

        // 记录日志信息
        RobotLog.LOG.info(objCGMsg.getClass().getSimpleName());
        // 如果消息对象和会话对象都不为空,
        this._cgMsgQ.offer(new DelayCGMsg(cgMsgExecTimePoint, objCGMsg));

        // 修改最后活动时间
        this._lastActiveTime = nowTime;
    }

    /**
     * 实际发送 CG 消息
     *
     * @param nowTime
     */
    void realSendCGMsg(long nowTime) {
        // 获取迭代器
        Iterator<?> it = this._cgMsgQ.iterator();

        for (; it.hasNext(); ) {
            // 获取 CG 消息
            Object cgMsgObj = it.next();

            if (!(cgMsgObj instanceof DelayCGMsg)) {
                it.remove();
                continue;
            }

            // 转型为 DelayCGMsg
            DelayCGMsg delayCGMsg = (DelayCGMsg)cgMsgObj;
            // 获取执行时间点
            long execTimePoint = delayCGMsg._execTimePoint;

            if (execTimePoint > nowTime) {
                // 如果还没到时间,
                // 则直接跳过!
                continue;
            }

            try {
                // 写出 CG 消息
                this._ch.writeAndFlush(delayCGMsg._cgMsg).await();
            } catch (Exception ex) {
                // 记录并抛出异常
                RobotLog.LOG.error(
                    ex.getMessage(), ex
                );
            }

            it.remove();
        }
    }

    /**
     * 处理 GC 消息
     *
     * @param objGCMsg
     *
     */
    public void processGCMsg(Object objGCMsg) {
        if (objGCMsg != null) {
            this._gcMsgQ.offer(objGCMsg);
            this.digestAllMsg();
        }
    }
}
