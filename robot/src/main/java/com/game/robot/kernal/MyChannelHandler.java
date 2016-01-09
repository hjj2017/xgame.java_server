package com.game.robot.kernal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;

import com.game.bizModule.login.msg.CGLogin;
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
	private Robot _robotObj;

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
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 创建 JSON 对象
        JSONObject jsonObj = new JSONObject();

        // 创建登陆协议文本
        jsonObj.put("protocol", "dbUser");
        jsonObj.put("userName", this._robotObj._userName);
        jsonObj.put("password", this._robotObj._userPass);

		ctx.writeAndFlush(new CGLogin(
            this._robotObj._userName,
            jsonObj.toString()
        ));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) {
		if (ctx == null ||
			obj == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 将消息对象添加到队列
		this._robotObj._msgQ.offer(obj);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable err) {
		// 记录错误日志
		MsgLog.LOG.error(err.getMessage(), err);
		// 令玩家断开连接
		ctx.disconnect();
		ctx.close();
	}
}
