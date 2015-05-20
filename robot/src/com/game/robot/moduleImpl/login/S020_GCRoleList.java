package com.game.robot.moduleImpl.login;

import java.text.MessageFormat;

import com.game.gameServer.necessary.msg.GCRoleList;
import com.game.gameServer.player.RoleInfo;
import com.game.gameServer.player.msg.CGPlayerEnter;
import com.game.gameServer.player.msg.CGRoleTemplate;
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
public class S020_GCRoleList extends AbstractGCMsgHandler<GCRoleList> {
	@Override
	public void handleGCMsg(
		Robot robotObj, GCRoleList msgObj) {
		// 获取角色信息数组
		RoleInfo[] roleInfoArr = msgObj.getRoleList();

		if (roleInfoArr == null || 
			roleInfoArr.length <= 0) {
			// 需要新建角色!
			RobotLog.LOG.warn(MessageFormat.format(
				"玩家 {0} 没有角色, 需要新建角色", 
				robotObj._userName
			));
			// 加载角色模板
			robotObj.sendMsg(new CGRoleTemplate());
			return;
		}

		// 选定第一个角色进入游戏
		RoleInfo selectedRole = roleInfoArr[0];

		if (selectedRole == null) {
			// 如果选择的角色为空, 
			// 那还是直接断开吧!
			RobotLog.LOG.error(MessageFormat.format(
				"玩家 {0} 角色数组中有空数据, 需要检查服务器端消息或者数据库", 
				robotObj._userName
			));
			robotObj.disconnect();
		}

		// 获取角色名称
		final String roleName = selectedRole.getOrigName();
		// 获取角色 UUId
		final long roleUUId = selectedRole.getRoleUUID();

		// 记录日志信息
		RobotLog.LOG.error(MessageFormat.format(
			"玩家 {0} 选定角色, roleName = {1}, roleUUId = {2}", 
			robotObj._userName, 
			roleName, 
			String.valueOf(roleUUId)
		));

		// 发送 CG 消息
		robotObj.sendMsg(new CGPlayerEnter(roleUUId));
	}

	@Override
	protected Class<GCRoleList> getGCMsgClazzDef() {
		return GCRoleList.class;
	}
}
