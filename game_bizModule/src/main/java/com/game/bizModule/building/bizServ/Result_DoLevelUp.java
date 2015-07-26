package com.game.bizModule.building.bizServ;

import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.part.util.BizResultObj;

/**
 * 建筑升级结果
 *
 * @author hjj2017
 * @since 2015/7/25
 *
 */
public class Result_DoLevelUp extends BizResultObj {
    /** 已使用的 Cd 类型 */
    public CdTypeEnum _usedCdType = null;
    /** 已使用的银两 */
    public int _usedSilver = 0;

    @Override
    protected void clearContent() {
    }
}
