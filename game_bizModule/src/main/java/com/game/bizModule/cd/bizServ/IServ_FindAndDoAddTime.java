package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 查找并增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_FindAndDoAddTime {
    /**
     * 从 cdTypeArr 中查找第一个可以累计时间的 Cd 类型, 并增加 Cd 时间!
     * <font color="#990000">
     * 注意: 该函数适用于建筑类型 Cd, 因为建筑有多个 Cd</font>
     * 
     * @param h
     * @param fromCdTypeArr
     * @param ms
     * @return 
     * 
     */
    default Result_FindAndDoAddTime findAndDoAddTime(
        Human h,
        CdTypeEnum[] fromCdTypeArr,
        long ms) {
        // 断言参数对象不为空
        Assert.notNull(h, "h");
        Assert.notNullOrEmpty(fromCdTypeArr, "fromCdTypeArr");

        // 借出结果对象
        Result_FindAndDoAddTime result = BizResultPool.borrow(Result_FindAndDoAddTime.class);
        // 首先查找 Cd 类型
        CdTypeEnum targetCdType = firstCanAdd(h, fromCdTypeArr);

        if (targetCdType == null) {
            // 如果找不到 Cd 类型可以累计时间,
            // 则直接推出!
            result._errorCode = MultiLangDef.LANG_CD_cannotFoundCdTypeToAddTime;
            return result;
        }

        // 增加 Cd 时间
        Result_DoAddTime result_2 = CdServ.OBJ.doAddTime(h, targetCdType, ms);

        if (result_2.isFail()) {
            result._errorCode = result_2._errorCode;
            return result;
        }

        // 设置已使用的 Cd 类型
        result._usedCdType = result_2._usedCdType;
        return result;
    }

    /**
     * 获取第一个可以增加时间的 Cd 类型
     * 
     * @param h
     * @param fromCdTypeArr
     * @return 
     * 
     */
    static CdTypeEnum firstCanAdd(Human h, CdTypeEnum[] fromCdTypeArr) {
        // 断言参数对象不为空
        Assert.notNull(h, "h");
        Assert.notNullOrEmpty(fromCdTypeArr, "fromCdTypeArr");

        for (CdTypeEnum cdType : fromCdTypeArr) {
            if (CdServ.OBJ.canAddTime(h._humanUId, cdType)._canAddTime) {
                // 如果可以增加 Cd 时间, 
                // 则直接返回!
                return cdType;
            }
        }

        return null;
    }
}
