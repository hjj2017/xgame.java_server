package com.game.bizModule.cd.serv;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.game.bizModule.cd.CdLog;
import com.game.bizModule.cd.entity.CdTimerEntity;
import com.game.bizModule.cd.model.CdTimer;
import com.game.bizModule.cd.model.CdTypeEnum;
import com.game.bizModule.human.Human;
import com.game.bizModule.human.event.IHumanEventListen;
import com.game.part.util.Assert;

/**
 * 冷却队列服务
 * 
 * @author haijiang.jin
 * 
 */
public final class CdServ implements IHumanEventListen, IServ_CanAddTime, IServ_DoAddTime, IServ_FindAndDoAddTime, IServ_KillCdTime {
	/** 单例对象 */
	public static final CdServ OBJ = new CdServ();
	/** 管理器字典 */
	final ConcurrentHashMap<Long, CdManager> _mngrMap = new ConcurrentHashMap<>();

	/**
	 * 类默认构造器
	 * 
	 */
	private CdServ() {
	}

	@Override
	public void onLoadDb(Human h) {
		if (h == null) {
			return;
		}

		// 获取管理器对象
		CdManager mngr = this._mngrMap.get(h._humanUId);

		if (mngr == null) {
			mngr = new CdManager(h._humanUId);
			// 添加到字典
			CdManager orig = this._mngrMap.putIfAbsent(
				h._humanUId, mngr
			);

			if (orig != null) {
				CdLog.LOG.warn("Cd 管理器不为空, 角色 = " + h._humanUId);
				mngr = orig;
			}
		}

		// 获取 Cd 计时器列表
		List<CdTimerEntity> el = null;

		if (el != null && 
			el.isEmpty() == false) {
			// 定义临时字典
			Map<CdTypeEnum, CdTimer> tmpMap = null;
			// 将实体对象转换为业务对象, 
			// 并添加到字典!
			tmpMap = el.stream()
				.map(e -> new CdTimer(
					h._humanUId,
					CdTypeEnum.parse(e._cdTypeInt), 
					e._startTime, 
					e._endTime
				)).collect(Collectors.toMap(
					t -> t._cdType, t -> t
				));
	
			// 添加到管理器字典
			mngr._cdMap.putAll(tmpMap);
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 
	 * 
	 */
	public long getCurrTime() {
		return 0L;
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

		if (t._endTime <= this.getCurrTime()) {
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
