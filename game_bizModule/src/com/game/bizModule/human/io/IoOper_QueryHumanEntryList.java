package com.game.bizModule.human.io;

import java.util.List;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.bizModule.human.msg.GGQueryHumanEntryListOk;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.part.dao.CommDao;

/**
 * 查询玩家角色入口列表
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class IoOper_QueryHumanEntryList extends AbstractLoginIoOper {
    /** 玩家对象 */
    public Player _p = null;

    @Override
    public long getBindUId() {
        return 0;
    }

    @Override
    public boolean doIo() {
        // 获取玩家角色入口列表
        List<HumanEntryEntity> heel = CommDao.OBJ.getResultList(
            HumanEntryEntity.class,
            "entity._platformUId = :platformUId"
        );

        // 创建 GG 消息
        GGQueryHumanEntryListOk ggMSG = new GGQueryHumanEntryListOk();
        ggMSG._p = this._p;
        ggMSG._heel = heel;
        // 消息分派
        this.msgDispatch(ggMSG);

        return true;
    }
}
