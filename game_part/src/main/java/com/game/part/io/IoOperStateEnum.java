package com.game.part.io;

/**
 * 异步操作状态枚举
 * 
 * @author haijiang
 *
 */
enum IoOperStateEnum {
	/** 初始化完成 */
	initOk(0, "initOk"),
	/** IO 执行完成 */
	ioFinishOk(1, "ioFinishOk"),
	/** 退出 */
	exit(-1, "exit"), 
;

	/** 整型数值 */
	private int _intVal;
	/** 字符串值 */
	private String _strVal;

	/**
	 * 枚举参数构造器
	 * 
	 * @param intVal
	 * @param strVal
	 */
	IoOperStateEnum(int intVal, String strVal) {
		this._intVal = intVal;
		this._strVal = strVal;
	}

	/**
	 * 获取 int 数值
	 * 
	 * @return
	 */
	public int getIntVal() {
		return this._intVal;
	}

	/**
	 * 获取字符串值
	 * 
	 * @return
	 */
	public String getStrVal() {
		return this._strVal;
	}
}
