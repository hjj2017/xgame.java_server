package com.game.gameServer.framework;

import java.text.MessageFormat;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.game.gameServer.msg.AbstractCGMsgObj;
import com.game.part.msg.MsgServ;

/**
 * 客户端消息 -&gt; 服务端消息
 * 
 * @author hjj2017
 * @since 2014/8/11
 * 
 */
class MINA_CGMsgDecoder extends ProtocolDecoderAdapter {	
	/**
	 * 类参数构造器
	 * 
	 */
	public MINA_CGMsgDecoder() {
	}
	
	@Override
	public void decode(IoSession sessionObj, IoBuffer buff, ProtocolDecoderOutput output) {
		if (buff == null || 
			output == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取消息定义
		short msgTypeDef = (short)0;
		// 获取消息对象
		AbstractCGMsgObj<?> msgObj = MsgServ.OBJ.getMsgObj(msgTypeDef);

		if (msgObj == null) {
			// 如果消息对象为空, 
			// 则直接退出!
			FrameworkLog.LOG.error(MessageFormat.format(
				"无法取得消息对象, msgTypeDef = {0}", 
				String.valueOf(msgTypeDef)
			));
			return;
		}

		// 令消息读取数据
		msgObj.read(buff);
		// 向下处理
		output.write(msgObj);
	}
}
