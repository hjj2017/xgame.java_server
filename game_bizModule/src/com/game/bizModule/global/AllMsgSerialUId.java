package com.game.bizModule.global;

import com.game.gameServer.msg.SpecialMsgSerialUId;

/**
 * 所有消息的 Id
 *
 * @author hjj2017
 * @since 2015/7/7
 *
 */
public final class AllMsgSerialUId {
// 继承特殊消息
///////////////////////////////////////////////////////////////////

    /** @see SpecialMsgSerialUId#CG_FLASH_POLICY */
    public static final short CG_FLASH_POLICY = SpecialMsgSerialUId.CG_FLASH_POLICY;
    /** @see SpecialMsgSerialUId#CG_QQ_TGW */
    public static final short CG_QQ_TGW = SpecialMsgSerialUId.CG_QQ_TGW;
    /** @see SpecialMsgSerialUId#GC_QQ_TGW */
    public static final short GC_QQ_TGW = SpecialMsgSerialUId.GC_QQ_TGW;
    /** @see SpecialMsgSerialUId#SESSION_OPENED */
    public static final short SESSION_OPENED = SpecialMsgSerialUId.SESSION_OPENED;
    /** @see SpecialMsgSerialUId#SESSION_CLOSED */
    public static final short SESSION_CLOSED = SpecialMsgSerialUId.SESSION_CLOSED;

// 登陆模
///////////////////////////////////////////////////////////////////

    /** 消息基址 */
    private static short MSG_BASE_LOGIN = 1000;
    /** 登陆 CG 消息 */
    public static final short CG_LOGIN = ++MSG_BASE_LOGIN;
    /** 登陆 GC 消息 */
    public static final short GC_LOGIN = ++MSG_BASE_LOGIN;

// 玩家角色
///////////////////////////////////////////////////////////////////

    /** 消息基址 */
    private static short MSG_BASE_HUMAN = 1100;
    /** 获取玩家角色列表 CG 消息 */
    public static final short CG_QUERY_HUMAN_ENTRY_LIST = ++MSG_BASE_HUMAN;
    /** 获取玩家角色列表 GC 消息 */
    public static final short GC_QUERY_HUMAN_ENTRY_LIST = ++MSG_BASE_HUMAN;

    /**
     * 类默认构造器
     *
     */
    private AllMsgSerialUId() {
    }
}
