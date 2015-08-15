package com.game.bizModule.chat.msg;

import java.util.List;

import com.game.bizModule.chat.handler.Handler_GGPost;
import com.game.gameServer.msg.AbstractGGMsgObj;

/**
 * 投递聊天消息
 *
 * @author hjj2017
 * @since 2015/8/17
 *
 */
public final class GGPost extends AbstractGGMsgObj<Handler_GGPost> {
    /** GC 消息 */
    public GCCommChat _gcMSG = null;
    /** 角色 UId 列表 */
    public List<Long> _humanUIdList = null;

    @Override
    protected Handler_GGPost newHandlerObj() {
        return new Handler_GGPost();
    }
}
