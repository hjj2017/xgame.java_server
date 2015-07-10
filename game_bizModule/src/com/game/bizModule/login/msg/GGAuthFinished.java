package com.game.bizModule.login.msg;

import com.game.bizModule.login.handler.Handler_GGAuthFinished;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.AbstractGGMsgObj;

/**
 * 登陆验证完成
 *
 * @author hjj2017
 * @since 2015/7/10
 *
 */
public class GGAuthFinished extends AbstractGGMsgObj<Handler_GGAuthFinished> {
    /** 玩家对象 */
    public Player _p = null;

    @Override
    protected Handler_GGAuthFinished newHandlerObj() {
        return new Handler_GGAuthFinished();
    }
}
