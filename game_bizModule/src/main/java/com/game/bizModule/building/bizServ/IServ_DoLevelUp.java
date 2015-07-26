package com.game.bizModule.building.bizServ;

import com.game.bizModule.building.BuildingLog;
import com.game.bizModule.building.model.BuildingTypeEnum;
import com.game.bizModule.building.tmpl.BuildingLevelUpTmpl;
import com.game.bizModule.cd.bizServ.CdServ;
import com.game.bizModule.cd.bizServ.Result_FindAndDoAddTime;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
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
                result._errorCode = MultiLangDef.LANG_BUILDING_homeLevelCannotGTHumanLevel;
                return result;
            }
        } else {
            // 获取目标建筑等级
            final int targetBuildingLevel = mngrObj.getLevel(bt);

            if (targetBuildingLevel <= 0) {
                // 如果建筑未开启,
                // 则直接退出!
                BuildingLog.LOG.error(MessageFormat.format(
                    "建筑尚未开启, 角色 = {0}, 建筑 = {1}",
                    String.valueOf(h._humanUId),
                    bt.getStrVal()
                ));
                return result;
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
                result._errorCode = MultiLangDef.LANG_BUILDING_buildingLevelCannotGTHomeLevel;
                return result;
            }
        }

        // 获取建筑升级配置
        BuildingLevelUpTmpl levelUpTmpl = BuildingLevelUpTmpl.getByBuildingTypeAndLevel(
            bt, mngrObj.getLevel(bt) + 1
        );

        // 获取所需银两
        final int needSilver = levelUpTmpl._needSilver.getIntVal();
        // TODO : 消耗银两
        // if (MoneyServ.OBJ.tryCost(h, needSilver) == false) {
        //     // 如果消耗银两失败,
        //     // 则直接退出!
        //     return;
        // }

        // 设置所需银两
        result._usedSilver = needSilver;
        // 查找并增加 Cd 时间
        Result_FindAndDoAddTime result_2 = CdServ.OBJ.findAndDoAddTime(
            h, BUILDING_CD_TYPE_ARR,
            levelUpTmpl._needCdTime.getLongVal()
        );

        if (result_2.isFail()) {
            // 如果增加 Cd 失败,
            // 则直接退出!
            BuildingLog.LOG.error(MessageFormat.format(
                "累计建筑升级 Cd 时间失败, 角色 = {0}, 建筑 = {1}",
                String.valueOf(h._humanUId),
                bt.getStrVal()
            ));
            result._errorCode = result_2._errorCode;
            return result;
        }

        // 设置已使用的 Cd 类型
        result._usedCdType = result_2._usedCdType;

        // 增加建筑等级并保存
        mngrObj.addLevel(bt, +1);
        mngrObj.saveOrUpdate();

        return result;
    }
}
