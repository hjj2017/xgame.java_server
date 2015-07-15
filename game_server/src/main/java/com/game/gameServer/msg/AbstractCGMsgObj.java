package com.game.gameServer.msg;

import com.game.part.msg.IoBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * 抽象的 CG 的消息
 * 
 * @author hjj2019
 * @since 2015/01/25
 *
 */
public abstract class AbstractCGMsgObj<THandler extends AbstractCGMsgHandler<?>> extends AbstractExecutableMsgObj {
	/** 版本修订, 主要用于消息加密 */
	public int _revision;
	/** Md5 字符串, 主要用于消息加密 */
	public String _md5;

	/** 处理器对象 */
	private THandler _h = null;

	/**
	 * 获取消息序列化 Id
	 * 
	 * @return 
	 * 
	 */
	public abstract short getSerialUId();

	@Override
	public void readBuff(IoBuffer buff) {
		if (buff == null ||
			buff.remaining() < 10) {
			// 如果参数对象为空,
			// 则直接退出!
			// * 注意:
			// 10 = 消息长度 + SerialUId + Revision + Md5.StrLen
			//    = Short + Short + Int + Short
			//    = 2 + 2 + 4 + 2
			//    = 10
			//
			return;
		}

		// 首先, 跳过消息的前 4 个字节!
		buff.skip(4);
		// 前 2 个字节表示消息长度, 后 2 个字节表示消息的 SerialUId...
		// 消息长度只会在解码时用到,
		// 而 SerialUId 是由 #getSerialUId 方法提供的,
		// 在这里无需赋值!
		// 所以, 前 4 个字节可以跳过.

		// 读取版本修订
		this._revision = IoBuffUtil.readInt(buff);
		// 读取 Md5 字符串
		this._md5 = IoBuffUtil.readStr(buff);

		// 执行父类的读取罗辑
		super.readBuff(buff);
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		if (buff == null) {
			// 如果参数对象为空,
			// 则直接退出!
			return;
		}

		// 首先记录一下原始位置
		final int oldPos = buff.position();

		// 事先写出消息长度 = -1,
		// 主要目的是占位!
		IoBuffUtil.writeShort((short)-1, buff);
		// SerialUId
		IoBuffUtil.writeShort(this.getSerialUId(), buff);
		// 写出版本修订
		IoBuffUtil.writeInt(this._revision, buff);
		// 写出 Md5 字符串
		IoBuffUtil.writeStr(this._md5, buff);

		// 执行父类的写出罗辑
		super.writeBuff(buff);

		// 获取最新位置
		final int newPos = buff.position();
		// 测量一下长度,
		// 用最新位置减掉原始的位置
		short msgLen = (short)(newPos - oldPos);

		// 回转到原始位置,
		// 写出消息长度
		buff.position(oldPos);
		IoBuffUtil.writeShort(msgLen, buff);
		// 回转到最新位置,
		// 恢复现场
		buff.position(newPos);
	}

	@Override
	public final void exec() {
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
	public final THandler getSelfHandler() {
		if (this._h == null) {
			// 如果处理器对象为空,
			// 则新建处理器!
			this._h = this.newHandlerObj();
		}

		return this._h;
	}

	/**
	 * 创建新的处理器对象
	 *
	 * @return
	 *
	 */
	protected abstract THandler newHandlerObj();
}
