package com.game.gameServer.io;

import com.game.gameServer.framework.Player;

/**
 * 登陆异步操作
 * 
 * @author hjj2017
 * @since 2014/8/14
 * 
 */
public abstract class AbstractLoginIoOper implements IBindUIdIoOper, IMsgDispatchable {
    @Override
    public final String getThreadKey() {
        return "LOGIN."
            + IoConf.OBJ.getLoginThreadIndex(this.getBindUId());
    }

    /**
     * 根据玩家获取 IO 操作的绑定 UId
     *
     * @param p
     * @return
     *
     */
    protected static long getBindUIdByPlayer(Player p) {
        if (p == null ||
            p._platformUId == null ||
            p._platformUId.isEmpty()) {
            // 如果平台 UId 为空,
            // 则直接退出!
            return 0L;
        }

        if (p._platformUId.length() == 1) {
            // 如果平台 UId 就一个字符,
            // 则直接返回!
            return p._platformUId.charAt(0);
        }

        // 获取字符串长度
        int strLen = p._platformUId.length();
        // 获取最后两位字符
        long n1 = p._platformUId.charAt(strLen - 2);
        long n0 = p._platformUId.charAt(strLen - 1);
        // 换算为 10 进制数
        return n1 * 10 + n0;
    }
}
