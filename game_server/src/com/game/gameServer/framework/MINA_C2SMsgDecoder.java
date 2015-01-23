package com.game.gameServer.framework;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 客户端消息 -&gt; 服务端消息
 * 
 * @author hjj2017
 * @since 2014/8/11
 * 
 */
class MINA_C2SMsgDecoder extends ProtocolDecoderAdapter {	
	/**
	 * 类参数构造器
	 * 
	 */
	public MINA_C2SMsgDecoder() {
	}
	
	@Override
	public void decode(IoSession sessionObj, IoBuffer buff, ProtocolDecoderOutput output) {
		if (buff == null || 
			output == null) {
			return;
		}
	}
}
