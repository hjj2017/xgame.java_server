package com.game.gameServer.msg;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 抽象的 CG 的消息
 * 
 * @author hjj2019
 * @since 2015/01/25
 *
 */
public abstract class AbstractGGMsgObj<THandler extends AbstractGGMsgHandler<?>> extends AbstractExecutableMsgObj {
	/** 版本修订, 主要用于消息加密 */
	public int _revision;
	/** Md5 字符串, 主要用于消息加密 */
	public String _md5;
	/** 处理器对象 */
	private THandler _msgHandler = null;

	@Override
	public void readBuff(IoBuffer buff) {
		return;
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		return;
	}

	@Override
	public final void exec() {
		@SuppressWarnings("unchecked")
		AbstractGGMsgHandler<AbstractGGMsgObj<?>>
			handler = (AbstractGGMsgHandler<AbstractGGMsgObj<?>>)this.getSelfHandler();

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
	public final THandler getSelfHandler() {
		if (this._msgHandler == null) {
			// 如果处理器对象为空,
			// 则新建处理器!
			this._msgHandler = this.newHandlerObj();
		}

		return this._msgHandler;
	}

	/**
	 * 创建新的处理器对象
	 *
	 * @return
	 *
	 */
	protected abstract THandler newHandlerObj();
}
