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
    /** @see SpecialMsgSerialUId#PLAYER_CONNECTED */
    public static final short PLAYER_CONNECTED = SpecialMsgSerialUId.PLAYER_CONNECTED;
    /** @see SpecialMsgSerialUId#PLAYER_DISCONNECTED */
    public static final short PLAYER_DISCONNECTED = SpecialMsgSerialUId.PLAYER_DISCONNECTED;
    /** 消息盒子 */
    public static final short GC_MSG_BOX = 100;

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
    /** 随机角色名称 CG 消息 */
    public static final short CG_RAND_HUMAN_NAME = ++MSG_BASE_HUMAN;
    /** 随机角色名称 GC 消息 */
    public static final short GC_RAND_HUMAN_NAME = ++MSG_BASE_HUMAN;
    /** 创建角色 CG 消息 */
    public static final short CG_CREATE_HUMAN = ++MSG_BASE_HUMAN;
    /** 创建角色 GC 消息 */
    public static final short GC_CREATE_HUMAN = ++MSG_BASE_HUMAN;
    /** 进入角色 CG 消息 */
    public static final short CG_ENTER_HUMAN = ++MSG_BASE_HUMAN;
    /** 进入角色 GC 消息 */
    public static final short GC_ENTER_HUMAN = ++MSG_BASE_HUMAN;

// 建筑
///////////////////////////////////////////////////////////////////////

    /** 消息基址 */
    private static short MSG_BASE_BUILDING = 1200;
    /** 建筑升级 */
    public static final short CG_BUILDING_LEVEL_UP = ++MSG_BASE_BUILDING;

// Cd
///////////////////////////////////////////////////////////////////////

    /** 消息基址 */
    private static short MSG_BASE_CD = 1300;
    /** 列表所有计时器 CG 消息 */
    public static final short CG_LIST_ALL_TIMER = ++MSG_BASE_CD;
    /** 列表所有计时器 GC 消息 */
    public static final short GC_LIST_ALL_TIMER = ++MSG_BASE_CD;
    /** 列表已修改的计时器 GC 消息 */
    public static final short GC_LIST_CHANGED_TIMER = ++MSG_BASE_CD;

// 聊天
///////////////////////////////////////////////////////////////////////

    /** 消息基址 */
    private static short MSG_BASE_CHAT = 1400;
    /** 世界聊天 CG 消息 */
    public static final short CG_WORLD_CHAT = ++MSG_BASE_CHAT;
    /** 通用的聊天 GC 消息 */
    public static final short GC_COMM_CHAT = ++MSG_BASE_CHAT;

    /**
     * 类默认构造器
     *
     */
    private AllMsgSerialUId() {
    }
}
