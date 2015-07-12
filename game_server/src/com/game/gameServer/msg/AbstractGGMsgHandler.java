package com.game.gameServer.msg;

import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.mina.OnlineSessionManager;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractGGMsgHandler<TMsgObj extends AbstractGGMsgObj<?>> extends AbstractExecutableMsgHandler<TMsgObj> {
}
