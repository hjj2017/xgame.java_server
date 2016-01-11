package com.game.bizModule.cd.bizServ;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.game.bizModule.cd.CdLog;
import com.game.bizModule.cd.entity.CdTimerEntity_X;
import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.cd.tmpl.CdTimerTmpl;
import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.HumanEvent;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.bizModule.time.TimeServ;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;
import com.game.part.util.Assert;

/**
 * 冷却队列服务
 * 
 * @author haijiang.jin
 * 
 */
public final class CdServ extends AbstractBizServ implements IHumanEventListen, IServ_CanAddTime, IServ_DoAddTime, IServ_FindAndDoAddTime {
    /** 单例对象 */
    public static final CdServ OBJ = new CdServ();
    /** 管理器字典 */
    public final ConcurrentHashMap<Long, CdManager> _mngrMap = new ConcurrentHashMap<>();

    /**
     * 类默认构造器
     *
     */
    private CdServ() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        HumanEvent.OBJ.regEventListen(this);
    }

    @Override
    public void onLoadDb(Player p, Human h) {
        if (p == null ||
            h == null) {
            // 如果参数对象为空,
            // 则直接退出!
            return;
        }

        // 获取管理器对象
        CdManager mngrObj = this._mngrMap.get(h._humanUId);

        if (mngrObj == null) {
            mngrObj = new CdManager(h._humanUId);
            // 添加到字典
            CdManager oldMngr = this._mngrMap.putIfAbsent(
                h._humanUId, mngrObj
            );

            if (oldMngr != null) {
                CdLog.LOG.warn(MessageFormat.format(
                    "Cd 管理器不为空, 角色 = {0}",
                    String.valueOf(h._humanUId)
                ));
                mngrObj = oldMngr;
            }
        }

        for (CdTypeEnum typeEnum : CdTypeEnum.values()) {
            // 获取 Cd 计时器模板
            CdTimerTmpl tmplObj = CdTimerTmpl.getByCdType(typeEnum);
            // 创建计时器
            CdTimer timerObj = new CdTimer(
                h._humanUId,
                typeEnum,
                tmplObj._defaultOpen.getBoolVal()
            );
            // 添加到字典
            mngrObj._cdTimerMap.put(typeEnum, timerObj);
        }

        // 获取 Cd 计时器列表
        List<? extends CdTimerEntity_X> el = CommDao.OBJ.getResultList(
            // 注意: 这里是从分表里读取数据的...
            CdTimerEntity_X.getSplitEntityClazz(h._humanUId),
            // 设置 SQL 查询条件
            "entity._humanUId = " + h._humanUId
        );

        if (el != null && 
            el.isEmpty() == false) {
            for (CdTimerEntity_X e : el) {
                // 获取计时器对象
                CdTimer timerObj = mngrObj._cdTimerMap.get(CdTypeEnum.parse(e._cdTypeInt));

                if (timerObj == null) {
                    // 如果计时器对象为空,
                    // 这说明 Excel 配置表里没有该类型的 Cd,
                    // 但是数据库里还有...
                    // 可能策划改表了!
                    // 这时候直接跳过就可以了
                    CdLog.LOG.warn(MessageFormat.format(
                        "Cd 类型 {0} 已经被取消",
                        String.valueOf(e._cdTypeInt)
                    ));
                    continue;
                }

                // 加载数据
                timerObj.fromEntity(e);
            }
        }
    }

    /**
     * 检查计时器是否已过期并重置
     * 
     * @param t
     * @return
     * 
     */
    boolean checkExpiredAndReset(CdTimer t) {
        // 断言参数不为空
        Assert.notNull(t, "t");

        if (t._endTime <= TimeServ.OBJ.now()) {
            // 如果定时器已过期, 
            // 即, 结束时间 <= 当前时间, 
            // 则重置计时器!
            t._startTime = 0L;
            t._endTime = 0L;
            return true;
        } else {
            // 如果定时器还没过期, 
            // 则直接退出!
            return false;
        }
    }
}
