package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;
import java.util.Random;

import com.game.gameServer.player.msg.CGCreateRole;
import com.game.gameServer.player.msg.GCRoleTemplate;
import com.game.robot.RobotLog;
import com.game.robot.kernal.AbstractGCMsgHandler;
import com.game.robot.kernal.Robot;

/**
 * 接收 GC 消息
 * 
 * @author hjj2019
 * @since 2015/5/15
 * 
 */
public class S030_GCRoleTemplate extends AbstractGCMsgHandler<GCRoleTemplate> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCRoleTemplate msgObj) {

		// 随机一个角色名称
		final String humanName = randomHumanName(robotObj);
		// 随机一个使用模板
		final int useTmplId = randomTmplId();

		// 记录日志信息
		RobotLog.LOG.info(MessageFormat.format(
			"玩家 {0} 创建新角色! humanName = {1}, useTmplId = {2}", 
			robotObj._userName, 
			humanName, 
			String.valueOf(useTmplId)
			
		));
		// 发送建角消息
		robotObj.sendMsg(new CGCreateRole(
			humanName, useTmplId
		));
	}

	@Override
	protected Class<GCRoleTemplate> getGCMsgClazzDef() {
		return GCRoleTemplate.class;
	}

	/**
	 * 创建角色名称
	 * 
	 * @param robotObj
	 * @return
	 * 
	 */
	private static String randomHumanName(
		Robot robotObj) {
		// 随机对象
		final Random R = new Random();
		// 名字字符串
		final String S = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			// 随机一个索引位置
			int I = R.nextInt(S.length() - 1);
			// 添加字符
			sb.append(S.charAt(I));
		}

		return "R-" + sb.toString();
	}

	/**
	 * 随机一个模板 Id
	 * 
	 * @return
	 * 
	 */
	private static int randomTmplId() {
		// 随机对象
		final Random R = new Random();

		if (R.nextBoolean()) {
			return 1000;
		} else {
			return 1001;
		}
	}
}
