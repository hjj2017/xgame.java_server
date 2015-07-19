package com.game.gameServer.bizServ;

import com.game.gameServer.framework.FrameworkLog;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏业务服务
 * 
 * @author hjj2019
 * @since 2015/7/19
 *
 */
public abstract class AbstractBizServ implements IIoOperExecutable {
    /** 角色时间监听列表 */
    public static final List<AbstractBizServ> BIZ_SERV_LIST = new ArrayList<>();

    /**
     * 初始化业务服务
     *
     */
    public void init() {
    }

    /**
     * 告诉框架: 参数中指定的服务对象需要执行初始化过程
     *
     * @param bizServ
     * @see #needToInit(AbstractBizServ, Class)
     *
     */
    protected static void needToInit(AbstractBizServ bizServ) {
        needToInit(bizServ,
            (Class<AbstractBizServ>)null
        );
    }

    /**
     * 告诉框架: 参数中指定的服务对象需要执行初始化过程,
     * 而且一定要在某个服务初始化之前执行
     *
     * @param bizServ
     * @param aheadBizServ
     * @see #needToInit(AbstractBizServ, Class)
     *
     */
    protected static void needToInit(
        AbstractBizServ bizServ, AbstractBizServ aheadBizServ) {
        needToInit(bizServ,
            aheadBizServ.getClass()
        );
    }

    /**
     * 告诉框架: 参数中指定的服务对象需要执行初始化过程
     *
     * @param bizServ
     * @param aheadOfServClazz 要在某个服务之前初始化
     *
     */
    protected static void needToInit(
        AbstractBizServ bizServ,
        Class<? extends AbstractBizServ> aheadOfServClazz) {
        if (bizServ == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 是否已经注册过指定服务?
        boolean alreadyExist = BIZ_SERV_LIST.stream().anyMatch(BS -> BS.getClass().equals(bizServ.getClass()));

        if (alreadyExist) {
            // 如果已经注册过业务服务,
            // 则直接退出!
            FrameworkLog.LOG.warn(MessageFormat.format(
                "已经注册过业务服务 {0}, 不再注册",
                bizServ.getClass()
            ));
            return;
        }

        if (aheadOfServClazz != null) {
            // 获取服务索引位置
            AbstractBizServ aheadOfServ = BIZ_SERV_LIST.stream()
                .filter(BS -> BS.getClass().equals(aheadOfServClazz))
                .findAny()
                .orElse(null);

            if (aheadOfServ != null) {
                // 插入到指定服务之前
                BIZ_SERV_LIST.add(
                    BIZ_SERV_LIST.indexOf(aheadOfServ),
                    bizServ
                );
            }
        } else {
            // 添加到列表末尾
            BIZ_SERV_LIST.add(bizServ);
        }
    }
}
