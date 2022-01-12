package org.xgame.comm.lazysave;

import org.xgame.comm.util.MyTimer;

import java.util.concurrent.TimeUnit;

/**
 * 延迟保存服务
 */
public class LazySaveService {
    private static final LazySaveService INSTANCE = new LazySaveService();

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static LazySaveService getInstance() {
        return INSTANCE;
    }

    public void saveOrUpdate() {
    }

    public void delete() {

    }

    public void startHeartbeat() {
        MyTimer.getInstance().scheduleWithFixedDelay(
            this::heartbeat,
            1, 1, TimeUnit.SECONDS
        );
    }

    private void heartbeat() {

    }
}
