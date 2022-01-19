package org.xgame.comm.lazysave;

import org.slf4j.Logger;
import org.xgame.comm.CommLog;
import org.xgame.comm.util.MyTimer;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 延迟保存服务
 */
public final class LazySaveService {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = CommLog.LOGGER;

    /**
     * 提交心跳绑定 Id
     */
    private static final int SUBMIT_HEARTBEAT_BIND_ID = 0;

    /**
     * 执行心跳绑定 Id
     */
    private static final int DO_HEARTBEAT_BIND_ID = 1;

    /**
     * 心跳周期
     */
    private static final int PERIOD = 1000;

    /**
     * 当数据对象空闲超过指定时间后才真正执行更新操作
     */
    private static final long IDLE_TO_UPDATE = 20000L;

    /**
     * 一次写出数量
     */
    private static final int MAX_WRITE_COUNT = 512;

    /**
     * 单例对象
     */
    private static final LazySaveService INSTANCE = new LazySaveService();

    /**
     * 延迟入口字典
     */
    private final Map<String, LazyEntryWrapper> _lazyEntryMap = new ConcurrentHashMap<>();

    /**
     * 私有化类默认构造器
     */
    private LazySaveService() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static LazySaveService getInstance() {
        return INSTANCE;
    }

    /**
     * 延迟保存或更新
     *
     * @param le 延迟入口
     */
    public void saveOrUpdate(ILazyEntry le) {
        if (null == le) {
            return;
        }

        LazyEntryWrapper wrapper = _lazyEntryMap.get(le.getUId());

        if (null == wrapper) {
            _lazyEntryMap.putIfAbsent(
                le.getUId(), new LazyEntryWrapper(le)
            );
        }

        wrapper = _lazyEntryMap.get(le.getUId());

        if (null == wrapper) {
            LOGGER.warn(
                "字典中的延迟入口为空, UId = {}",
                le.getUId()
            );
            return;
        }

        wrapper.putLastChangeTime(System.currentTimeMillis());
    }

    /**
     * 保存和更新, 立即执行
     *
     * @param le 延迟入口
     */
    public void saveOrUpdateImmediate(ILazyEntry le) {
        if (null == le) {
            return;
        }

        Callable<Void> caller = () -> {
            _lazyEntryMap.remove(le.getUId());
            le.saveOrUpdate();
            return null;
        };

        MyTimer.getInstance().schedule(
            DO_HEARTBEAT_BIND_ID, caller, 0, TimeUnit.MILLISECONDS
        );
    }

    /**
     * 延迟删除
     *
     * @param le 延迟入口
     */
    public void delete(ILazyEntry le) {
        if (null == le) {
            return;
        }

        LazyEntryWrapper wrapper = _lazyEntryMap.get(le.getUId());

        if (null == wrapper) {
            _lazyEntryMap.putIfAbsent(
                le.getUId(), new LazyEntryWrapper(le)
            );
        }

        wrapper = _lazyEntryMap.get(le.getUId());

        if (null == wrapper) {
            LOGGER.warn(
                "字典中的延迟入口为空, UId = {}",
                le.getUId()
            );
            return;
        }

        wrapper.putLastChangeTime(System.currentTimeMillis()).putDel(true);
    }

    /**
     * 删除, 立即执行
     *
     * @param le 延迟入口
     */
    public void deleteImmediate(ILazyEntry le) {
        if (null == le) {
            return;
        }

        Callable<Void> caller = () -> {
            _lazyEntryMap.remove(le.getUId());
            le.delete();
            return null;
        };

        MyTimer.getInstance().schedule(
            DO_HEARTBEAT_BIND_ID, caller, 0, TimeUnit.MILLISECONDS
        );
    }

    /**
     * 放弃所有
     */
    public void forgetALL() {
        _lazyEntryMap.clear();
    }

    /**
     * 启动心跳
     */
    public void startHeartbeat() {
        // 在固定定时器里提交任务
        MyTimer.getInstance().scheduleWithFixedDelay(
            SUBMIT_HEARTBEAT_BIND_ID, this::submitHeartbeat, PERIOD, PERIOD, TimeUnit.SECONDS
        );
    }

    /**
     * 提交心跳处理
     */
    private void submitHeartbeat() {
        // 在固定定时器里执行任务
        MyTimer.getInstance().schedule(
            DO_HEARTBEAT_BIND_ID, this::doHeartbeat, 0, TimeUnit.MILLISECONDS
        );
    }

    /**
     * 执行心跳
     *
     * @return 空值
     */
    private Void doHeartbeat() {
        // 获取迭代器
        Iterator<LazyEntryWrapper> it = _lazyEntryMap.values().iterator();
        // 获取当前时间
        long nowTime = System.currentTimeMillis();

        // 写出数量
        int wc = 0;

        LOGGER.debug("执行延迟保存");

        while (it.hasNext() &&
            wc < MAX_WRITE_COUNT) {
            // 获取入口
            LazyEntryWrapper wrapper = it.next();

            if (null == wrapper) {
                // 如果入口对象为空,
                // 则直接跳过!
                it.remove();
                continue;
            }

            if ((nowTime - wrapper.getLastChangeTime()) < IDLE_TO_UPDATE) {
                // 如果还没有到时间,
                // 则直接跳过!
                continue;
            }

            it.remove();
            wc++;

            // 获取延迟入口
            final ILazyEntry le = wrapper.getLazyEntry();

            if (null == le) {
                continue;
            }

            if (wrapper.isDel()) {
                // 执行删除
                le.delete();
            } else {
                // 执行保存
                le.saveOrUpdate();
            }
        }

        return null;
    }
}
