package com.game.bizModule.human.io;

import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.bizServ.HumanNaming;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.msg.GGCreateHumanFinish;
import com.game.gameServer.framework.Player;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.part.dao.CommDao;
import com.game.part.util.StringUtil;

import java.text.MessageFormat;

/**
 * 异步方式创建角色
 *
 * @author hjj2017
 * @since 2015/7/22
 *
 */
public class IoOper_CreateHuman extends AbstractLoginIoOper {
    /** 玩家对象 */
    public Player _p = null;
    /** 角色 UId */
    public long _humanUId;
    /** 服务器名称 */
    public String _serverName;
    /** 角色名称 */
    public String _humanName;

    @Override
    public long getBindUId() {
        return AbstractLoginIoOper.getBindUIdByPlayer(
            this._p
        );
    }

    @Override
    public boolean doIo() {
        // 获取角色全名
        final String fullName = HumanNaming.OBJ.getFullName(
            this._serverName,
            this._humanName
        );

        // 事先获取旧数据
        HumanEntity oldEntity = CommDao.OBJ.getSingleResult(
            HumanEntity.class,
            "entity.fullName = '" + StringUtil.addSlash(fullName) + "'"
        );

        if (oldEntity != null) {
            // 如果旧实体不为空,
            // 则直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "角色全名 {0} 重复",
                fullName
            ));
            return false;
        }

        // 创建新数据
        HumanEntity newEntity = new HumanEntity();
        newEntity._humanUId = this._humanUId;
        newEntity._platformUIdStr = this._p._platformUIdStr;
        newEntity._fullName = fullName;
        newEntity._serverName = this._serverName;
        newEntity._humanName = this._humanName;

        // 保存数据
        CommDao.OBJ.save(newEntity);
        // 触发建角事件
        HumanEvent.OBJ.fireCreateHumanEvent(
            this._p,
            this._humanUId,
            this._serverName,
            this._humanName
        );

        // 创建消息对象
        GGCreateHumanFinish ggMSG = new GGCreateHumanFinish();
        ggMSG._p = this._p;
        ggMSG._success = true;
        // 分派消息
        this.msgDispatch(ggMSG);
        return true;
    }
}
