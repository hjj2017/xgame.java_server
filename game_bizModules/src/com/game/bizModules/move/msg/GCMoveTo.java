package com.game.bizModules.move.msg;

import com.game.gameServer.msg.AbstractGameMsg;

/**
 * 移动
 * 
 * @author hjj2017
 *
 */
public class GCMoveTo extends AbstractGameMsg {
	/** 消息类型 ID */
	private static final short MSG_TYPE_ID = 1003;

	/** 玩家 ID */
	private String _playerID;
	/** 目标位置 X */
	private int _x = -1;
	/** 目标位置 Y */
	private int _y = -1;

	/**
	 * 获取玩家 ID
	 * 
	 * @return
	 */
	public String getPlayerID() {
		return this._playerID;
	}

	/**
	 * 设置玩家 ID 
	 * 
	 * @param value
	 */
	public void setPlayerID(String value) {
		this._playerID = value;
	}

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
	public short getMsgTypeId() {
		return MSG_TYPE_ID;
	}
}
