package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.BuildingLog;
import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.building.tmpl.BuildingLevelUpTmpl;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.bizServ.Result_DoAddTime;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.part.util.BizResultObj;
import com.game.part.util.BizResultPool;

import java.text.MessageFormat;

/**
 * 建筑升级
 *
 * @author hjj2019
 * @since 2015/7/24
 *
 */
interface IServ_DoLevelUp {
    /** 建筑类型 Cd 数组 */
    static final CdTypeEnum[] BUILDING_CD_TYPE_ARR = {
        CdTypeEnum.building1,
        CdTypeEnum.building2,
        CdTypeEnum.building3,
    };

    /**
     * 建筑升级
     *
     * @param h
     * @param bt
     *
     */
    default Result_DoLevelUp doLevelUp(
        Human h,
        BuildingTypeEnum bt) {
        // 借出结果对象
        Result_DoLevelUp result = BizResultPool.borrow(Result_DoLevelUp.class);

        if (h == null ||
            bt == null) {
            // 如果参数对象为空,
            // 则直接退出!
            result._errorCode = MultiLangDef.LANG_COMM_paramError;
            return result;
        }

        // 获取建筑管理器
        BuildingManager mngrObj = h.getPropVal(BuildingManager.class);

        if (mngrObj == null) {
            // 如果管理器对象为空,
            // 则直接退出!
            result._errorCode = MultiLangDef.LANG_COMM_nullMngrObj;
            return result;
        }

        // 建筑是否不能升级?
        int errorCode = cannotLevelUp(h, bt, mngrObj);

        if (errorCode != BizResultObj.NO_ERROR) {
            // 如果建筑不能升级,
            // 则直接退出!
            result._errorCode = errorCode;
            return result;
        }

        // 获取建筑升级配置
        BuildingLevelUpTmpl levelUpTmpl = BuildingLevelUpTmpl.getByBuildingTypeAndLevel(
            bt, mngrObj.getLevel(bt) + 1
        );

        // 获取所需银两
        final int needSilver = levelUpTmpl._needSilver.getIntVal();

        // TODO : 判断银两

        // 查找可以增加时间的 Cd 类型
        CdTypeEnum cdType = CdServ.OBJ.findCdTypeToAddTime(h, BUILDING_CD_TYPE_ARR);

        if (cdType == null) {
            // 如果没有可用的 Cd 类型,
            // 则直接退出!
            result._errorCode = MultiLangDef.LANG_CD_cannotFoundCdTypeToAddTime;
            return result;
        }

        // TODO : 消耗银两
        // if (MoneyServ.OBJ.tryCost(h, needSilver) == false) {
        //     // 如果消耗银两失败,
        //     // 则直接退出!
        //     return;
        // }

        // 增加 Cd 时间
        Result_DoAddTime result_2 = CdServ.OBJ.doAddTime(h, cdType, levelUpTmpl._needCdTime.getIntVal());

        if (result_2.isFail()) {
            // 如果增加 Cd 时间失败,
            // 则直接退出!
            result._errorCode = result_2._errorCode;
            return result;
        }

        // 设置所需银两和 Cd 类型
        result._usedSilver = needSilver;
        result._usedCdType = cdType;

        // 增加建筑等级并保存
        mngrObj.addLevel(bt, +1);
        mngrObj.saveOrUpdate();

        return result;
    }

    /**
     * 建筑类型不能升级? 如果不能升级则返回不能升级的理由
     *
     * @param h
     * @param bt
     * @param mngrObj
     * @return 不能升级的理由
     *
     * @see BizResultObj#NO_ERROR
     *
     */
    static int cannotLevelUp(final Human h, final BuildingTypeEnum bt, final BuildingManager mngrObj) {
        if (h == null ||
            bt == null ||
            mngrObj == null) {
            // 参数不能为空
            return MultiLangDef.LANG_COMM_paramError;
        }

        if (bt == BuildingTypeEnum.home) {
            // 如果升级的是主城,
            // 那么看看主城等级是否会超过角色等级?
            if (mngrObj._homeLevel >= h._humanLevel) {
                // 如果主城等级 >= 角色等级,
                // 则直接退出!
                BuildingLog.LOG.error(MessageFormat.format(
                    "主城等级不能大于角色等级, 角色 = {0}",
                    String.valueOf(h._humanUId)
                ));
                return MultiLangDef.LANG_BUILDING_homeLevelCannotGTHumanLevel;
            }
        } else {
            // 如果升级的不是主城,
            // 那么先获取目标建筑等级
            final int targetBuildingLevel = mngrObj.getLevel(bt);

            if (targetBuildingLevel <= 0) {
                // 如果建筑未开启,
                // 则直接退出!
                BuildingLog.LOG.error(MessageFormat.format(
                    "建筑尚未开启, 角色 = {0}, 建筑 = {1}",
                    String.valueOf(h._humanUId),
                    bt.getStrVal()
                ));
                return MultiLangDef.LANG_BUILDING_notOpenYet;
            }

            // 获取主城等级
            int homeLevel = mngrObj._homeLevel;

            if (targetBuildingLevel >= homeLevel) {
                // 如果目标建筑等级 >= 主城等级,
                // 则直接退出!
                BuildingLog.LOG.error(MessageFormat.format(
                    "建筑等级不能超过主城等级, 角色 = {0}, 建筑 = {1}",
                    String.valueOf(h._humanUId),
                    bt.getStrVal()
                ));
                return MultiLangDef.LANG_BUILDING_buildingLevelCannotGTHomeLevel;
            }
        }

        return BizResultObj.NO_ERROR;
    }
}
