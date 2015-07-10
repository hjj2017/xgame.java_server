package com.game.gameServer.msg;

import com.game.gameServer.framework.FrameworkLog;
import com.game.gameServer.framework.Player;
import com.game.gameServer.framework.mina.OnlineSessionManager;
import org.apache.mina.core.session.IoSession;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractGGMsgHandler<TMsgObj extends AbstractGGMsgObj<?>> extends AbstractExecutableMsgHandler<TMsgObj> {
}
