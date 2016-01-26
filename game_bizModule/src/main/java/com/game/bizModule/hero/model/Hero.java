package com.game.bizModule.hero.model;

import com.game.bizModule.hero.entity.HeroEntity_X;
import com.game.bizModule.human.AbstractHumanBelonging;

/**
 * 武将
 *
 * @author hjj2017
 * @since 2016/1/17
 *
 */
public class Hero extends AbstractHumanBelonging<HeroEntity_X> {
    /**
     * 类参数构造器
     *
     * @param humanUId
     *
     */
    public Hero(long humanUId) {
        super(humanUId);
    }

    @Override
    public HeroEntity_X toEntity() {
        return null;
    }
}
