package com.game.bizModule.building.bizServ;

import static com.game.bizModule.building.model.BuildingTypeEnum.*;

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
    /** 兵营等级 */
    public int _campLevel = 0;
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
     * 增加建筑等级
     *
     * @param buildingType
     * @param deltaVal
     *
     */
    public void addLevel(BuildingTypeEnum buildingType, int deltaVal) {
        if (buildingType == null ||
            deltaVal <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        if (buildingType == BuildingTypeEnum.home) {
            this._homeLevel += deltaVal;
        } else if (buildingType == BuildingTypeEnum.camp) {
            this._campLevel += deltaVal;
        } else if (buildingType == BuildingTypeEnum.forge) {
            this._forgeLevel = deltaVal;
        } else {
            // 抛出异常
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
        entity._building2Level = this._campLevel;
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

        // 兵营等级
        this._campLevel = NullUtil.optVal(
            entity._building2Level,
            this._campLevel
        );

        // 铁匠铺等级
        this._forgeLevel = NullUtil.optVal(
            entity._building3Level,
            this._forgeLevel
        );
    }
}
