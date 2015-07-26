package com.game.bizModule.cd.msg;

import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.MsgBool;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgLong;

/**
 * Cd 计时器消息对象
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class CdTimerMO extends AbstractMsgObj {
    /** Cd 类型 */
    public MsgInt _cdTypeInt;
    /** 开始时间 */
    public MsgLong _startTime;
    /** 结束时间 */
    public MsgLong _endTime;
    /** 是否已开启 */
    public MsgBool _opened;
}
