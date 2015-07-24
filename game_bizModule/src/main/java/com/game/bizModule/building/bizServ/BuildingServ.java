package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.entity.BuildingEntity;
import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;

/**
 * 建筑业务服务
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
public class BuildingServ extends AbstractBizServ implements IHumanEventListen {
    /** 单例对象 */
    public static final BuildingServ OBJ = new BuildingServ();

    /**
     * 类默认构造器
     *
     */
    private BuildingServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        HumanEvent.OBJ.regEventListen(this);
    }

    @Override
    public void onLoadDb(Player p, Human h) {
        if (p == null ||
            h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取管理器对象
        BuildingManager mngrObj = h.getPropValOrCreate(BuildingManager.class);
        // 获取建筑实体
        BuildingEntity entity = CommDao.OBJ.getSingleResult(
            BuildingEntity.class,
            "entity._humanUId = " + h._humanUId
        );

        if (entity != null) {
            // 从实体中加载数据
            mngrObj.fromEntity(entity);
        }
    }
}
