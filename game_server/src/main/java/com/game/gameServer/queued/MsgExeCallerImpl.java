package com.game.gameServer.queued;

import com.game.part.queued.AbstractQueuedMsg;
import com.game.part.queued.IMsgExeCaller;

/**
 * 消息执行调用者实现类
 *
 * @author hjj2019
 * @since 2016/05/01
 */
public class MsgExeCallerImpl implements IMsgExeCaller {
    @Override
    public void callExec(AbstractQueuedMsg queuedMsg) {
        if (queuedMsg == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        queuedMsg.execute();
    }
}
