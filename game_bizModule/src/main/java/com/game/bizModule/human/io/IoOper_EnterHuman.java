package com.game.bizModule.human.io;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.msg.GGCreateHumanFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.gameServer.io.AbstractPlayerOrSceneIoOper;
import com.game.part.dao.CommDao;

/**
 * 异步方式进入角色
 *
 * @author hjj2017
 * @since 2015/7/22
 *
 */
public class IoOper_EnterHuman extends AbstractPlayerOrSceneIoOper {
    /** 玩家 */
    public Player _p = null;
    /** 角色 UId */
    public long _humanUId;

    @Override
    public long getBindUId() {
        return this._humanUId;
    }

    @Override
    public boolean doIo() {
        // 获取角色实体
        final HumanEntity he = CommDao.OBJ.find(HumanEntity.class, this._humanUId);
        // 创建角色
        Human h = Human.create(
            this._p,
            he._humanUId,
            he._humanName,
            he._serverName
        );

        // 触发建角事件
        HumanServ.OBJ.fireLoadDbEvent(h);
        return true;
    }
}
