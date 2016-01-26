package com.game.bizModule.hero.bizServ;

import com.game.bizModule.hero.entity.HeroEntity_X;
import com.game.bizModule.hero.model.Hero;
import com.game.bizModule.human.Human;
import com.game.bizModule.human.HumanStateTable;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.bizModule.time.TimeServ;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 武将服务
 *
 * @author hjj2017
 * @since 2016/1/16
 *
 */
public class HeroServ extends AbstractBizServ implements IHumanEventListen {
    /** 单例对象 */
    public static final HeroServ OBJ = new HeroServ();
    private static final ConcurrentHashMap<Long, HeroManager> _mngrMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private HeroServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        HumanEvent.OBJ.regEventListen(this);
    }

    @Override
    public void onCreateNew(Player byPlayer, final HumanEntity he) {
        if (byPlayer == null ||
            he == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 创建主将
        HeroEntity_X newEntity = new HeroEntity_X();

        newEntity._UIdStr = "UId";
        newEntity._humanUId = he._humanUId;
        newEntity._exp = -1;
        newEntity._heroLevel = -1;
        newEntity._commanderFlag = 1;
        newEntity._hireTime = TimeServ.OBJ.now();
        newEntity._tmplId = he._heroTmplId;

        newEntity._mAttkAdd = 0;
        newEntity._pAttkAdd = 0;
        newEntity._mDefnAdd = 0;
        newEntity._pDefnAdd = 0;
        newEntity._hpAdd = 0;

        // 保存到数据库
        CommDao.OBJ.save(newEntity.getSplitEntityObj());
    }

    @Override
    public void onLoadDb(Player byPlayer, Human h) {
        if (byPlayer == null ||
            h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取角色状态表
        HumanStateTable hStatTbl = h.getPropValOrCreate(HumanStateTable.class);

        if (hStatTbl._firstLogin) {
            // 如果角色是第一次登陆,
            // 则直接退出!
            return;
        }

        List<? extends HeroEntity_X> hel = CommDao.OBJ.getResultList(
            HeroEntity_X.getSplitEntityClazz(h._humanUId),
            "entity.human_uid = " + h._humanUId
        );

        if (hel == null) {
            // 如果实体列表为空,
            // 则直接退出!
            return;
        }

        // 初始化武将
        hel.forEach(he -> {
            Hero heroObj = new Hero(h._humanUId);
            heroObj.fromEntity(he);

            HeroManager mngrObj = _mngrMap.get(he._humanUId);

            if (mngrObj == null) {
                mngrObj = new HeroManager();
            }

            mngrObj._heroMap.put(heroObj._UIdStr, heroObj);
        });
    }
}
