package com.game.gameServer.framework.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.game.gameServer.msg.AbstractGCMsgObj;

/**
 * 服务端消息 -&gt; 客户端消息
 * 
 * @author hjj2017
 * @since 2015/3/28
 *
 */
class GCMsgEncoder extends ProtocolEncoderAdapter {
	/** GC 消息默认容量 */
	private static final int GCMSG_DEFAULT_CAPCITY = 64;
	
	/**
	 * 类默认构造器
	 * 
	 */
	public GCMsgEncoder() {
	}

	@Override
	public void encode(IoSession sessionObj, Object obj, ProtocolEncoderOutput output) {
		if (obj == null || 
			output == null) {
			return;
		}

		// 创建 IoBuff 对象
		IoBuffer buff = IoBuffer.allocate(GCMSG_DEFAULT_CAPCITY);
		// 设置为自动扩展
		buff.setAutoExpand(true);

		// 获取消息对象
		AbstractGCMsgObj msgObj = (AbstractGCMsgObj)obj;
		// 令消息写出数据
		msgObj.writeBuff(buff);
		// 向下处理
		output.write(msgObj);
	}
}
