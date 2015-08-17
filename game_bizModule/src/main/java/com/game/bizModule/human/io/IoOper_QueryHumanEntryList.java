package com.game.bizModule.human.io;

import java.util.List;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.bizModule.human.msg.GGQueryHumanEntryListFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.part.dao.CommDao;
import com.game.part.util.StringUtil;

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
        if (this._p == null) {
            // 如果玩家对象为空,
            // 则直接退出!
            return false;
        }

        // 获取平台 UId 字符串
        final String platformUIdStr = this._p._platformUIdStr;
        // 获取玩家角色入口列表
        List<HumanEntryEntity> heel = CommDao.OBJ.getResultList(
            HumanEntryEntity.class,
            "entity._platformUIdStr = '" + StringUtil.addSlash(platformUIdStr) + "'"
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
