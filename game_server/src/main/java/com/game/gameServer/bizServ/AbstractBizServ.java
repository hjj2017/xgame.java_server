package com.game.gameServer.bizServ;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.game.gameServer.framework.FrameworkLog;

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
     * 而且一定要在某几个服务初始化之前执行
     *
     * @see #needToInit(AbstractBizServ, Set)
     *
     */
    protected static void needToInit(
        AbstractBizServ bizServ, Class<?> ... aheadOfServClazzArr) {
        if (aheadOfServClazzArr == null ||
            aheadOfServClazzArr.length <= 0) {
            // 如果类数组为空
            needToInit(bizServ, Collections.emptySet());
        } else {
            // 如果类数组不为空
            needToInit(bizServ,
                new HashSet<>(Arrays.asList(aheadOfServClazzArr))
            );
        }
    }

    /**
     * 告诉框架: 参数中指定的服务对象需要执行初始化过程,
     * 而且一定要在某几个服务初始化之前执行
     *
     * @param bizServ
     * @param aheadOfServClazzSet 要在某几个服务之前初始化
     *
     */
    private static void needToInit(
        AbstractBizServ bizServ,
        Set<Class<?>> aheadOfServClazzSet) {

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

        if (aheadOfServClazzSet != null &&
            aheadOfServClazzSet.isEmpty() == false) {
            //
            // 如果 aheadOfServClazzSet 集合不为空, 则
            // 遍历 BIZ_SERV_LIST 列表,
            // 获取当前元素并判断当前元素是不是在 aheadOfServClazzSet 集合中出现过?
            // 如果是, 则记录当前位置!
            // ( 换句话说就是要找到最早出现的位置 )
            // 这个位置将作为插入 bizServ 参数的位置...
            //
            // 插入位置预设为 -1
            int insertPos = -1;
            // 服务器列表大小
            final int SIZE = BIZ_SERV_LIST.size();

            for (int i = 0; i < SIZE; i++) {
                // 获取位置变量
                AbstractBizServ currServ = BIZ_SERV_LIST.get(i);

                if (currServ != null &&
                    aheadOfServClazzSet.contains(currServ.getClass())) {
                    // 设置插入位置
                    insertPos = i;
                    break;
                }
            }

            if (insertPos != -1) {
                // 插入到指定服务之前
                BIZ_SERV_LIST.add(insertPos, bizServ);
                return;
            }
        }

        // 添加到列表末尾
        BIZ_SERV_LIST.add(bizServ);
    }
}
