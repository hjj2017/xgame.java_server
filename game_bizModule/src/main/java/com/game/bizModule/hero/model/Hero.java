package com.game.bizModule.hero.model;

import com.game.bizModule.hero.entity.HeroEntity_X;
import com.game.bizModule.human.AbstractHumanBelonging;
import com.game.bizModule.human.Human;

/**
 * 武将
 *
 * @author hjj2017
 * @since 2016/1/17
 *
 */
public class Hero extends AbstractHumanBelonging<HeroEntity_X> {
    /** UId 字符串 */
    public String _UIdStr;
    /** 所属玩家角色 */
    public final Human _owner;
    /** 模版 Id */
    public int _tmplId;
    /** 招募时间 */
    public long _hireTime;
    /** 经验值 */
    public int _exp;
    /** 武将等级 */
    public int _heroLevel;
    /** 武将星级 */
    public int _starLevel;

// 属性加成
/////////////////////////////////////////////////////////////////////////////

    /** 物理攻击加成值 */
    public int _pAttkAdd;
    /** 战术攻击加成值 */
    public int _mAttkAdd;
    /** 物理防御加成值 */
    public int _pDefnAdd;
    /** 战术防御加成值 */
    public int _mDefnAdd;
    /** 血量加成值 */
    public int _hpAdd;

    /**
     * 类参数构造器
     *
     * @param owner
     *
     */
    public Hero(Human owner) {
        super(owner == null ? -1 : owner._humanUId);
        this._owner = owner;
    }

    /**
     * 获取武将经验
     *
     * @return
     *
     */
    public int getExp() {
        if (this.isTotalCommander()) {
            return this._owner._exp;
        } else {
            return this._exp;
        }
    }

    /**
     * 获取武将等级
     *
     * @return
     *
     */
    public int getHeroLevel() {
        if (this.isTotalCommander()) {
            return this._owner._humanLevel;
        } else {
            return this._heroLevel;
        }
    }

    @Override
    public HeroEntity_X toEntity() {
        // 创建实体对象
        HeroEntity_X entity = new HeroEntity_X();

        entity._exp = this.getExp();
        entity._heroLevel = this.getHeroLevel();
        entity._hpAdd = this._hpAdd;
        entity._humanUId = this._humanUId;
        entity._mAttkAdd = this._mAttkAdd;
        entity._mDefnAdd = this._mDefnAdd;
        entity._pAttkAdd = this._pAttkAdd;
        entity._pDefnAdd = this._pDefnAdd;
        entity._starLevel = this._starLevel;
        entity._tmplId = this._tmplId;
        entity._UIdStr = this._UIdStr;

        return entity.getSplitEntityObj();
    }

    /**
     * 从实体中加载数据
     *
     * @param entity
     *
     */
    public void fromEntity(HeroEntity_X entity) {
        if (entity == null) {
            return;
        }

        this._exp = entity._exp;
        this._heroLevel = entity._heroLevel;
        this._hpAdd = entity._hpAdd;
        this._mAttkAdd = entity._mAttkAdd;
        this._mDefnAdd = entity._mDefnAdd;
        this._pAttkAdd = entity._pAttkAdd;
        this._pDefnAdd = entity._pDefnAdd;
        this._starLevel = entity._starLevel;
        this._tmplId = entity._tmplId;
        this._UIdStr = entity._UIdStr;
    }

    /**
     * 是不是总指挥官?
     *
     * @return
     *
     */
    public boolean isTotalCommander() {
        if (this._owner == null) {
            return false;
        } else {
            return this._owner._heroTmplId == this._tmplId;
        }
    }
}
