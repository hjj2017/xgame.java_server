package com.game.part.util;

/**
 * 字节实用工具类
 * 
 * @author hjj2017
 * @since 2014/3/14
 * 
 */
public class BytesUtil {
    /**
     * 将 Int 数字转换为字节数组
     *
     * @param src
     * @return
     *
     */
    public static byte[] int2bytes(int src) {
        // 创建字节数组
        byte[] result = new byte[4];

        // 最低位
        result[3] = (byte)(src & 0xff);
        // 次低位
        result[2] = (byte)((src >> 8) & 0xff);
        // 次高位
        result[1] = (byte)((src >> 16) & 0xff);
        // 最高位
        result[0] = (byte)(src >>> 24);

        return result;
    }

    /**
     * 将字节数组转换成整数值
     *
     * @param src
     * @return
     *
     */
    public static int bytes2int(byte[] src) {
        int value= 0;

        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (src[i] & 0x000000FF) << shift; // 往高位游
        }

        return value;
    }
}
