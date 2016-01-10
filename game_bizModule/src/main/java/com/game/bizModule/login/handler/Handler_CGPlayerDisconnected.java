package com.game.bizModule.login.handler;

import com.game.bizModule.login.bizServ.LoginServ;
import com.game.bizModule.login.msg.CGPlayerDisconnected;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractCGMsgHandler;
import com.game.gameServer.msg.netty.IoSessionManager;

/**
 * 断开连接消息
 * 
 * @author hjj2019
 * @since 2015/7/12
 * 
 */
public class Handler_CGPlayerDisconnected extends AbstractCGMsgHandler<CGPlayerDisconnected> {
    @Override
    public void handle(CGPlayerDisconnected cgMSG) {
        if (cgMSG == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取玩家对象
        final Player p = this.getPlayer();

        if (p == null) {
            // 如果玩家对象为空,
            // 则直接退出!
            return;
        }

        // 玩家不能处理任何类型的 CG 消息
        p._allowMsgTypeMap.clear();

        // 玩家断线
        LoginServ.OBJ.playerDisconnected(p);
        // 删除玩家对象
        super.uninstallPlayer(p);
        IoSessionManager.OBJ.removeSession(p._sessionUId);
        // 清除所有属性
        p.clearAllProp();
    }
}
