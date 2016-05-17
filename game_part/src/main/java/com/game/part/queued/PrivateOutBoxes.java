package com.game.part.queued;

import com.game.part.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 私人的发件箱列表
 *
 * @author hjj2019
 * @since 2016/5/17
 *
 */
class PrivateOutBoxes {
    /** 私人发件箱列表 */
    private List<PrivateOutBox> _outBoxList = null;

    /**
     * 初始化发件箱列表
     *
     * @param root
     *
     */
    void init(SessionRoot root) {
        // 断言参数不为空
        Assert.notNull(root, "null root");

        this._outBoxList = root.getLeafList().stream()
            .filter((leaf) -> leaf.getAddrType() == AddrTypeEnum.privateAddr)
            .map((leaf) -> PrivateOutBox.createByLeaf(leaf))
            .collect(Collectors.toList());
    }

    void sendMsg(AbstractQueuedMsg queuedMsg, String strAddr) {
        if (queuedMsg == null ||
            strAddr == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        int index = RouterStrategy.indexByAddr(strAddr, this._outBoxList.size());
        PrivateOutBox outBox = this._outBoxList.get(index);
        outBox.sendMsg(queuedMsg, strAddr);
    }
}
