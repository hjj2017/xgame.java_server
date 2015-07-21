package com.game.bizModule.human.io;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.bizServ.HumanServ;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.msg.GGCreateHumanFinish;
import com.game.gameServer.io.AbstractLoginIoOper;
import com.game.part.dao.CommDao;

import java.text.MessageFormat;

/**
 * 异步方式创建角色
 *
 * @author hjj2017
 * @since 2015/7/22
 *
 */
public class IoOper_CreateHuman extends AbstractLoginIoOper {
    /** 角色对象 */
    public Human _h = null;

    @Override
    public long getBindUId() {
        return AbstractLoginIoOper.getBindUIdByPlayer(
            this._h.getPlayer()
        );
    }

    @Override
    public boolean doIo() {
        // 获取角色实体
        final HumanEntity he = this._h.toEntity();

        HumanEntity oldEntity = CommDao.OBJ.getSingleResult(
            HumanEntity.class,
            "entity.fullName = " + he._fullName
        );

        if (oldEntity != null) {
            // 如果旧实体不为空,
            // 则直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "角色全名 {0} 重复",
                he._fullName
            ));
            return false;
        }

        // 保存数据
        CommDao.OBJ.save(he);
        // 触发建角事件
        HumanServ.OBJ.fireCreateHumanEvent(this._h);

        // 创建消息对象
        GGCreateHumanFinish ggMSG = new GGCreateHumanFinish();
        ggMSG._p = this._h.getPlayer();
        ggMSG._success = true;
        // 分派消息
        this.msgDispatch(ggMSG);

        return true;
    }
}
