package com.game.gameServer.framework;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 服务端消息 -&gt; 客户端消息
 * 
 * @author hjj2017
 *
 */
class MINA_GCMsgEncoder extends ProtocolEncoderAdapter {
	/**
	 * 类默认构造器
	 * 
	 */
	public MINA_GCMsgEncoder() {
	}

	@Override
	public void encode(IoSession sessionObj, Object obj, ProtocolEncoderOutput output) {
		if (obj == null || 
			output == null) {
			return;
		}
	}
}
