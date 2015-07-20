package com.game.bizModule.human.bizServ;

import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.io.IoOper_QueryHumanEntryList;
import com.game.bizModule.login.LoginStateTable;
import com.game.gameServer.framework.Player;

import java.text.MessageFormat;

/**
 * 查询玩家入口列表
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
interface IServ_QueryHumanEntryList {
    /**
     * 异步方式查询玩家入口列表
     *
     * @param p
     * @param serverName
     *
     */
    default void asyncQueryHumanEntryList(Player p, String serverName) {
        if (p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取登陆状态表
        LoginStateTable loginStateTbl = p.getPropValOrCreate(LoginStateTable.class);

        if (loginStateTbl._platformUIdOk == false ||
            loginStateTbl._authSuccess == false) {
            // 如果登陆验证都没成功,
            // 那还是退出吧!
            HumanLog.LOG.error(MessageFormat.format(
                "玩家 {0} 还没有通过登陆验证",
                p._platformUIdStr
            ));
            return;
        }

        // 获取角色状态表
        HumanStateTable humanStateTbl = p.getPropValOrCreate(HumanStateTable.class);

        if (humanStateTbl._execQueryHumanEntryList) {
            // 如果正在执行查询角色入口列表的任务,
            // 则直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "玩家 {0} 正在操作中",
                p._platformUIdStr
            ));
            return;
        }

        // 正在执行查询
        humanStateTbl._execQueryHumanEntryList = true;
        // 创建异步操作对象
        IoOper_QueryHumanEntryList op = new IoOper_QueryHumanEntryList();
        op._p = p;
        op._serverName = serverName;
        // 执行异步操作!
        HumanServ.OBJ.execute(op);
    }
}
