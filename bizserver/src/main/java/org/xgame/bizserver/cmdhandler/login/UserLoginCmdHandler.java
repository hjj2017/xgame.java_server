package org.xgame.bizserver.cmdhandler.login;

import org.xgame.bizserver.base.MyCmdHandlerContext;
import org.xgame.bizserver.msg.LoginServerProtocol;
import org.xgame.comm.cmdhandler.ICmdHandler;

/**
 * 用户登录指令处理器
 */
public class UserLoginCmdHandler implements ICmdHandler<MyCmdHandlerContext, LoginServerProtocol.UserLoginCmd> {
    @Override
    public void handle(
        MyCmdHandlerContext ctx,
        LoginServerProtocol.UserLoginCmd cmdObj) {
    }
}
