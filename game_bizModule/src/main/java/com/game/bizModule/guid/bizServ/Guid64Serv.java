package com.game.bizModule.guid.bizServ;

import com.game.bizModule.time.TimeServ;
import com.game.gameServer.bizServ.AbstractBizServ;
import com.game.gameServer.framework.GameServerConf;
import com.game.part.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Guid 服务
 *
 * @author hjj2017
 * @since 2015/7/18
 *
 */
public final class Guid64Serv extends AbstractBizServ {
    /** 单例对象 */
    public static final Guid64Serv OBJ = new Guid64Serv();

    /**
     * 最大位数,
     * 因为长整形数的最大数值 = pow(2, 64) - 1,
     * 所以我们这里取 63
     *
     */
    private static final int MAX_BIT = 63;

    /**
     * 平台索引位数,
     * 我们这里取的是 pow(2, 7), 最多可以支持到 255 个平台
     *
     */
    private static final int P_MAX_BIT = 7;

    /**
     * 服务器索引位数
     * 我们这里取的是 pow(2, 12), 最多可以支持到 8191 个服务器
     *
     */
    private static final int S_MAX_BIT = 12;

    /** 数据字典 */
    private final Map<Guid64TypeEnum, Guid64Data> _guidDataMap = new ConcurrentHashMap<>();
    /** 计数器 */
    private final AtomicInteger _counter = new AtomicInteger(0);

    /**
     * 类默认构造器
     *
     */
    private Guid64Serv() {
        super.needToInit(this);
    }

    @Override
    public void init() {
        // 获取基值
        final long baseVal = getBaseVal();
        // 获取极限数列
        final long limitCount = getLimitCount();

        Arrays.asList(Guid64TypeEnum.values()).forEach(typeEnum -> {
            // 创建并初始化 Guid
            Guid64Data guid = new Guid64Data(
                typeEnum,
                baseVal,
                limitCount
            );
            guid.init();

            // 添加到字典
            Guid64Serv.OBJ._guidDataMap.put(
                typeEnum, guid
            );
        });
    }

    /**
     * 获取基值, 基值是由平台 Index 和服务器 Index 推算得出
     *
     * @return
     *
     */
    private static long getBaseVal() {
        // 获取平台和服务器索引
        final long P_IX = GameServerConf.OBJ._platformIndex;
        final long S_IX = GameServerConf.OBJ._serverIndex;
        // 对基值进行位运算
        long baseVal = 0L;
        baseVal = baseVal | P_IX << (MAX_BIT - P_MAX_BIT);
        baseVal = baseVal | S_IX << (MAX_BIT - S_MAX_BIT);

        return baseVal;
    }

    /**
     * 获取极限数量
     *
     * @return
     *
     */
    private static long getLimitCount() {
        // 对基值进行位运算
        long limitCount = (1 << (MAX_BIT - S_MAX_BIT)) - 2;

        if (limitCount < 0) {
            limitCount = 0;
        }

        return limitCount;
    }

    /**
     * 获取下一个 UId
     *
     * @param typeEnum
     * @return
     *
     */
    public long nextUId(Guid64TypeEnum typeEnum) {
        // 断言参数不为空
        Assert.notNull(typeEnum, "typeEnum");
        // 获取 Guid 数据
        Guid64Data gd = this._guidDataMap.get(typeEnum);

        // 断言 Guid 数据不为空,
        Assert.notNull(gd);

        // 获取下一 UId
        return gd.next();
    }

    /**
     * 获取新的 UId 字符串
     *
     * @param serverNamePrefix
     * @return
     *
     */
    public String newUIdStr(String serverNamePrefix) {
        return newUIdStr(serverNamePrefix, null);
    }

    /**
     * 获取随机 UId 字符串, 格式 = 服务器名称前缀/模块名称/年月日-时分秒/计数器
     *
     * @param serverNamePrefix 服务器名称前缀
     * @param moduleName 模块名称
     * @return
     *
     */
    public String newUIdStr(String serverNamePrefix, String moduleName) {
        StringBuilder sb = new StringBuilder();

        if (serverNamePrefix != null &&
            serverNamePrefix.isEmpty() == false) {
            // 添加服务器名称前缀
            sb.append(serverNamePrefix).append("/");
        }

        if (moduleName != null &&
            moduleName.isEmpty() == false) {
            // 添加模块名称
            sb.append(moduleName).append("/");
        }

        // 添加时间字符串, 格式 = 20160101100059
        sb.append((new SimpleDateFormat("yyyyMMdd-HHmmss")).format(TimeServ.OBJ.now())).append("/");
        // 添加计数器数值
        sb.append(_counter.incrementAndGet());

        return sb.toString();
    }
}
