package com.game.bizModule.human.bizServ;

import com.game.gameServer.bizServ.AbstractBizServ;

/**
 * 玩家角色服务
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class HumanServ extends AbstractBizServ implements IServ_QueryHumanEntryList, IServ_CreateHuman {
    /** 单例对象 */
    public static final HumanServ OBJ = new HumanServ();

    /**
     * 类默认构造器
     *
     */
    private HumanServ() {
    }
}
