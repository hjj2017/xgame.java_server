package com.game.bizModules.move.msg;

import com.game.core.msg.BaseExternalMsg;

/**
 * 移动消息
 * 
 * @author hjj2017
 *
 */
public class CGMoveTo extends BaseExternalMsg {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1002;

	/** 目标位置 X */
	private int _x = -1;
	/** 目标位置 Y */
	private int _y = -1;

	/**
	 * 获取目标位置 X
	 * 
	 * @return
	 */
	public int getX() {
		return this._x;
	}

	/**
	 * 设置目标位置 X
	 * 
	 * @param value 
	 * 
	 */
	public void setX(int value) {
		this._x = value;
	}

	/**
	 * 获取目标位置 Y
	 * 
	 * @return
	 */
	public int getY() {
		return this._y;
	}

	/**
	 * 设置目标位置 Y
	 * 
	 * @param value 
	 * 
	 */
	public void setY(int value) {
		this._y = value;
	}

	@Override
	public short getMsgTypeID() {
		return MSG_TYPE_ID;
	}
}
