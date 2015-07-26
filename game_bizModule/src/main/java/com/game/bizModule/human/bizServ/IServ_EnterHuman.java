package com.game.bizModule.human.bizServ;

import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.io.IoOper_LoadHuman;
import com.game.bizModule.login.LoginStateTable;
import com.game.gameServer.framework.Player;

import java.text.MessageFormat;

/**
 * 进入角色
 *
 * @author hjj2017
 * @since 2015/7/20
 *
 */
interface IServ_EnterHuman {
    /**
     * 异步方式进入角色
     *
     * @param p
     * @param humanUId
     *
     */
    default void asyncEnterHuman(Player p, long humanUId) {
        if (p == null ||
            humanUId <= 0) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取登陆状态表
        LoginStateTable loginStateTbl = p.getPropValOrCreate(LoginStateTable.class);

        if (loginStateTbl._platformUIdOk == false ||
            loginStateTbl._authSuccess == false) {
            // 平台验证都没过,
            // 直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "玩家 {0} 还没有通过登陆验证",
                p._platformUIdStr
            ));
            return;
        }

        // 获取角色状态表
        HumanStateTable hStateTable = p.getPropValOrCreate(HumanStateTable.class);

        if (hStateTable._queryHumanEntryListTimes <= 0 ||
            hStateTable._hasHuman == false) {
            // 如果还没有查询过角色入口列表,
            // 或者是已有角色,
            // 则直接退出!
            // 都 TM 还不知道自己是谁呢, 进个毛进
            HumanLog.LOG.error(MessageFormat.format(
                "玩家 {0} 还没有查询过角色入口列表, 或者玩家已有角色",
                p._platformUIdStr
            ));
            return;
        }

        if (hStateTable._execQueryHumanEntryList ||
            hStateTable._execCreateHuman ||
            hStateTable._execEnterHuman) {
            // 如果正在查询角色入口列表,
            // 或者正在创建角色,
            // 再或者正在进入角色,
            // 则直接退出!
            HumanLog.LOG.error(MessageFormat.format(
                "玩家 {0} 已有角色或正在操作中",
                p._platformUIdStr
            ));
            return;
        }

        // 修改状态表值
        hStateTable._humanLoadFromDbOk = false;
        hStateTable._execEnterHuman = true;

        // 创建异步操作对象
        IoOper_LoadHuman op = new IoOper_LoadHuman();
        op._p = p;
        op._humanUId = humanUId;
        // 执行异步操作
        HumanServ.OBJ.execute(op);
    }
}
