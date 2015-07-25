package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.entity.BuildingEntity;
import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.human.AbstractHumanBelonging;
import com.game.part.util.NullUtil;

/**
 * 建筑管理器
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
public class BuildingManager extends AbstractHumanBelonging<BuildingEntity> {
    /** 主城等级 */
    public int _homeLevel = 0;
    /** 酒馆等级 */
    public int _pubLevel = 0;
    /** 铁匠铺等级 */
    public int _forgeLevel = 0;

    /**
     * 类参数构造器
     *
     * @param humanUId
     *
     */
    protected BuildingManager(long humanUId) {
        super(humanUId);
    }

    /**
     * 获取建筑等级
     *
     * @param bt
     * @return
     *
     */
    public int getLevel(BuildingTypeEnum bt) {
        if (bt == null) {
            return -1;
        }

        if (bt == BuildingTypeEnum.home) {
            return this._homeLevel;
        } else if (bt == BuildingTypeEnum.pub) {
            return this._pubLevel;
        } else if (bt == BuildingTypeEnum.forge) {
            return this._forgeLevel;
        } else {
            return -1;
        }
    }

    /**
     * 设置建筑等级
     *
     * @param bt
     * @param level
     *
     */
    void setLevel(BuildingTypeEnum bt, int level) {
        if (bt == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        if (bt == BuildingTypeEnum.home) {
            this._homeLevel = level;
        } else if (bt == BuildingTypeEnum.pub) {
            this._pubLevel = level;
        } else if (bt == BuildingTypeEnum.forge) {
            this._forgeLevel = level;
        } else {
            // 抛出异常
        }
    }

    /**
     * 增加建筑等级
     *
     * @param bt
     * @param deltaVal
     *
     */
    public void addLevel(BuildingTypeEnum bt, int deltaVal) {
        if (bt == null ||
            deltaVal <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        } else {
            // 重新设置建筑等级
            this.setLevel(bt, this.getLevel(bt) + deltaVal);
        }
    }

    @Override
    public BuildingEntity toEntity() {
        // 创建数据实体
        BuildingEntity entity = new BuildingEntity();
        // 设置角色 UId
        entity._humanUId = this._humanUId;
        // 设置建筑等级
        entity._building1Level = this._homeLevel;
        entity._building2Level = this._pubLevel;
        entity._building3Level = this._forgeLevel;

        return entity;
    }

    /**
     * 从实体中加载数据
     *
     * @param entity
     *
     */
    public void fromEntity(BuildingEntity entity) {
        if (entity == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 主城等级
        this._homeLevel = NullUtil.optVal(
            entity._building1Level,
            this._homeLevel
        );

        // 酒馆等级
        this._pubLevel = NullUtil.optVal(
            entity._building2Level,
            this._pubLevel
        );

        // 铁匠铺等级
        this._forgeLevel = NullUtil.optVal(
            entity._building3Level,
            this._forgeLevel
        );
    }
}
