package com.game.bizModule.newer.bizServ;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.gameServer.bizServ.AbstractBizServ;

/**
 * 新玩家
 *
 * @author hjj2017
 * @since 2016/1/11
 *
 */
public class NewerServ extends AbstractBizServ implements IHumanEventListen {
    /** 单例对象 */
    public static final NewerServ OBJ = new NewerServ();

    /**
     * 类默认构造器
     *
     */
    private NewerServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        HumanEvent.OBJ.regEventListenAtFirst(this);
    }

    @Override
    public void onEnterGame(Human h) {
        if (h == null ||
            h._newerRewardCheckout) {
            // 如果参数对象为空,
            // 或者玩家已经领取过奖励,
            // 则直接退出!
            return;
        }

        // 修改玩家等级并给钱
        h._humanLevel = 99;
        h._gold = 9999;
        h._newerRewardCheckout = true;
        h.saveOrUpdate();
    }
}
