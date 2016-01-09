package com.game.bizModule.login.bizServ;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.login.LoginLog;
import com.game.gameServer.framework.Player;
import com.game.part.lazySaving.LazySavingHelper;

import java.text.MessageFormat;

/**
 * 玩家断线
 *
 * @author hjj2017
 * @since 2015/7/12
 *
 */
interface IServ_PlayerDisconnected {
    /**
     * 玩家断线
     *
     * @param p
     *
     */
    default void playerDisconnected(final Player p) {
        if (p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家角色
        final Human h = Human.getHumanByPlayer(p);

        if (h == null) {
            // 如果角色对象为空,
            // 则直接退出!
            return;
        }

        // 触发角色退出游戏事件
        HumanEvent.OBJ.fireQuitGameEvent(h);
        // 立即写入延迟数据
        LazySavingHelper.OBJ.execUpdateWithPredicate(lso -> {
            return lso != null
                && lso.getGroupKey() != null
                && lso.getGroupKey().equals(h.getGroupKey());
        });

        // 记录日志信息
        LoginLog.LOG.info(MessageFormat.format(
            "玩家 {0} 已经退出游戏",
            p._platformUIdStr
        ));
    }
}
