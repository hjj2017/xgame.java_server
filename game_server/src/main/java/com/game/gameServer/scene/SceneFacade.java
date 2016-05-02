package com.game.gameServer.scene;

import com.game.gameServer.msg.AbstractExecutableMsgObj;
import com.game.gameServer.msg.MsgOrigTypeEnum;
import com.game.gameServer.msg.MsgTypeEnum;
import com.game.part.heartbeat.HeartbeatTrigger;
import com.game.part.lazySaving.LazySavingHelper;
import com.game.part.msg.IMsgReceiver;
import com.game.part.msg.type.AbstractMsgObj;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 场景门面
 *
 * @author hjj2019
 * @since 2015/7/2
 *
 */
public final class SceneFacade implements IMsgReceiver {
    /** 单例对象 */
    public static final SceneFacade OBJ = new SceneFacade();

    /**
     * 类默认构造器
     *
     */
    private SceneFacade() {
    }

    @Override
    public void receive(AbstractMsgObj msgObj) {
        if (msgObj == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        HeartbeatTrigger.OBJ.known(new ExecutableMsgRunner((AbstractExecutableMsgObj)msgObj));
    }
}
