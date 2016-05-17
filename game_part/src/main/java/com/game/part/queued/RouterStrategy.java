package com.game.part.queued;

/**
 * 路由策略
 *
 * @author hjj2019
 * @since 2016/5/17
 *
 */
class RouterStrategy {
    /**
     * 根据地址获取索引
     *
     * @param strAddr
     * @param factor
     * @return
     *
     */
    static int indexByAddr(String strAddr, int factor) {
        if (strAddr == null ||
            strAddr.isEmpty()) {
            return 0;
        }

        if (factor <= 0) {
            return 0;
        }

        char lastChar = strAddr.charAt(strAddr.length() - 1);
        return lastChar % factor;
    }
}
