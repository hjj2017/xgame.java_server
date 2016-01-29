package com.game.bizModule.human;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.game.bizModule.human.bizServ.HumanNaming;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.gameServer.framework.Player;
import com.game.gameServer.msg.netty.IoSessionManager;
import com.game.part.GameError;
import com.game.part.util.Assert;
import com.game.part.util.NullUtil;

/**
 * 角色,
 * <font color="#990000">注意: 角色自己就是自己的财物</font>
 * 
 * @author hjj2017
 * @since 2015/7/11
 *
 */
public final class Human extends AbstractHumanBelonging<HumanEntity> {
    /** 服务器名称 */
    public String _serverName = null;
    /** 角色名称 */
    public String _humanName = null;
    /** 武将模板 Id */
    public int _heroTmplId;
    /** 经验值 */
    public int _exp;
    /** 角色等级 */
    public int _humanLevel = 1;
    /** 金币 */
    public int _gold = 0;
    /** 新手奖励是否已领取? */
    public boolean _newerRewardCheckout = false;

    /** 玩家引用 */
    private WeakReference<Player> _pRef = null;

    /**
     * 类默认构造器
     *
     * @param UId
     *
     */
    private Human(long UId) {
        super(UId);
    }

    /**
     * 获取角色全名
     *
     * @return
     *
     */
    public String getFullName() {
        return HumanNaming.OBJ.getFullName(this._serverName, this._humanName);
    }

    /**
     * 获取玩家对象
     *
     * @return
     *
     */
    public Player getPlayer() {
        if (this._pRef == null) {
            return null;
        } else {
            return this._pRef.get();
        }
    }

    /**
     * 抱住玩家的大腿!
     *
     * @param p
     *
     */
    public void bindPlayer(Player p) {
        if (p == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        if (this._pRef != null &&
            this._pRef.get() != null) {
            // 如果已经绑定过玩家,
            // 则直接抛出异常!
            throw new GameError(MessageFormat.format(
                "不能重复绑定玩家, 角色 = {0}, 绑定到玩家 = {1}",
                String.valueOf(this._humanUId),
                p._platformUIdStr
            ));
        }

        // 设置玩家引用
        this._pRef = new WeakReference<>(p);
    }

    /**
     * 从玩家对象获取角色
     *
     * @param p
     * @return
     *
     */
    public static Human getHumanByPlayer(Player p) {
        if (p == null) {
            return null;
        } else {
            return p.getPropVal(Human.class);
        }
    }

    /**
     * @see Player#getPropValOrCreate(Class)
     */
    public<T> T getPropValOrCreate(Class<T> byClazz) {
        if (this.getPlayer() != null) {
            return this.getPlayer().getPropValOrCreate(byClazz);
        } else {
            return null;
        }
    }

    /**
     * @see Player#getPropVal(Class)
     */
    public<T> T getPropVal(Class<T> byClazz) {
        if (this.getPlayer() != null) {
            return this.getPlayer().getPropVal(byClazz);
        } else {
            return null;
        }
    }

    /**
     * 创建角色实体
     *
     * @return
     *
     */
    @Override
    public HumanEntity toEntity() {
        // 创建角色实体
        HumanEntity he = new HumanEntity();
        // 设置实体属性
        he._humanUId = this._humanUId;
        he._platformUIdStr = this.getPlayer()._platformUIdStr;
        he._fullName = this.getFullName();
        he._serverName = this._serverName;
        he._humanName = this._humanName;
        he._exp = this._exp;
        he._humanLevel = this._humanLevel;
        he._gold = this._gold;
        he._newerRewardCheckOut = this._newerRewardCheckout ? 1 : 0;

        return he;
    }

    /**
     * 从角色实体中加载数据
     *
     * @param entity
     *
     */
    public void fromEntity(HumanEntity entity) {
        if (entity == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        this._serverName = entity._serverName;
        this._humanName = entity._humanName;
        this._heroTmplId = entity._heroTmplId;
        this._exp = NullUtil.optVal(entity._exp, 0);
        this._humanLevel = NullUtil.optVal(entity._humanLevel, this._humanLevel);
        this._gold = NullUtil.optVal(entity._gold, this._gold);
        this._newerRewardCheckout = NullUtil.optVal(entity._newerRewardCheckOut, 0) == 1;
    }

    /**
     * 创建角色
     *
     * @param entity
     * @return
     *
     */
    public static Human create(HumanEntity entity) {
        // 断言参数不为空
        Assert.notNull(entity);
        // 创建角色并加载数据
        Human h = new Human(entity._humanUId);
        h.fromEntity(entity);

        return h;
    }

    /**
     * 获取角色列表
     *
     * @param humanUIdList
     * @return
     *
     */
    public static List<Human> getHumanList(List<Long> humanUIdList) {
        if (humanUIdList == null ||
            humanUIdList.isEmpty()) {
            // 如果参数对象为空,
            // 则直接退出!
            return null;
        }

        // 获取会话 UId 集合
        Set<Long> sessionUIdSet = IoSessionManager.OBJ.getSessionUIdSet();

        if (sessionUIdSet == null ||
            sessionUIdSet.isEmpty()) {
            // 如果会话 UId 集合为空,
            // 则直接退出!
            return null;
        }

        return sessionUIdSet.stream().map(sessionUId -> {
            // 根据会话 UId 获取玩家对象
            Player p = IoSessionManager.OBJ.getPlayerBySessionUId(sessionUId);

            if (p == null) {
                // 如果玩家对象为空,
                // 则直接退出!
                return null;
            }

            // 获取角色对象
            Human h = Human.getHumanByPlayer(p);

            if (h != null &&
                humanUIdList.contains(h._humanUId)) {
                return h;
            } else {
                return null;
            }
        }).filter(h -> h != null).collect(Collectors.toList());
    }

    /**
     * 获取所有在线的角色列表
     *
     * @return
     *
     */
    public static List<Human> getAllOnlineHuman() {
        // 获取会话 UId 集合
        Set<Long> sessionUIdSet = IoSessionManager.OBJ.getSessionUIdSet();

        if (sessionUIdSet == null ||
            sessionUIdSet.isEmpty()) {
            // 如果会话 UId 集合为空,
            // 则直接退出!
            return null;
        }

        return sessionUIdSet.stream().map(sessionUId -> {
            // 根据会话 UId 获取玩家对象
            Player p = IoSessionManager.OBJ.getPlayerBySessionUId(sessionUId);

            if (p == null) {
                // 如果玩家对象为空,
                // 则直接退出!
                return null;
            }

            // 获取角色对象
            return Human.getHumanByPlayer(p);
        }).filter(h -> h != null).collect(Collectors.toList());
    }
}
