package com.game.part.queued;

import java.util.List;

/**
 * JMS 会话根
 *
 * @author hjj2019
 * @since 2016/5/17
 *
 */
class SessionRoot {
    /** 叶子列表 */
    private List<SessionLeaf> _leafList;

    /**
     * 获取会话叶子列表
     *
     * @return
     *
     */
    List<SessionLeaf> getLeafList() {
        return this._leafList;
    }
}
