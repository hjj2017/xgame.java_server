package com.game.gameServer.msg.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.game.part.msg.type.AbstractMsgObj;

import java.nio.ByteOrder;

/**
 * 消息编码器
 * 
 * @author hjj2017
 * @since 2015/3/28
 *
 */
public class MsgEncoder extends ProtocolEncoderAdapter {
	/** 消息默认容量 */
	private static final int MSG_DEFAULT_CAPCITY = 256;

	@Override
	public void encode(IoSession sessionObj, Object obj, ProtocolEncoderOutput output) {
		if (obj == null || 
			output == null) {
			// 如果参数对象为空,
			// 则直接退出!
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
		IoBuffer buff = IoBuffer.allocate(MSG_DEFAULT_CAPCITY);
		buff.order(ByteOrder.LITTLE_ENDIAN);
		// 设置为自动扩展
		buff.setAutoExpand(true);
		buff.position(0);

		// 获取消息对象
		AbstractMsgObj msgObj = (AbstractMsgObj)obj;
		// 令消息写出数据
		msgObj.writeBuff(buff);
		buff.flip();

		// 向下处理
		output.write(buff);
	}
}
