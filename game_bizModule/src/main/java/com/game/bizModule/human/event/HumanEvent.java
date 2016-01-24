package com.game.bizModule.human.event;

import com.game.bizModule.human.Human;
import com.game.bizModule.human.HumanLog;
import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.gameServer.framework.Player;

import java.text.MessageFormat;
import java.util.*;

/**
 * 角色事件
 *
 * @author hjj2017
 * @since 2015/7/21
 *
 */
public final class HumanEvent {
    /** 单例对象 */
    public static final HumanEvent OBJ = new HumanEvent();
    /** 角色时间监听列表 */
    private final List<IHumanEventListen> _ell = new ArrayList<>();

    /**
     * 类默认构造器
     *
     */
    private HumanEvent() {
    }

    /**
     * 注册事件监听
     *
     * @param newEL
     * @param aheadOfArr
     *
     */
    public void regEventListen(IHumanEventListen newEL, Class<?> ... aheadOfArr) {
        if (aheadOfArr == null ||
            aheadOfArr.length <= 0) {
            // 如果类数组为空
            this.regEventListen(newEL, Collections.emptySet());
        } else {
            // 如果类数组不为空
            this.regEventListen(newEL,
                new HashSet<>(Arrays.asList(aheadOfArr))
            );
        }
    }

    /**
     * 在第一个位置注册事件监听
     *
     * @param newEL
     *
     */
    public void regEventListenAtFirst(IHumanEventListen newEL) {
        if (newEL != null) {
            this._ell.add(0, newEL);
        }
    }

    /**
     * 在最后的位置注册事件监听
     *
     * @param newEL
     *
     */
    public void regEventListenAtLast(IHumanEventListen newEL) {
        if (newEL != null) {
            this._ell.add(newEL);
        }
    }

    /**
     * 注册角色事件监听
     *
     * @param newEL
     * @param aheadOfSet
     *
     */
    public void regEventListen(
        IHumanEventListen newEL, Set<Class<?>> aheadOfSet) {
        if (newEL == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 是否已经注册过指定服务?
        boolean alreadyExist = this._ell.stream().anyMatch(EL -> EL.getClass().equals(newEL.getClass()));

        if (alreadyExist) {
            // 如果已经注册过业务服务,
            // 则直接退出!
            HumanLog.LOG.warn(MessageFormat.format(
                "已经注册过事件监听 {0}, 不再注册",
                newEL.getClass()
            ));
            return;
        }

        if (aheadOfSet != null &&
            aheadOfSet.isEmpty() == false) {
            // 插入位置预设为 -1
            int insertPos = -1;
            // 服务器列表大小
            final int SIZE = this._ell.size();

            for (int i = 0; i < SIZE; i++) {
                // 获取位置变量
                IHumanEventListen currListen = this._ell.get(i);

                if (currListen != null &&
                    aheadOfSet.contains(currListen.getClass())) {
                    // 设置插入位置
                    insertPos = i;
                    break;
                }
            }

            if (insertPos != -1) {
                // 插入到指定服务之前
                this._ell.add(insertPos, newEL);
                return;
            }
        }

        this._ell.add(newEL);
    }

    /**
     * 触发建角事件
     *
     * @param byPlayer
     * @param he
     *
     */
    public void fireCreateHumanEvent(Player byPlayer, HumanEntity he) {
        if (byPlayer == null ||
            he == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        } else {
            this._ell.forEach(el -> el.onCreateNew(
                byPlayer, he
            ));
        }
    }

    /**
     * 触发数据库加载事件
     *
     * @param byPlayer
     * @param h
     *
     */
    public void fireLoadDbEvent(Player byPlayer, Human h) {
        if (h == null) {
            return;
        } else {
            this._ell.forEach(el -> el.onLoadDb(
                byPlayer, h
            ));
        }
    }

    /**
     * 触发进入游戏事件
     *
     * @param h
     *
     */
    public void fireEnterGameEvent(Human h) {
        if (h == null) {
            return;
        } else {
            this._ell.forEach(el -> el.onEnterGame(h));
        }
    }

    /**
     * 触发退出游戏事件
     *
     * @param h
     *
     */
    public void fireQuitGameEvent(Human h) {
        if (h == null) {
            return;
        }

        // 获取角色状态表
        HumanStateTable hStateTbl = h.getPropValOrCreate(HumanStateTable.class);

        if (hStateTbl._inGame) {
            // 如果玩家处在游戏中状态,
            // 触发退出游戏事件
            this._ell.forEach(el -> el.onQuitGame(h));
        }
    }
}
