package com.game.part.lazySaving;

import java.text.MessageFormat;

import com.game.part.dao.CommDao;
import com.game.part.io.IIoOper;
import com.game.part.util.Assert;

/**
 * 通用的 LSO 更新器
 * 
 * @author hjj2017
 * @since 2015/3/31
 * 
 */
class CommUpdater {
	/** 通用的更新器 */
	static final CommUpdater OBJ = new CommUpdater();

	/**
	 * 类默认构造器
	 * 
	 */
	private CommUpdater() {
	}

	/**
	 * 保存或者更新业务对象
	 * 
	 * @param lso 
	 * 
	 */
	void saveOrUpdate(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取数据库实体
		final Object entity = lso.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			LazySavingLog.LOG.error(MessageFormat.format(
				"无法保存实体数据, 实体对象为空! UId = {0}", 
				lso.getUId()
			));
			return;
		}

		if (LazySavingHelper.OBJ._ioServ == null) {
			// 
			// 如果没有设置 IO 服务, 
			// 则直接在当前线程执行保存/更新操作...
			// 注意 : 这会阻塞当前线程!
			LazySavingLog.LOG.warn(MessageFormat.format(
				"没有设置 IO 服务, 在当前线程 {0} 中执行保存/更新操作! 业务对象 UId = {1}", 
				Thread.currentThread().getName(), 
				lso.getUId()
			));
			CommDao.OBJ.save(entity);
		} else {
			// 通过 IO 服务执行保存操作
			LazySavingHelper.OBJ._ioServ.execute(new IIoOper() {
				@Override
				public boolean doIo() {
					// 保存到数据库
					CommDao.OBJ.save(entity);
					return true;
				}
			}, this.getThreadEnum(lso));
		}
	}

	/**
	 * 获取线程枚举
	 * 
	 * @param lso
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	private <T extends Enum<T>> T getThreadEnum(ILazySavingObj<?, ?> lso) {
		// 断言参数不为空
		Assert.notNull(lso, "lso");
		// 获取线程枚举
		Object threadEnum = lso.getThreadEnum();
		// 将枚举值转型
		return (T)threadEnum;
	}

	/**
	 * 删除业务对象
	 * 
	 * @param lso 
	 * 
	 */
	void del(ILazySavingObj<?, ?> lso) {
		if (lso == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		// 获取数据库实体
		Object entity = lso.toEntity();

		if (entity == null) {
			// 如果实体数据为空, 
			// 则直接退出!
			LazySavingLog.LOG.error(MessageFormat.format(
				"无法删除实体数据, 实体对象为空! UId = {0}", 
				lso.getUId()
			));
			return;
		}

		if (LazySavingHelper.OBJ._ioServ == null) {
			// 
			// 如果没有设置 IO 服务, 
			// 则直接在当前线程执行删除操作...
			// 注意 : 这会阻塞当前线程!
			LazySavingLog.LOG.warn(MessageFormat.format(
				"没有设置 IO 服务, 在当前线程 {0} 中执行删除操作! 业务对象 UId = {1}", 
				Thread.currentThread().getName(), 
				lso.getUId()
			));
			CommDao.OBJ.del(entity);
		} else {
			// 
			// 如果已经设置了 IO 服务, 
			// 那么通过 IO 服务执行保存操作...
			LazySavingHelper.OBJ._ioServ.execute(new IIoOper() {
				@Override
				public boolean doIo() {
					// 从数据库中删除
					CommDao.OBJ.del(entity);
					return true;
				}
			}, this.getThreadEnum(lso));
		}
	}
}
