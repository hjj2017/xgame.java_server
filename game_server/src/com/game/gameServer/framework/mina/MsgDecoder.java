package com.game.gameServer.framework.mina;

import java.text.MessageFormat;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.IoBuffUtil;
import com.game.part.msg.MsgServ;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 消息解码器
 * 
 * @author hjj2017
 * @since 2014/8/11
 * 
 */
public class MsgDecoder extends ProtocolDecoderAdapter {
	@Override
	public void decode(IoSession sessionObj, IoBuffer buff, ProtocolDecoderOutput output) {
		if (buff == null || 
			output == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取原始位置
		final int oldPos = buff.position();

		// 首先, 跳过消息长度
		buff.skip(2);
		// 获取消息序列化 Id
		short msgSerialUId = IoBuffUtil.readShort(buff);
		//
		// 获取消息对象, 注意:
		// CG 消息和 GC 消息,
		// 都是继承自 AbstractMsgObj 类
		AbstractMsgObj msgObj = MsgServ.OBJ.newMsgObj(msgSerialUId);

		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error(MessageFormat.format(
				"无法取得消息对象, msgSerialUId = {0}", 
				String.valueOf(msgSerialUId)
			));
			return;
		}

		// 回转到原始位置
		buff.position(oldPos);
		// 令消息读取数据
		msgObj.readBuff(buff);

		// 向下处理
		output.write(msgObj);
	}
}
