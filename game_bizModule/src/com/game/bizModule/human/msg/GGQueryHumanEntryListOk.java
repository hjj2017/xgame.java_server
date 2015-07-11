package com.game.bizModule.human.msg;

import java.util.List;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.bizModule.human.handler.Handler_GGQueryHumanEntryListOk;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractGGMsgObj;

/**
 * 查询玩家角色列表完成
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class GGQueryHumanEntryListOk extends AbstractGGMsgObj<Handler_GGQueryHumanEntryListOk> {
    /** 玩家对象 */
    public Player _p = null;
    /** 玩家角色入口实体列表 */
    public List<HumanEntryEntity> _heel = null;

    @Override
    protected Handler_GGQueryHumanEntryListOk newHandlerObj() {
        return new Handler_GGQueryHumanEntryListOk();
    }
}
