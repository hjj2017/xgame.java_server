package com.game.passportServer.restful.servlet;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import com.game.passportServer.dao.CommDao;
import com.game.passportServer.entity.PassportEntity_X;
import com.game.passportServer.restful.RestfulLog;

/**
 * 获取 Passport 信息
 * 
 * @author hjj2019
 * @since 2015/2/9
 * 
 */
@Path(value = "/get_passport_info")
public class Servlet_GetPassportInfo {
	/** 锁字典 */
	private static final ConcurrentHashMap<String, ReentrantLock> _lockMap = new ConcurrentHashMap<>();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public String doPost(
		@FormParam("platform_uuid") String platformUUId, 
		@FormParam("pf") String pf, 
		@FormParam("game_server_id") int gameServerId) {
		return this.doGet(
			platformUUId, pf, gameServerId
		);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public String doGet(
		@QueryParam("platform_uuid") String platformUUId,
		@QueryParam("pf") String pf, 
		@QueryParam("game_server_id") int gameServerId) {
		// 记录日志信息
		RestfulLog.LOG.info(MessageFormat.format(
			"接到请求 : platform_uuid = {0}, pf = {1}, game_server_id = {2}", 
			platformUUId, pf, 
			String.valueOf(gameServerId)
		));
		
		// 创建互斥锁
		ReentrantLock newLock = new ReentrantLock(true);
		// 获取老锁
		ReentrantLock oldLock = _lockMap.putIfAbsent(platformUUId, newLock);

		if (oldLock != null) {
			// 如果老锁不为空, 
			// 则直接指向老锁 ...
			RestfulLog.LOG.warn(MessageFormat.format(
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
				RestfulLog.LOG.error(MessageFormat.format(
					"加锁失败, platformUUId = {0}", 
					platformUUId
				));

				return "";
			}

			// 根据 platformUUId 加锁
			RestfulLog.LOG.error(MessageFormat.format(
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
				RestfulLog.LOG.error(MessageFormat.format(
					"passport 数据为空, platformUUId = {0}", 
					platformUUId
				));

				return "";
			}

			// 创建并写出 JSON 对象
			JSONObject jsonObj = new JSONObject();
			pe.writeJsonObj(jsonObj);
			// 获取 JSON 字符串
			String jsonStr = jsonObj.toString();
			// 记录日志信息
			RestfulLog.LOG.info(MessageFormat.format(
				"准备返回给调用者, jsonStr = {0}", 
				jsonStr
			));

			return jsonStr;
		} catch (Exception ex) {
			// 记录异常信息
			RestfulLog.LOG.error(MessageFormat.format(
				"加锁时发生异常, platformUUId = {0}", 
				platformUUId
			), ex);
		} finally {
			// 给玩家解锁
			newLock.unlock();
			_lockMap.remove(platformUUId);
			// 记录日志信息
			RestfulLog.LOG.info(MessageFormat.format(
				"给玩家解锁, platformUUId = {0}", 
				platformUUId
			));
		}

		return "";
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
			RestfulLog.LOG.warn(
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
