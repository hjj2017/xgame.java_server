package com.game.bizModule.hero.entity;

import com.game.bizModule.cd.entity.*;

import javax.persistence.*;

/**
 * 武将实体
 *
 * @author hjj2017
 * @since 2016/1/16
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public class HeroEntity_X {
    /** UId 字符串 */
    @Id @Column(name = "uid_str")
    public String _UIdStr;

    /** 所属角色 UId */
    @Column(name = "human_uid")
    public Long _humanUId;

    /** 模版 Id */
    @Column(name = "tmpl_id")
    public Integer _tmplId;

    /** 武将等级 */
    @Column(name = "level")
    public Integer _level;

    /** 指挥官标志 */
    @Column(name = "commander_flag")
    public Integer _commanderFlag;

    /** 招募时间 */
    @Column(name = "hire_time")
    public Long _hireTime;

// 属性加成
/////////////////////////////////////////////////////////////////////////////

    /** 物理攻击加成值 */
    @Column(name = "p_attk_add")
    public Integer _pAttkAdd;

    /** 战术攻击加成值 */
    @Column(name = "m_attk_add")
    public Integer _mAttkAdd;

    /** 物理防御加成值 */
    @Column(name = "p_defn_add")
    public Integer _pDefnAdd;

    /** 战术防御加成值 */
    @Column(name = "m_defn_add")
    public Integer _mDefnAdd;

    /** 血量加成值 */
    @Column(name = "hp_add")
    public Integer _hpAdd;

    /**
     * 获取分表数据实体
     *
     * @return
     *
     */
    public HeroEntity_X getSplitEntityObj() {
        if (this._humanUId == null) {
            this._humanUId = 0L;
        }

        HeroEntity_X splitEntity = null;
        // 获取临时的 UId
        final int tmpUId = (int)(this._humanUId % 10L);

        switch (tmpUId) {
            // 10 进制数
            case 0: splitEntity = new HeroEntity_0(); break;
            case 1: splitEntity = new HeroEntity_1(); break;
            case 2: splitEntity = new HeroEntity_2(); break;
            case 3: splitEntity = new HeroEntity_3(); break;
            case 4: splitEntity = new HeroEntity_4(); break;
            case 5: splitEntity = new HeroEntity_5(); break;
            case 6: splitEntity = new HeroEntity_6(); break;
            case 7: splitEntity = new HeroEntity_7(); break;
            case 8: splitEntity = new HeroEntity_8(); break;
            case 9: splitEntity = new HeroEntity_9(); break;
            default: return null;
        }

        splitEntity._UIdStr = this._UIdStr;
        splitEntity._humanUId = this._humanUId;
        splitEntity._tmplId = this._tmplId;
        splitEntity._level = this._level;
        splitEntity._commanderFlag = this._commanderFlag;
        splitEntity._hireTime = this._hireTime;

        splitEntity._pAttkAdd = this._pAttkAdd;
        splitEntity._mAttkAdd = this._mAttkAdd;
        splitEntity._pDefnAdd = this._pDefnAdd;
        splitEntity._mDefnAdd = this._mDefnAdd;
        splitEntity._hpAdd = this._hpAdd;

        return splitEntity;
    }

    /**
     * 获取分表实体类
     *
     * @param humanUId
     * @return
     *
     */
    public static Class<? extends HeroEntity_X> getSplitEntityClazz(long humanUId) {
        // 获取临时的 UId
        final int tmpUId = (int)(humanUId % 10L);

        switch (tmpUId) {
            // 10 进制数
            case 0: return HeroEntity_0.class;
            case 1: return HeroEntity_1.class;
            case 2: return HeroEntity_2.class;
            case 3: return HeroEntity_3.class;
            case 4: return HeroEntity_4.class;
            case 5: return HeroEntity_5.class;
            case 6: return HeroEntity_6.class;
            case 7: return HeroEntity_7.class;
            case 8: return HeroEntity_8.class;
            case 9: return HeroEntity_9.class;
            default: return null;
        }
    }
}
