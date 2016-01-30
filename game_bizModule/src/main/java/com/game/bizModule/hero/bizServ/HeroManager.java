package com.game.bizModule.hero.bizServ;

import com.game.bizModule.hero.model.Hero;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 武将管理器
 *
 * @author hjj2017
 * @since 2016/1/17
 *
 */
public class HeroManager {
    /** 武将字典 */
    public final Map<String, Hero> _heroMap = new ConcurrentHashMap<>();
}
