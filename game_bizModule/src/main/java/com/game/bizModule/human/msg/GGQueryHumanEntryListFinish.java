package com.game.bizModule.human.msg;

import java.util.List;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.bizModule.human.handler.Handler_GGQueryHumanEntryListFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.gameServer.msg.AbstractGGMsgObj;
import com.game.gameServer.msg.MsgTypeEnum;

/**
 * 查询玩家角色列表完成
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class GGQueryHumanEntryListFinish extends AbstractGGMsgObj {
    /** 玩家对象 */
    public Player _p = null;
    /** 服务器名称 */
    public String _serverName = null;
    /** 玩家角色入口实体列表 */
    public List<HumanEntryEntity> _heel = null;

    @Override
    protected AbstractGGMsgHandler<GGQueryHumanEntryListFinish> newHandlerObj() {
        return new Handler_GGQueryHumanEntryListFinish();
    }

    @Override
    public MsgTypeEnum getMsgType() {
        return MsgTypeEnum.login;
    }
}
