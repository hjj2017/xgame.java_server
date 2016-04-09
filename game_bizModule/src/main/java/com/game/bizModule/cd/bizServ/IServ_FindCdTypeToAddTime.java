package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 获取第一个可以增加时间的 Cd 类型
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_FindCdTypeToAddTime {
    /**
     * 获取第一个可以增加时间的 Cd 类型
     * 
     * @param h
     * @param fromCdTypeArr
     * @return CdTypeEnum, 如果返回 null 空值, 则说明没有可以增加时间的 Cd 类型
     * 
     */
    default CdTypeEnum findCdTypeToAddTime(Human h, CdTypeEnum[] fromCdTypeArr) {
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
