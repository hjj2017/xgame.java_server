package com.game.passportServer.http.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.game.part.dao.CommDao;
import com.game.passportServer.ServerLog;
import com.game.passportServer.entity.PassportEntity_X;

/**
 * 获取 Passport 信息
 * 
 * @author hjj2019
 * @since 2015/2/9
 * 
 */
public class Servlet_GetPassportInfo extends HttpServlet {
	/// serialVersionUID
	private static final long serialVersionUID = 8875654284161116765L;
	/** 锁字典 */
	private static final ConcurrentHashMap<String, ReentrantLock> _lockMap = new ConcurrentHashMap<>();

	@Override
	protected void doPost(
		final HttpServletRequest req, final HttpServletResponse res) 
		throws ServletException, IOException {
		this.doGet(req, res);
	}

	@Override
	protected void doGet(
		final HttpServletRequest req, final HttpServletResponse res) 
		throws ServletException, IOException {
		if (req == null || 
			res == null) {
			// 如果参数对象为空, 
			// 则直接退出!
			return;
		}

		res.setContentType("application/json; charset=utf-8");
		res.setStatus(HttpServletResponse.SC_OK);

		// 获取平台 UUId
		String platformUUId = req.getParameter("platform_uuid");
		// 获取平台 Pf 值
		String pf = req.getParameter("pf");
		// 获取游戏服 Id
		int gameServerId = Integer.parseInt(req.getParameter("game_server_id") != null ? req.getParameter("game_server_id") : "-1");

		// 记录日志信息
		ServerLog.LOG.info(MessageFormat.format(
			"接到请求 : platform_uuid = {0}, pf = {1}, game_server_id = {2}", 
			platformUUId, pf, 
			String.valueOf(gameServerId)
		));

		// 输出结果
		res.getWriter().println(
			this.doAction(platformUUId, pf, gameServerId
		));
	}

	/**
	 * 执行动作
	 * 
	 * @param platformUUId
	 * @param pf
	 * @param gameServerId
	 * @return
	 * 
	 */
	private String doAction(String platformUUId, String pf, int gameServerId) {
		if (platformUUId == null || 
			platformUUId.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return "null_platform_uuid";
		}
		
		// 创建互斥锁
		ReentrantLock newLock = new ReentrantLock(true);
		// 获取老锁
		ReentrantLock oldLock = _lockMap.putIfAbsent(platformUUId, newLock);

		if (oldLock != null) {
			// 如果老锁不为空, 
			// 则直接指向老锁 ...
			ServerLog.LOG.warn(MessageFormat.format(
				"将引用指向旧锁, platformUUId = {0}", 
				platformUUId
			));
			newLock = oldLock;
		}

		try {
			// 尝试锁定 5 秒
			boolean lockFlag = newLock.tryLock(5, TimeUnit.SECONDS);

			if (!lockFlag) {
				// 如果加锁失败, 
				// 则直接退出!
				ServerLog.LOG.error(MessageFormat.format(
					"加锁失败, platformUUId = {0}", 
					platformUUId
				));

				return "lock_error";
			}

			// 根据 platformUUId 加锁
			ServerLog.LOG.error(MessageFormat.format(
				"加锁成功, platformUUId = {0}", 
				platformUUId
			));

			// 获取 passport 数据
			PassportEntity_X pe = this.getPassportEntityAndUpdate(
				platformUUId, pf, gameServerId
			);

			if (pe == null) {
				// 如果 passport 数据依然为空, 
				// 则直接退出!
				ServerLog.LOG.error(MessageFormat.format(
					"passport 数据为空, platformUUId = {0}", 
					platformUUId
				));

				return "null_passport_entity";
			}

			// 创建并写出 JSON 对象
			JSONObject jsonObj = new JSONObject();
			pe.writeJsonObj(jsonObj);
			// 获取 JSON 字符串
			String jsonStr = jsonObj.toString();
			// 记录日志信息
			ServerLog.LOG.info(MessageFormat.format(
				"准备返回给调用者, jsonStr = {0}", 
				jsonStr
			));

			return jsonStr;
		} catch (Exception ex) {
			// 记录异常信息
			ServerLog.LOG.error(MessageFormat.format(
				"加锁时发生异常, platformUUId = {0}", 
				platformUUId
			), ex);
		} finally {
			// 给玩家解锁
			newLock.unlock();
			_lockMap.remove(platformUUId);
			// 记录日志信息
			ServerLog.LOG.info(MessageFormat.format(
				"给玩家解锁, platformUUId = {0}", 
				platformUUId
			));
		}

		return "error";
	}

	/**
	 * 获取 passport 实体并保存
	 * 
	 * @param platformUUId
	 * @param pf
	 * @param gameServerId
	 * @return
	 * 
	 */
	private PassportEntity_X getPassportEntityAndUpdate(
		String platformUUId, 
		String pf, 
		int gameServerId) {

		if (platformUUId == null || 
			platformUUId.isEmpty()) {
			// 如果参数对象为空, 
			// 则直接退出!
			return null;
		}

		// 获取 passport 数据
		PassportEntity_X pe = CommDao.OBJ.find(
			PassportEntity_X.getSplitEntityClazz(platformUUId), 
			platformUUId
		);
		
		// 获取当前时间
		long now = System.currentTimeMillis();

		if (pe == null) {
			// 如果 passport 数据为空, 
			ServerLog.LOG.warn(
				"passport 数据为空需要新建, platformUUId = {0}", 
				platformUUId
			);

			pe = new PassportEntity_X();
			pe._platformUUId = platformUUId;
			pe._createTime = now;
			pe._pf = pf;
		}

		// 设置最后登录时间和最后服务器 Id
		pe._lastLoginTime = now;
		pe._lastGameServerId = gameServerId;
		// 保存数据
		CommDao.OBJ.save(pe.getSplitEntityObj());

		return pe;
	}
}

