package com.game.bizModule.human.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.bizModule.human.msg.GGQueryHumanEntryListFinish;
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
    /** 服务器名称 */
    public String _serverName = null;

    @Override
    public long getBindUId() {
        return AbstractLoginIoOper.getBindUIdByPlayer(this._p);
    }

    @Override
    public boolean doIo() {
        // 创建参数字典
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(
            "platformUId",
            this._p._platformUId
        );

        // 获取玩家角色入口列表
        List<HumanEntryEntity> heel = CommDao.OBJ.getResultList(
            HumanEntryEntity.class,
            "entity._platformUId = :platformUId",
            paramMap
        );

        // 创建 GG 消息
        GGQueryHumanEntryListFinish ggMSG = new GGQueryHumanEntryListFinish();
        ggMSG._p = this._p;
        ggMSG._serverName = this._serverName;
        ggMSG._heel = heel;
        // 消息分派
        this.msgDispatch(ggMSG);

        return true;
    }
}
