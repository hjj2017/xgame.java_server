package com.game.gameServer.framework.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.game.gameServer.msg.AbstractGCMsgObj;
import com.game.part.msg.IoBuffUtil;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 服务端消息 -&gt; 客户端消息
 * 
 * @author hjj2017
 * @since 2015/3/28
 *
 */
public class GCMsgEncoder extends ProtocolEncoderAdapter {
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

		// 是否为消息对象?
		boolean isMsgObj = (obj instanceof AbstractMsgObj);

		if (isMsgObj == false) {
			// 如果不是消息对象,
			// 则直接退出!
			return;
		}

		// 创建 IoBuff 对象
		IoBuffer buff = IoBuffer.allocate(GCMSG_DEFAULT_CAPCITY);
		// 设置为自动扩展
		buff.setAutoExpand(true);

		// 实现写出消息长度 = 0,
		// 主要目的是占位!
		IoBuffUtil.writeShort((short)0, buff);

		// 获取消息对象
		AbstractMsgObj msgObj = (AbstractMsgObj)obj;
		// 令消息写出数据
		msgObj.writeBuff(buff);

		// 写出消息的实际长度
		buff.position(0);
		IoBuffUtil.writeShort((short)buff.remaining(), buff);
		buff.position(0);

		// 向下处理
		output.write(buff);
	}
}
