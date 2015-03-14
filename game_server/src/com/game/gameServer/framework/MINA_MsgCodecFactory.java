package com.game.gameServer.framework;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.game.part.NullArgsError;

/**
 * 消息解码器工厂类
 * 
 * @author hjj2017
 * @since 2014/8/11
 * 
 */
class MINA_MsgCodecFactory implements ProtocolCodecFactory {
	/**
	 * 类参数构造器
	 * 
	 * @param serializer
	 * @throws NullArgsError if serializer == null 
	 * 
	 */
	public MINA_MsgCodecFactory() {
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession sessionObj) {
		return new MINA_CGMsgDecoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession sessionObj) {
		return new MINA_GCMsgEncoder();
	}
}
