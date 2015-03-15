package com.game.part.msg.type;

import org.apache.mina.core.buffer.IoBuffer;

import com.game.part.msg.MsgError;
import com.game.part.msg.MsgLog;

/**
 * 抽象的消息字段
 * 
 * @author hjj2017
 * @since 2015/3/15
 * 
 */
public abstract class AbstractMsgField {
	/**
	 * 从 IoBuff 中读取数据
	 * 
	 * @param buff
	 * 
	 */
	public abstract void readBuff(IoBuffer buff);

	/**
	 * 写出数据到 IoBuff 中
	 * 
	 * @param buff
	 * 
	 */
	public abstract void writeBuff(IoBuffer buff);

	/**
	 * objVal 不能为空, 但如果真为空值, 则自动创建
	 * 
	 * @param objVal
	 * @param cell
	 * @param xlsxFileName
	 * @return
	 * 
	 */
	public static<T extends AbstractMsgField> T ifNullThenCreate(T objVal, Class<T> objClazz) {
		try {
			if (objVal == null) {
				// 创建对象
				objVal = objClazz.newInstance();
			}

			return objVal;
		} catch (Exception ex) {
			// 抛出异常
			MsgLog.LOG.error(ex.getMessage(), ex);
			throw new MsgError(ex);
		}
	}
}
