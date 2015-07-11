package com.game.gameServer.msg;

/**
 * 抽象的消息处理器
 * 
 * @author hjj2017
 * @param <TMsgObj>
 * 
 */
public abstract class AbstractGGMsgHandler<TMsgObj extends AbstractGGMsgObj<?>> extends AbstractExecutableMsgHandler<TMsgObj> {
}
