package com.game.bizModule.cd.bizServ;

import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.multiLang.MultiLangDef;
import com.game.bizModule.time.TimeServ;
import com.game.part.util.Assert;
import com.game.part.util.BizResultPool;

/**
 * 增加 Cd 时间
 * 
 * @author hjj2017
 * @since 2014/6/23
 * 
 */
interface IServ_DoAddTime {
    /**
     * 增加 Cd 时间
     * 
     * @param h
     * @param cdType
     * @param ms
     * @return 
     * 
     */
    default Result_DoAddTime doAddTime(
        Human h,
        CdTypeEnum cdType, 
        long ms) {
        // 断言参数对象不为空
        Assert.notNull(cdType, "cdType");
        // 借出结果对象
        Result_DoAddTime result = BizResultPool.borrow(Result_DoAddTime.class);

        if (ms <= 0) {
            // 如果累计时间 <= 0,
            // 则直接退出!
            result._errorCode = MultiLangDef.LANG_CD_addTimeLeq0;
            return result;
        }

        // 是否可以增加 Cd 时间 ?
        Result_CanAddTime result_2 = CdServ.OBJ.canAddTime(
            h._humanUId, cdType
        );

        if (result_2._canAddTime == false) {
            // 如果不能增加 Cd 时间, 
            // 则直接退出!
            result._errorCode = result_2._errorCode;
            return result;
        }

        // 获取管理器
        CdManager mngrObj = CdServ.OBJ._mngrMap.get(h._humanUId);
        // 获取计时器
        CdTimer t = mngrObj._cdTimerMap.get(cdType);
        // 获取当前时间
        long now = TimeServ.OBJ.now();

        // 累计时间并保存
        t.addTime(now, ms);
        t.saveOrUpdate();

        return result;
    }
}
