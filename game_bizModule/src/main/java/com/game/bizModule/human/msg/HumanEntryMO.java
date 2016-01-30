package com.game.bizModule.human.msg;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.part.msg.type.AbstractMsgObj;
import com.game.part.msg.type.MsgInt;
import com.game.part.msg.type.MsgLong;
import com.game.part.msg.type.MsgStr;
import com.game.part.util.NullUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 玩家角色入口
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
public class HumanEntryMO extends AbstractMsgObj {
    /** 玩家角色 UId */
    public MsgLong _humanUId;
    /** 玩家角色 UId 字符串 */
    public MsgStr _humanUIdStr;
    /** 角色等级 */
    public MsgInt _humanLevel;
    /** 角色名称 */
    public MsgStr _humanName;

    /**
     * 从数据实体中创建消息对象
     *
     * @param hee
     * @return
     *
     */
    public static HumanEntryMO fromEntity(HumanEntryEntity hee) {
        if (hee == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 创建消息对象
        HumanEntryMO mo = new HumanEntryMO();
        // 设置相关属性
        mo._humanUId = new MsgLong(hee._humanUId);
        mo._humanUIdStr = new MsgStr(String.valueOf(hee._humanUId));
        mo._humanLevel = new MsgInt(NullUtil.optVal(hee._humanLevel, 0));
        mo._humanName = new MsgStr(hee._humanName);

        return mo;
    }

    /**
     * 从数据实体列表中创建消息对象列表
     *
     * @param heel
     * @return
     *
     */
    public static List<HumanEntryMO> fromEntityList(List<HumanEntryEntity> heel) {
        if (heel == null ||
            heel.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return Collections.emptyList();
        }

        return heel.stream().map(hee -> fromEntity(hee))
            .collect(Collectors.toList());
    }
}
