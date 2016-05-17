package com.game.part.queued;

/**
 * 私人的发件箱
 *
 * @author hjj2019
 * @since 2016/5/17
 *
 */
class PrivateOutBox {
    static PrivateOutBox createByLeaf(SessionLeaf leaf) {
        PrivateOutBox outBox = new PrivateOutBox();
        return outBox;
    }

    void sendMsg(AbstractQueuedMsg queuedMsg, String strAddr) {

    }
}
