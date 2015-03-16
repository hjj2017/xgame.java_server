package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.IoBuffUtil;

/**
 * 抽象消息
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
public abstract class AbstractMsgObj extends AbstractMsgField {
	/**
	 * 获取消息类型定义, 也就是消息 Id
	 * 
	 * @return 
	 * 
	 */
	public abstract short getMsgTypeDef();

	@Override
	public void readBuff(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 0) {
			// 如果数据流为空, 
			// 则直接退出!
			return;
		}

		// 先读掉开头的 msgTypeDef
		IoBuffUtil.readShort(buff);
		// 创建帮助者对象
		IReadHelper helper = ReadHelperMaker.make(this.getClass());

		if (helper != null) {
			// 如果帮助者不为空, 
			// 则读取数据...
			helper.readBuff(this, buff);
		}
	}

	@Override
	public void writeBuff(IoBuffer buff) {
		if (buff == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 先写出 msgTypeDef
		IoBuffUtil.writeShort(getMsgTypeDef(), buff);
		// 创建帮助者对象
		IWriteHelper helper = WriteHelperMaker.make(this.getClass());
		
		if (helper != null) {
			// 如果帮助器不为空, 
			// 则写出数据...
			helper.writeBuff(this, buff);
		}
	}

	/**
	 * 执行自身
	 * 
	 */
	public abstract void exec();
}
