package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 抽象消息
 * 
 * @author hjj2017
 * @since 2015/3/14
 * 
 */
public abstract class AbstractMsgObj extends AbstractMsgField {
	@Override
	public void readBuff(IoBuffer buff) {
		if (buff == null || 
			buff.remaining() < 0) {
			// 如果数据流为空, 
			// 则直接退出!
			return;
		}

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

		// 创建帮助者对象
		IWriteHelper helper = WriteHelperMaker.make(this.getClass());
		
		if (helper != null) {
			// 如果帮助器不为空, 
			// 则写出数据...
			helper.writeBuff(this, buff);
		}
	}
}
