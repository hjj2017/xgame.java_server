package com.game.bizModule.login.bizServ.auth;

import java.text.MessageFormat;

import com.game.bizModule.login.LoginLog;
import com.game.bizModule.player.entity.PlayerEntity;
import com.game.gameServer.framework.Player;
import com.game.part.dao.CommDao;
import com.game.part.util.NullUtil;
import com.game.part.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 根据用户名和密码验证, 用户名密码一般是存在数据库中
 * 
 * @author hjj2019
 * @since 2014/9/15 
 * 
 */
public class Auth_ByDbUser implements IAuthorize {
	/** 用户名 */
	private static final String JK_userName = "userName";
	/** 密码 */
	private static final String JK_password = "password";

	@Override
	public boolean auth(Player p, String loginStr) {
		if (p == null ||
			loginStr == null ||
			loginStr.isEmpty()) {
			return false;
		}

		// 创建 JSON 对象
		JSONObject jsonObj = JSONObject.fromObject(loginStr);
		// 获取用户名和密码
		final String userName = jsonObj.optString(JK_userName, "");
		final String passwd = jsonObj.optString(JK_password, "");

		if (userName == null || 
			userName.isEmpty() || 
			passwd == null || 
			passwd.isEmpty()) {
			// 如果用户名或者密码为空, 
			// 则直接退出!
			return false;
		}

		// 获取用户实体
		PlayerEntity pe = CommDao.OBJ.getSingleResult(
			PlayerEntity.class,
			"entity._userName = '" + StringUtil.addSlash(userName) +"'"
		);

		if (pe == null ||
			passwd.equals(pe._userPass) == false) {
			// 如果数据实体为空或者密码不正确,
			// 则直接退出!
			LoginLog.LOG.error(MessageFormat.format(
				"用户不存在或者密码不正确! userName = {0}", userName
			));
			return false;
		}

		// 更新用户实体
		CommDao.OBJ.save(pe);

		// 更新玩家对象
		p._userName = pe._userName;
		p._pf = NullUtil.optVal(p._pf, "WEB");
		p._createTime = NullUtil.optVal(pe._createTime, 0L);

		return true;
	}
}
