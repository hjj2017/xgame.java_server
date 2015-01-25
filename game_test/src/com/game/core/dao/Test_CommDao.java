package com.game.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Persistence;

import org.junit.Test;

import com.game.bizModules.login.entity.UserEntity;
import com.game.part.dao.CommDao;
import com.game.part.dao.DaoLog;

/**
 * 测试登陆 DAO
 * 
 * @author hjj2019
 * @since 2014/9/19 
 * 
 */
public class Test_CommDao {
	/** 是否已初始化 ? */
	private static boolean _inited = false;

	/**
	 * 初始化
	 * 
	 */
	private static void init() {
		if (_inited) {
			// 如果已经初始化完成, 
			// 则直接退出!
			return;
		}

		// 设置实体管理器工厂
		CommDao.OBJ.putEMF(Persistence.createEntityManagerFactory("llz"));
		_inited = true;
	}

	@Test
	public void testAll() {
		init();

		this.save();
		this.getSingleResult();
		this.del();
	}

	// 保存数据
	public void save() {
		UserEntity ue = new UserEntity();

		ue._passportId = 1001L;
		ue._userName = "1001";
		ue._passwd = "1";
		// 插入一条新数据
		CommDao.OBJ.save(ue);

		List<UserEntity> uel = new ArrayList<>();

		for (int i = 1002; i <= 1005; i++) {
			ue = new UserEntity();
			ue._passportId = (long)i;
			ue._userName = String.valueOf(i);
			ue._passwd = "1";
			uel.add(ue);
		}

		// 插入一堆新数据
		CommDao.OBJ.saveAll(uel);

		ue = CommDao.OBJ.find(UserEntity.class, 1004L);
		ue._userName = "1004.01";

		// 更新一条数据
		CommDao.OBJ.save(ue);
	}

	// 获取单个数据
	public void getSingleResult() {
		// 获取用户实体
		UserEntity ue = CommDao.OBJ.getSingleResult(
			UserEntity.class
		);
 
		if (ue != null) {
			DaoLog.LOG.info("找到了 : " + ue._userName);
		}

		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userName", "1001");

		ue = CommDao.OBJ.getSingleResult(
			UserEntity.class, 
			"obj._userName = :userName", 
			paramsMap
		);

		if (ue != null) {
			DaoLog.LOG.info("找到了 : " + ue._userName);
		}
	}

	// 删除数据
	public void del() {
		// 删除实体
		CommDao.OBJ.del(UserEntity.class, 1001L);

		List<Long> idList = new ArrayList<>();
		idList.add(1002L);
		idList.add(1003L);
		idList.add(1004L);
		idList.add(1005L);
		// 删除列表数据
		CommDao.OBJ.delAll(UserEntity.class, idList);
	}
}
