package com.game.robot.kernal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.game.part.msg.MsgLog;
import com.game.part.util.Assert;

/**
 * GC 消息处理器
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
class MyChannelHandler extends ChannelInboundHandlerAdapter {
    /** 机器人对象 */
    private final Robot _robotObj;

    /**
     * 类参数构造器
     * 
     * @param robotObj
     * 
     */
    MyChannelHandler(Robot robotObj) {
        // 断言参数不为空
        Assert.notNull(robotObj);
        // 设置机器人对象
        this._robotObj = robotObj;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        // 处理消息
        this._robotObj.processGCMsg(obj);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
        // 记录错误日志并断线
        MsgLog.LOG.error(err.getMessage(), err);
        this._robotObj.disconnect();
    }
}
