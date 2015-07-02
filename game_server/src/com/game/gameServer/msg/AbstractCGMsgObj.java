package com.game.gameServer.msg;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 抽象的 CG 的消息
 * 
 * @author hjj2019
 * @since 2015/01/25
 *
 */
public abstract class AbstractCGMsgObj<THandler extends AbstractCGMsgHandler<?>> extends AbstractExecutableMsgObj {
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

	/**
	 * 是不是聊天消息?
	 * 注意 : 聊天消息会进入聊天线程执行!
	 *
	 * @return
	 */
	public boolean isChatMsg() {
		return false;
	}

	@Override
	public void exec() {
		@SuppressWarnings("unchecked")
		AbstractCGMsgHandler<AbstractCGMsgObj<?>> 
			handler = (AbstractCGMsgHandler<AbstractCGMsgObj<?>>)this.getSelfHandler();

		if (handler == null) {
			// 如果处理器为空, 
			// 则直接退出!
			return;
		}

		// 获取消息处理器
		handler.handle(this);
	}

	/**
	 * 获取消息处理器
	 * 
	 * @return
	 * 
	 */
	public abstract THandler getSelfHandler();
}
