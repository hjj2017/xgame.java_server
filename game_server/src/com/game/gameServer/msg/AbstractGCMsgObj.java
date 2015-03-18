package com.game.gameServer.msg;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;
import com.game.part.msg.type.AbstractMsgObj;

/**
 * 抽象的 GC 的消息
 * 
 * @author hjj2019
 * @since 2015/01/25
 *
 */
public abstract class AbstractGCMsgObj extends AbstractMsgObj {
	/**
	 * 获取消息序列化 Id
	 * 
	 * @return 
	 * 
	 */
	public abstract short getSerialUId();

	@Override
	public void readBuff(IoBuffer buff) {
		if (buff != null) {
			// 读掉开头的序列化 Id
			IoBuffUtil.readShort(buff);
			// 执行父类的读取罗辑
			super.readBuff(buff);
		}
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		if (buff != null) {
			// 将序列化 Id 写在开头
			IoBuffUtil.writeShort(getSerialUId(), buff);
			// 执行父类的写出罗辑
			super.writeBuff(buff);
		}
	}
}
