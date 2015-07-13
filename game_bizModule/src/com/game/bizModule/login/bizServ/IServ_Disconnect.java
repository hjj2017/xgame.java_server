package com.game.bizModule.login.bizServ;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.bizServ.HumanServ;
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
interface IServ_Disconnect {
    /**
     * 玩家断线
     *
     * @param p
     *
     */
    default void disconnect(final Player p) {
        if (p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家角色
        Human h = Human.getHuman(p);

        // 触发角色退出游戏事件
        HumanServ.OBJ.fireQuitGameEvent(h);
        // 立即写入延迟数据
        LazySavingHelper.OBJ.execUpdateWithPredicate(lso -> {
            return lso != null
                && lso.getGroupUId() != null
                && lso.getGroupUId().equals(h._UId);
        });

        // 记录日志信息
        LoginLog.LOG.info(MessageFormat.format(
            "玩家 {0} 已经退出游戏",
            p._platformUId
        ));
    }
}
