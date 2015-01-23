package com.game.gameServer.framework;

import java.util.List;

import com.game.core.utils.Assert;
import com.game.gameServer.io.AbstractBattleIoOper;
import com.game.gameServer.io.AbstractBindUUIdIoOper;
import com.game.gameServer.io.AbstractPlatformIoOper;
import com.game.gameServer.io.IBindUUIDIoOper;
import com.game.gameServer.io.IoOperThreadEnum;
import com.game.gameServer.io.IoOperThreadEnum.GroupEnum;

/**
 * 可执行 IO 操作的接口
 * 
 * @author hjj2019
 * @since 2014/9/15
 * 
 */
interface IIoOperExecutable {
	/**
	 * 执行 IO 操作
	 * 
	 * @param oper 
	 * @param group 
	 * 
	 */
	static void execute(IBindUUIDIoOper oper, GroupEnum group) {
		// 断言参数不为空
		Assert.notNull(oper);
		// 获取 UUID
		long uuid = oper.getBindUUId();

		if (uuid <= -1) {
			FrameworkLog.LOG.warn("UUID 竟然为负数");
			uuid = Math.abs(uuid);
		}

		// 获取枚举列表
		List<IoOperThreadEnum> enumList = IoOperThreadEnum.getEnumList(group);

		if (enumList == null || 
			enumList.isEmpty()) {
			// 如果枚举数组为空, 
			// 则直接退出!
			FrameworkLog.LOG.error("IO 操作线程枚举列表为空");
			return;
		}

		// 获取数组长度
		int listSize = enumList.size();
		// 取一个枚举值
		IoOperThreadEnum $enum = enumList.get((int)(uuid % listSize));
		// 执行异步操作
		App_GameServer.OBJ.getIoOperServ().execute(
			oper, $enum
		);
	}

	/**
	 * 执行 IO 操作
	 * 
	 * @param oper 
	 * 
	 */
	default void execute(AbstractBindUUIdIoOper oper) {
		execute(
			oper, GroupEnum.bind
		);
	}

	/**
	 * 执行战斗 IO 操作
	 * 
	 * @param oper 
	 * 
	 */
	default void execute(AbstractBattleIoOper oper) {
		execute(
			oper, GroupEnum.battle
		);
	}

	/**
	 * 执行登陆 IO 操作
	 * 
	 * @param oper 
	 * 
	 */
	default void execute(AbstractPlatformIoOper oper) {
		execute(
			oper, GroupEnum.login
		);
	}
}
