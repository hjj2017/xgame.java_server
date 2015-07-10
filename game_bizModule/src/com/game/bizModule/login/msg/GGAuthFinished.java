package com.game.bizModule.login.msg;

import com.game.bizModule.login.handler.Handler_GGAuthFinished;
import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.AbstractGGMsgHandler;
import com.game.gameServer.msg.AbstractGGMsgObj;

/**
 *
 */
public class GGAuthFinished extends AbstractGGMsgObj<Handler_GGAuthFinished> {
    @Override
    protected Handler_GGAuthFinished newHandlerObj() {
        return null;
    }
}
