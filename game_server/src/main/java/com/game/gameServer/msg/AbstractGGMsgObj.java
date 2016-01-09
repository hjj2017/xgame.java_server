package com.game.gameServer.msg;

import java.nio.ByteBuffer;
import java.text.MessageFormat;

import com.game.part.msg.MsgError;

/**
 * 抽象的 CG 的消息
 * 
 * @author hjj2019
 * @since 2015/01/25
 *
 */
public abstract class AbstractGGMsgObj extends AbstractExecutableMsgObj {
	/** 处理器对象 */
	private AbstractGGMsgHandler<AbstractGGMsgObj> _h = null;

	@Override
	public void readBuff(ByteBuffer buff) {
		// 内部消息无需实现该函数
		return;
	}

	@Override
	public void writeBuff(ByteBuffer buff) {
		// 内部消息无需实现该函数
		return;
	}

	@Override
	public final void exec() {
		// 获取消息处理器
		AbstractGGMsgHandler<AbstractGGMsgObj> hObj = this.getSelfHandler();

		if (hObj != null) {
			// 处理当前消息对象
			hObj.handle(this);
		}
	}

	/**
	 * 获取消息处理器
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public final <TGGMsgObj extends AbstractGGMsgObj> AbstractGGMsgHandler<TGGMsgObj> getSelfHandler() {
		if (this._h == null) {
			// 如果处理器对象为空,
			// 则新建处理器!
			this._h = this.newHandlerObj();

			if (this._h == null) {
				// 如果消息处理器为空,
				// 则抛出异常!
				throw new MsgError(MessageFormat.format(
					"{0} 消息处理器为空值",
					this.getClass().getName()
				));
			}
		}

		return (AbstractGGMsgHandler<TGGMsgObj>)this._h;
	}

	/**
	 * 创建新的处理器对象
	 *
	 * @return
	 *
	 */
	protected abstract <TGGMsgObj extends AbstractGGMsgObj> AbstractGGMsgHandler<TGGMsgObj> newHandlerObj();
}
