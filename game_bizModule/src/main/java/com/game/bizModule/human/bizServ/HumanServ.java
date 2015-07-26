package com.game.bizModule.human.bizServ;

import com.game.bizModule.human.entity.HumanEntryEntity;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.part.dao.CommDao;

import java.util.List;

/**
 * 玩家角色服务
 *
 * @author hjj2019
 * @since 2015/7/11
 *
 */
public class HumanServ extends AbstractBizServ implements IServ_QueryHumanEntryList, IServ_CreateHuman, IServ_EnterHuman {
    /** 单例对象 */
    public static final HumanServ OBJ = new HumanServ();

    /**
     * 类默认构造器
     *
     */
    private HumanServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        // 获取角色入口列表
        List<HumanEntryEntity> heel = CommDao.OBJ.getResultList(HumanEntryEntity.class);
        // 添加角色名称到集合
        heel.forEach(hee -> {
            HumanNaming.OBJ._fullNameSet.add(hee._fullName);
        });
    }
}
