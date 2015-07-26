package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.entity.BuildingEntity;
import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.building.tmpl.BuildingTmpl;
import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;

import java.util.Arrays;

/**
 * 建筑业务服务
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
public class BuildingServ extends AbstractBizServ implements IHumanEventListen, IServ_DoLevelUp {
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
        BuildingManager mngrObj = new BuildingManager(h._humanUId);
        // 设置属性值
        p.putPropVal(
            BuildingManager.class,
            mngrObj
        );

        Arrays.asList(BuildingTypeEnum.values())
            .forEach(bt -> {
            // 获取建筑模版
            BuildingTmpl tmplObj = BuildingTmpl.getByBuildingType(bt);
            // 建筑开启所需角色等级
            final int openingNeedHumanLevel = tmplObj._openingNeedHumanLevel.getIntVal();

            if (h._humanLevel >= openingNeedHumanLevel) {
                // 如果角色等级 >= 所需等级,
                // 则默认为 1 级
                mngrObj.setLevel(bt, +1);
            }
        });

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
