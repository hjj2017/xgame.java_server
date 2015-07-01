package com.game.gameServer.framework;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 消息解码器工厂类
 * 
 * @author hjj2017
 * @since 2014/8/11
 * 
 */
public class MINA_MsgCodecFactory implements ProtocolCodecFactory {
	/**
	 * 类参数构造器
	 *
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
