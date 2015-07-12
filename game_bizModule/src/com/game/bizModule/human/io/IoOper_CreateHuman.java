package com.game.bizModule.human.io;

import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.msg.GGCreateHumanFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.part.dao.CommDao;

/**
 * 异步方式创建角色
 *
 * @author hjj2017
 * @since 2015/7/22
 *
 */
public class IoOper_CreateHuman extends AbstractLoginIoOper {
    /** 玩家对象 */
    public Player _p;
    /** 服务器名称 */
    public String _serverName;
    /** 角色名称 */
    public String _humanName;
    /** 模版 Id */
    public int _tmplId;

    @Override
    public long getBindUId() {
        return AbstractLoginIoOper.getBindUIdByPlayer(this._p);
    }

    @Override
    public boolean doIo() {
        // 创建角色实体
        HumanEntity he = new HumanEntity();
        he._humanUId = 1001L;
        he._platformUId = this._p._platformUId;
        he._serverName = this._serverName;
        he._humanName = this._humanName;

        // 保存数据
        CommDao.OBJ.save(he);
        // 创建消息对象
        GGCreateHumanFinish ggMSG = new GGCreateHumanFinish();
        ggMSG._p = this._p;
        ggMSG._success = true;
        // 分派消息
        this.msgDispatch(ggMSG);

        return true;
    }
}
