package com.game.gameServer.msg;

/**
 * 消息序列化 UId
 * 
 * @author hjj2017
 * @since 2015/01/25
 * 
 */
public final class SpecialMsgSerialUId {
	/** 由 Flash Socket 发送的 Policy Request 请求协议 "&lt;policy" 中第 3, 4 两个字节 "ol" 的16进制表示 : 28524 */
	public static final short CG_FLASH_POLICY = 0x6f6c;
	/** 腾讯网关消息 */
	public static final short CG_QQ_TGW = 0x775f;
	/** 腾讯网关消息 */
	public static final short GC_QQ_TGW = 51;
	/** 会话开启 */
	public static final short SESSION_OPENED = 1;
	/** 会话关闭 */
	public static final short SESSION_CLOSED = 44;

	/**
	 * 类默认构造器
	 * 
	 */
	private SpecialMsgSerialUId() {
	}
}
