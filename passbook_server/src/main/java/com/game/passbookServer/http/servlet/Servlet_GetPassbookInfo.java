package com.game.passbookServer.http.servlet;

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
import com.game.passbookServer.ServerLog;
import com.game.passbookServer.entity.PassbookEntity_X;

/**
 * 获取 Passbook 信息, 请求示例 :
 * <code>
 * http://127.0.0.1:8001/get_passbook_info?platform_uid_str=qihu360-1001&player_ip_addr=0.0.0.0&pf=wan&game_server_id=1
 * </code>
 * 
 * @author hjj2019
 * @since 2015/2/9
 * 
 */
public class Servlet_GetPassbookInfo extends HttpServlet {
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

        // 获取平台 UId 字符串
        String platformUIdStr = req.getParameter("platform_uid_str");
        // 玩家 IP 地址
        String playerIpAddr = req.getParameter("player_ip_addr");
        // 获取平台 Pf 值
        String pf = req.getParameter("pf");
        // 获取游戏服 Id
        int gameServerId = Integer.parseInt(req.getParameter("game_server_id") != null ? req.getParameter("game_server_id") : "-1");

        // 记录日志信息
        ServerLog.LOG.info(MessageFormat.format(
            "接到请求 : platform_uid_str = {0}, pf = {1}, game_server_id = {2}",
            platformUIdStr, pf,
            String.valueOf(gameServerId)
        ));

        // 输出结果
        res.getWriter().println(this.doAction(
            platformUIdStr,
            playerIpAddr,
            pf, // 游戏平台
            gameServerId
        ));
    }

    /**
     * 执行动作
     * 
     * @param platformUIdStr 平台 UId 字符串
     * @param playerIpAddr 玩家 IP 地址
     * @param pf 登陆平台
     * @param gameServerId 游戏服 Id
     * @return
     * 
     */
    private String doAction(String platformUIdStr, String playerIpAddr, String pf, int gameServerId) {
        if (platformUIdStr == null ||
            platformUIdStr.isEmpty()) {
            // 如果参数对象为空, 
            // 则直接退出!
            return "null_platform_uuid";
        }
        
        // 创建互斥锁
        ReentrantLock newLock = new ReentrantLock(true);
        // 获取老锁
        ReentrantLock oldLock = _lockMap.putIfAbsent(platformUIdStr, newLock);

        if (oldLock != null) {
            // 如果老锁不为空, 
            // 则直接指向老锁 ...
            ServerLog.LOG.warn(MessageFormat.format(
                "将引用指向旧锁, platformUIdStr = {0}",
                platformUIdStr
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
                    "加锁失败, platformUIdStr = {0}",
                    platformUIdStr
                ));

                return "lock_error";
            }

            // 根据 platformUUId 加锁
            ServerLog.LOG.error(MessageFormat.format(
                "加锁成功, platformUIdStr = {0}",
                platformUIdStr
            ));

            // 获取 passbook 数据
            PassbookEntity_X pe = this.getPassbookEntityAndUpdate(
                platformUIdStr,
                playerIpAddr,
                pf, // 游戏平台
                gameServerId
            );

            if (pe == null) {
                // 如果 passbook 数据依然为空,
                // 则直接退出!
                ServerLog.LOG.error(MessageFormat.format(
                    "passbook 数据为空, platformUIdStr = {0}",
                    platformUIdStr
                ));

                return "null_passbook_entity";
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
                "加锁时发生异常, platformUIdStr = {0}",
                platformUIdStr
            ), ex);
        } finally {
            // 给玩家解锁
            newLock.unlock();
            _lockMap.remove(platformUIdStr);
            // 记录日志信息
            ServerLog.LOG.info(MessageFormat.format(
                "给玩家解锁, platformUIdStr = {0}",
                platformUIdStr
            ));
        }

        return "error";
    }

    /**
     * 获取 passbook 实体并保存
     * 
     * @param platformUIdStr 平台 UId 字符串
     * @param playerIpAddr 玩家 IP 地址
     * @param pf 游戏平台
     * @param gameServerId 游戏服 Id
     * @return
     * 
     */
    private PassbookEntity_X getPassbookEntityAndUpdate(
        String platformUIdStr,
        String playerIpAddr,
        String pf,
        int gameServerId) {

        if (platformUIdStr == null ||
            platformUIdStr.isEmpty()) {
            // 如果参数对象为空, 
            // 则直接退出!
            return null;
        }

        // 获取 passbook 数据
        PassbookEntity_X pe = CommDao.OBJ.find(
            PassbookEntity_X.getSplitEntityClazz(platformUIdStr),
            platformUIdStr
        );
        
        // 获取当前时间
        long now = System.currentTimeMillis();

        if (pe == null) {
            // 如果 passbook 数据为空,
            ServerLog.LOG.warn(
                "passbook 数据为空需要新建, platformUIdStr = {0}",
                platformUIdStr
            );

            pe = new PassbookEntity_X();
            pe._platformUIdStr = platformUIdStr;
            pe._createTime = now;
            pe._pf = pf;
        }

        // 设置最后登录时间和最后服务器 Id
        pe._lastLoginTime = now;
        pe._lastLoginIpAddr = playerIpAddr;
        pe._lastGameServerId = gameServerId;
        // 保存数据
        CommDao.OBJ.save(pe.getSplitEntityObj());

        return pe;
    }
}

