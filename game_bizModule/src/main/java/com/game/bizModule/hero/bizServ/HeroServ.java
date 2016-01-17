package com.game.bizModule.hero.bizServ;

import com.game.bizModule.hero.entity.HeroEntity_X;
import com.game.bizModule.human.entity.HumanEntity;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.bizModule.time.TimeServ;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;

/**
 * 武将服务
 *
 * @author hjj2017
 * @since 2016/1/16
 *
 */
public class HeroServ implements IHumanEventListen {
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
        newEntity._level = 1;
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
}
